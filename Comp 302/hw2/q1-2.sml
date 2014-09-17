(* -----------------------------------------------------------------------------*)
(* HOMEWORK 2                                                                   *)
(*                                                                              *)
(* NOTE: This homework has 2 parts: hw2.sml and parser.sml                      *)
(* THIS FILE CONTAINS CODE FOR QUESTION 1 and QUESTION 2                        *)
(* -----------------------------------------------------------------------------*)

exception Unimplemented

(* -----------------------------------------------------------------------------*)
(* QUESTION 1: Maximum Likelihood                                               *)
(* -----------------------------------------------------------------------------*)

fun factorial 0 = 1 
  | factorial n =  n * factorial (n-1)

fun fact n = if n <= 0 then 1 else factorial n

fun binom (n, k) = 
  if n < k then 0.0
    else 
      Real.fromInt (fact (n)) / (Real.fromInt (fact (k)) * 
				 Real.fromInt (fact (n - k)))

fun simple_dist N = (N * (N - 1.0) * (10.0 - N)) / 240.0

(* X = BlackMarblesDrawn *)
fun dist_black N X (MarblesTotal, MarblesDrawn) = 
  (binom (N, X) * binom (MarblesTotal - N, MarblesDrawn - X)) 
  / binom(MarblesTotal, MarblesDrawn)

(* -----------------------------------------------------------------------------*)
(* Q1.1 : Compute the distribution table.                                       *)
(* -----------------------------------------------------------------------------*)
 
fun dist_table (MarblesTotal, MarblesDrawn) X = 
    List.tabulate (MarblesTotal + 1, fn N => dist_black N X (MarblesTotal, MarblesDrawn))
        
 


(* -----------------------------------------------------------------------------*)
(* Compute the maximum of the dist_table. The maximum corresponds to the number *)
(* of black marbels which is most likely to be in an urn *)

fun max_in_list l = 
  let 
    fun max_in_list' pos [h] = (pos, h)
	| max_in_list' pos (h::t) = 
      let val (q, mx) = max_in_list' (pos+1) t in 
	if Real.< (h, mx) then (q,mx)
	else (pos, h)
      end 
    val (pos, _) = max_in_list' 0 l 
  in 
    pos
  end


(* -----------------------------------------------------------------------------*)
(* Q 1.2: Compute the distribution matrix                                       *)
(* -----------------------------------------------------------------------------*)

fun dist_matrix (total, drawn) resultList = List.map (fn x => dist_table (total, drawn) x) resultList

(* -----------------------------------------------------------------------------*)
(* Q 1.3: Compute the combined distribution table                               *)
(* -----------------------------------------------------------------------------*)
fun emptyMatrix matrix = 
List.all (fn l => case l of [] => true | _ => false) matrix

fun combined_dist_table matrix = List.tabulate(List.length(List.hd(matrix)), fn n => 
    List.foldr(fn(x,m) => List.nth(x, n)*m) 1.0 matrix) 

(* -----------------------------------------------------------------------------*)
(* Maximum Likelihood                                                           *)
fun max_likelihood (total, drawn)  resultList = 
   max_in_list 
   (combined_dist_table 
    (dist_matrix (total, drawn) resultList))

(*

Example: 

Combined distribution table for Total = 10, Drawn = 3, 
and ResultList = [2,0]

  [0.0,0.0,0.031,0.051,0.05,0.035, 0.017,0.004,0.0,0.0,0.0] : real list

The maximum in this list is at position 3. Hence, it is most likely 
that there are 3 black marbels in the urn. 

*)
(* ------------------------------------------------------------------------- *)
(* QUESTION 2: Church Lists                                                  *)
(* ------------------------------------------------------------------------- *)
(* Representation of lists a la Church 

[]    ==  fn c => fn t => t 
[1,2] ==  fn c => fn t => c (1, c (2, t)) 

 *)
 
val empty  =  (fn c => fn t => t)

fun list2clist [] = empty
    | list2clist l = (fn c => fn t => (List.foldr c t l))

fun clist2list l = l (op ::) nil

fun app (l1, l2) = fn c => fn t => (l1 c (l2 c t))

fun add l = l (op +) 0
