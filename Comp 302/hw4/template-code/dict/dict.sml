structure Dict : DICT = 
struct

datatype 'a trie = Node of 'a * ('a trie) list | Empty;

type dict = (char trie) list

val empty = []

exception notImplemented 
(* -------------------------------------------------------------*)
(* QUESTION 1.1 : Insert a string into a trie  [20 points]      *)
(* -------------------------------------------------------------*)

(* Insert a word in a dictionary *)
(* Duplicate inserts are allowed *)

(* ins: char list * (char trie) list -> (char trie) list *)
fun  ins ([], []) = [Empty]
    | ins([], ts) = Empty::ts
    | ins (n::s, []) = [Node(n, ins(s, []))]
    | ins (n::s, Empty::ts) = Empty::ins(n::s, ts)
    | ins (n::s,  (m as Node(a, l))::ts) = if(n = a) then (Node(a, ins(s, l))::ts) else m::ins(n::s, ts)
    
(* insert : string * (char trie) list -> (char trie) list *)
fun insert(s, t) = 
  let
    val l = String.explode s  (* turns a string into a char list *)
  in
    ins(l,t) 
    handle notImplemented => (print "Note: Insertion into a trie is not (yet) implemented.\n";empty)
  end 

(* -------------------------------------------------------------*)
(* QUESTION 1.2 : Find all words in the trie                    *) 
(* -------------------------------------------------------------*)
(* Find all words in the trie using continuations               *)

fun words_in_trie Empty cont = cont []
  | words_in_trie (Node(x, ys)) cont = 
  all_words ys (fn zs => cont( map (fn q => x::q) zs))

and all_words [] cont = cont []
  | all_words (x::xs) cont = (words_in_trie x cont) @ (all_words xs cont)

    
(*fun find_all2 t = all_words t (fn l => l)
fun find_all t =  (map (fn w => String.implode w) (find_all2 t)) *)
fun find_all t = 
  all_words t (fn l => map (fn w => String.implode w) l)
  handle notImplemented => 
    (print "Note: Retrieving all words in a trie is not (yet) implemented.\n"; [])

val sample_dict = 
 [Node
     (#"b",
      [Node (#"r",[Node (#"e",[Node (#"e",[Empty])])]),
       Node
         (#"e",
          [Node (#"d",[Empty]),
           Node
             (#"e",
              [Empty,Node (#"f",[Empty,Node (#"y",[Empty])]),
               Node (#"r",[Empty])]),
           Node (#"a",[Node (#"r",[Empty,Node (#"d",[Empty])])])])])]




end;
