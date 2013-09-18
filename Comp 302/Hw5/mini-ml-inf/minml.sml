(* MinML Abstract Syntax *)
signature TYPE =
sig
  (* MinML types *)
  datatype typ =
      INT
    | BOOL
    | ARROW of typ * typ
    | CROSS of typ * typ
    | VAR of (typ option) ref
end;  (* signature TP *)

structure T :> TYPE =
struct
  (* MinML types *)
  datatype typ =
      INT
    | BOOL
    | ARROW of typ * typ
    | CROSS of typ * typ
    | VAR of (typ option) ref  
end;  (* structure T *)


signature MINML =
sig
  (* Variables *)
  type name = string

  (* Primitive operations *)
  datatype primop = Equals | LessThan | Plus | Minus | Times | Negate

  (* Expression *)
  (* Binders are grouped with their scope *)
  datatype exp =
      Int of int			(* k *)
    | Bool of bool			(* true or false *)
    | If of exp * exp * exp		(* if e then e1 else e2 fi *)
    | Primop of primop * exp list	(* e1 <op> e2  or  <op> e *)
    | Pair of exp * exp                 (* (e1, e2) *)
    | Fst of exp                        (* fst e *)
    | Snd of exp                        (* snd e *)
    | Fun of (name * name * exp) (* fun f(x:t1):t2 is e end *)
    | Fn of (name * exp) (* fn (x:t1):t2 is e end *)
    | Let of exp * (name * exp)		(* let x = e1 in e2 end *)
    | Letp of exp * (name * name * exp) (* let pair (x,y) = e1 in e2 end *)
    | Apply of exp * exp		(* e1 e2 *)
    | Var of name			(* x *)

  (* given a primop and some arguments, try to apply it *)
  val evalOp : primop * exp list -> exp option

  val typeOfOp : primop -> (T.typ list * T.typ)

end;  (* signature MINML *)

(* MinML *)
(* Shared between representations *)
structure MinML :> MINML =
struct

  (* Variables *)
  type name = string

  (* Primitive operations *)
  datatype primop = Equals | LessThan | Plus | Minus | Times | Negate

  (* Expressions *)
  (* Binders are grouped with their scope *)
  datatype exp =
      Int of int			(* k *)
    | Bool of bool			(* true or false *)
    | If of exp * exp * exp		(* if e then e1 else e2 fi *)
    | Primop of primop * exp list	(* e1 <op> e2  or  <op> e *)
    | Pair of exp * exp                 (* (e1, e2) *)
    | Fst of exp                        (* fst e *)
    | Snd of exp                        (* snd e *)
    | Fun of name * name * exp          (* fun f(x) is e end *)
    | Fn of  name * exp                 (* fn f(x) is e end *)
    | Let of exp * (name * exp)		(* let x  = e1 in e2 end *)
    | Letp of exp * (name * name * exp) (* let pair (x,y) = e1 in e2 end *)
    | Apply of exp * exp		(* e1 e2 *)
    | Var of name			(* x *)

  (* Evaluation of primops on evaluated arguments *)
  fun evalOp (Equals, [Int i, Int i']) = SOME (Bool (i = i'))
    | evalOp (LessThan, [Int i, Int i']) = SOME (Bool (i < i'))
    | evalOp (Plus, [Int i, Int i']) = SOME (Int (i + i'))
    | evalOp (Minus, [Int i, Int i']) = SOME (Int (i - i'))
    | evalOp (Times, [Int i, Int i']) = SOME (Int (i * i'))
    | evalOp (Negate, [Int i]) = SOME (Int (~i))
    | evalOp _ = NONE

  fun typeOfOp Equals  = ([T.INT, T.INT], T.BOOL)
    | typeOfOp LessThan  = ([T.INT, T.INT], T.BOOL)
    | typeOfOp Plus   = ([T.INT, T.INT], T.INT)
    | typeOfOp Minus  = ([T.INT, T.INT], T.INT)
    | typeOfOp Times  = ([T.INT, T.INT], T.INT)
    | typeOfOp Negate = ([T.INT], T.INT)

end;  (* structure MinML *)


structure M = MinML 