(* Evaluation of MinML Expression via big step semantics *)
signature EVAL =
sig

  exception Stuck of string

  val subst : (MinML.exp * MinML.name) -> MinML.exp -> MinML.exp

  (* big-step evaluation; raises Stuck if impossible *)
  val eval : MinML.exp -> MinML.exp

end;  (* signature EVAL *)


structure Eval :> EVAL =
struct

  open MinML

  structure S = Stream

  exception Stuck of string
  exception Done

  exception notImplemented

 local
    (* local counter to guarantee new internal variables *)
    val counter = ref 0
  in
    (* freshVar () ===> a,  where a is a new internal variables *)
    (*
       Internal variables are natural numbers,
       so they cannot conflict with external variables.
    *)
    fun freshVar () =
	(counter := !counter+1;
	 Int.toString (!counter))

  fun resetCtr () = 
    counter := 0
  end


(* ------------------------------------------------------------ *)
(* QUESTION : Substitution                                      *) 
(* ------------------------------------------------------------ *)
(* Substitution 

   subst (e',x) e = [e/x]e 

  subst which will replace any occurrence of the variable x 
  in the expression e' with e.

  subst: (exp * string) -> exp -> exp

  YOUR TASK: Extend the function below to also handle the cases
  for let-name, pairs, projections (fst and snd), functions, 
  function application, and recursion. 

 *)

  fun substArg s ([]) = []
    | substArg s (a::args) = (subst s a)::(substArg s args)

  and subst (e,x) (Var y) = 
    if (x = y) then e
      else Var y
    (* arithmetic operations and integers *)
    | subst s (Int n) = Int n 
    | subst s (Bool b) = Bool b
    | subst s (Primop(po, args)) = Primop(po, substArg s args)
    | subst s (If(e, e1, e2)) = 
	If(subst s e, subst s e1, subst s e2)
    | subst s (Let (e, (x,e'))) = 
	let
	  val e1 = subst s e
	  val (x',e1') = rename (x,e')
	  val e2 = subst s e1'
	in 
	  Let (e1, (x', e2))
	end 
    | subst s _ = raise notImplemented

and rename (x, e) = 
  let
    val x' = freshVar () 
  in
    (x', subst (Var x', x) e)
  end


(*-------------------------------------------------------------------*)
(* QUESTION : Evaluation                                             *) 
(*-------------------------------------------------------------------*)
(* YOUR TASK: Implement the evaluator such that we can
   evaluate these expressions. Follow the description presented in
   class.
*)


(* eval : exp -> exp *)
and eval _ = raise Stuck "Not Implemented"

end;  (* structure Eval *)

structure E = Eval