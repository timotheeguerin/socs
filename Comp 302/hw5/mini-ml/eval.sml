(* Evaluation of MinML Expression *)
signature EVAL =
sig

  exception Stuck of string

  (* evaluation, raises Stuck if impossible *)
  val eval : MinML.exp -> MinML.exp
  val subst: (MinML.exp * MinML.name) -> MinML.exp -> MinML.exp

end;  (* signature EVAL *)

structure Eval :> EVAL =
struct

  open T
  open MinML
  structure S = Stream

  exception Stuck of string
  exception Done 

 local
    (* local counter to guarantee new internal variables *)
    val counter = ref 0
  in
    (* newVar () ===> a,  where a is a new internal variables *)
    (*
       Internal variables are natural numbers,
       so they cannot conflict with external variables.
    *)
    fun newVar () =
	(counter := !counter+1;
	 Int.toString (!counter))
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
    | subst s (Pair(e1,e2)) = Pair(subst s e1, subst s e2)
    | subst s (Letp(e,(x,y,e'))) = 
	let
	  val e1 = subst s e
	  val (x',e1') = rename (x,e')
          val (y',e2') = rename (y,e1')
	  val e2 = subst s e2'
	in 
	  Letp (e1, (x', y', e2))
	end
    | subst s (Fst(e)) = Fst(subst s e)
    | subst s (Snd(e)) = Snd(subst s e)
    | subst s (Fun(t1,t2,(x,y,e))) = Fun(t1,t2,(x,y,(subst s e)))
    | subst s (Fn(t,(x,e))) = Fn(t,(x,(subst s e)))
    | subst s (Apply(e1,e2)) = Apply(subst s e1, subst s e2)

and rename (x, e) = 
  let
    val x' = newVar () 
  in
    (x', subst (Var x', x) e)
  end


(*-------------------------------------------------------------------*)
(* QUESTION: Evaluation                                              *)
(*-------------------------------------------------------------------*)
(* YOUR TASK: Implement the evaluator such that we can
   evaluate these expressions. Follow the description presented in
   class.
*)


(* eval : exp -> exp *)
and eval (Int n) = Int n
  | eval (Bool b) = Bool b
  | eval (Primop (p,es)) =
      (case (evalOp (p, map eval es)) of
         SOME v => v
       | NONE   => raise Stuck "Failed to evaluate primitive op")
  | eval (If (e1,e2,e3)) =
      (case (eval e1) of
          Bool true => eval e2
        | Bool false => eval e3
        | _ => raise Stuck "Condition did not evaluate to a boolean")
  | eval (Pair(x,y)) = Pair(eval(x),eval(y))
  | eval (Fst(x)) = 
      let
        val Pair(v1,v2) = eval(x)
      in
        v1
      end
  | eval (Snd(x)) = 
      let
        val Pair(v1,v2) = eval(x)
      in
        v2
      end
  | eval (Fun(t1,t2,(v1,v2,x))) = (Fun(t1,t2,(v1,v2,x)))
  | eval (Fn(t,(v,x))) = Fn(t,(v,x))
  | eval (Let(x,(v,y))) = eval(subst (x,v) y)
  | eval (Letp(x,(a,b,y))) = 
      let
        val Pair(v1,v2) = eval(x)
        val e1 = subst (v1,a) y
        val e2 = subst (v2,b) e1
      in
        eval(e2)
      end
  | eval (Apply(e1,e2)) = 
      (case (eval e1) of 
          Fn (t, (y, e3)) => eval(subst ((eval e2), y) e3)
        | _ => raise Stuck "Not function!")
  | eval (Var(v)) = raise Stuck "Free variable!"


end;  (* structure Eval *)
