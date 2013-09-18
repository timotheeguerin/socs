(* Type checking MinML *)
(* Operates on MinML expressions 
(where variables are encoded as strings) *)

signature TYPING =
sig

  exception Error of string

  (* typeOf (e) = t for the t such that |- e : t *)
  (* raises Error if no such t exists *)
  val typeOf : MinML.exp -> T.typ

 
  (* unify (T1, T2) = () if there exists some substitution s
     for the free type variables in T1 and T2 s.t.
     [s]T1 = [s]T2 
   *)
  val unify : T.typ * T.typ -> unit

end;  (* signature TYPING *)

structure Typing :> TYPING =
struct

  open T
  open MinML

  exception Error of string


(* ------------------------------------------------------------ *)
(* QUESTION : Unification                                       *) 
(* -------------------------------------------------------------*)

(* unify: typ * typ -> unit 

   unify(T1, T2) = () 
   
   succeeds if T1 and T2 are unifiable, fails otherwise.

   Side Effect: Type variables in T1 and T2 will have been
    updated, s.t. if T1 and T2 are unifiable AFTER unification
    they will denote the "same" type.

*)
  fun unify (INT, INT) = ()
    | unify (BOOL, BOOL) = ()
    | unify (ARROW (a, b), ARROW (c, d)) = (unify (a, c); unify (b, d))
    | unify (CROSS (a, b), CROSS (c, d)) = (unify (a, c); unify (b, d))
    | unify (VAR (ref (SOME x)), t) = unify (x, t)
    | unify (t, VAR (ref (SOME x))) = unify (t, x)
    | unify (t1 as VAR (a as (ref NONE)), t2 as VAR (b as (ref NONE))) = 
            if a = b then ()
            else a := SOME t2
    | unify (t1 as VAR (a as (ref NONE)), t2) = 
            if unify' (a, t2) then raise Error "Not unifiable!"
            else a := SOME t2
    | unify (t1, t2 as VAR (a as (ref NONE))) = 
            if unify' (a, t1) then raise Error "Not unifiable!"
            else a := SOME t1
    | unify (_, _) = raise Error "Not unifiable!"

  and unify' (t1 as ref (t), t2) =
             (case t2 of INT => false
                 | BOOL => false
                 | ARROW (a, b) => unify' (t1, a) orelse unify' (t1, b)
                 | CROSS (a, b) => unify' (t1, a) orelse unify' (t1, b)
                 | VAR (b as ref (SOME(a))) => unify' (t1, a)
                 | VAR (b as ref (NONE)) => (t1 = b))


fun unifiable (t, t') = 
  (unify (t, t') ; true) handle Error s => false

(* Use Print.typToString: T.typ -> string to print types *)

(* ------------------------------------------------------------ *)
(* QUESTION  : Type Inference (EXTRA CREDIT)                    *) 
(* -------------------------------------------------------------*)
(* typeOf: MinML.exp  -> T.typ
 
   Implement a function typeOf which infers the most general type 
   for an expression. 

   raises exception Error if the input expression e has
   no type.

   Implement typeOf, using the auxiliary function 

  typeOf' (G,e) = T   s.t. G |- e:T 

  typeOf' infers the type T for expression e in the typing context G 
  If e has no type, it will raise the exception typeError.

  typeOf : (string * typ) Ctx * MinML.exp -> T.typ


  Adapt your implementation for inferring a type given an annotated
  expression, to allow for full type inference. 

*)

  (* Contexts *)
  datatype 'a Ctx =			(* Contexts                   *)
    Null				(* G ::= .                    *)
  | Decl of 'a Ctx * 'a			(*     | G, D                 *)


(* The context x:int, y:bool is then represented as

   Decl(Decl(Null,("y", BOOL)), ("x", INT))
*)


  fun freshVAR () = ref NONE

  fun typeOf (e) = raise Error "Not Implemented YET! "


end;  (* structure Typing *)
