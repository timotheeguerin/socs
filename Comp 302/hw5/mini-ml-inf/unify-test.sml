(* TEST FILE FOR unification *)

(* Equality testing on types                                         *)
(* equal(t,s) = bool
   
   checks whether type t and s are equal 
  
   equal:tp * tp -> true 
*)
fun equal(T.INT, T.INT) = true
  | equal(T.BOOL, T.BOOL) = true
  | equal(T.ARROW(t1, t2), T.ARROW(s1, s2)) = 
     equal(t1, s1) andalso equal(t2, s2)
  | equal(T.CROSS(t1, t2), T.CROSS(s1, s2)) = 
     equal(t1, s1) andalso equal(t2, s2)
  | equal(T.VAR (a as ref(SOME(t))), s) = 
     equal(t, s)
  | equal(t, T.VAR(a as ref(SOME(s)))) = 
     equal(t, s)
  | equal(T.VAR(a as ref(NONE)), T.VAR(b as ref(NONE))) = 
     a = b
  | equal(t, s) = false


(* -------------------------------------------------------------------------*)
(* Some test cases *)
(* -------------------------------------------------------------------------*)
(* Define some type variables *)
val a1 : (T.typ option) ref = ref(NONE);
val a2 : (T.typ option) ref = ref(NONE);

val a3 : (T.typ option) ref = ref(NONE);
val a4 : (T.typ option) ref = ref(NONE);

val a5 : (T.typ option) ref = ref(NONE);
val a6 : (T.typ option) ref = ref(NONE);

val a7 : (T.typ option) ref = ref(NONE);
val a8 : (T.typ option) ref = ref(NONE);

(* Define some types *)
val t1 = T.ARROW(T.CROSS(T.VAR(a1), T.VAR(a1)), T.VAR(a2));
val t2 = T.ARROW(T.CROSS(T.INT, T.INT), T.VAR(a1));

val t3 = T.ARROW(T.CROSS(T.VAR(a3), T.VAR(a4)), T.VAR(a4));
val t4 = T.ARROW(T.CROSS(T.VAR(a4), T.VAR(a3)), T.VAR(a3));

val t5 = T.ARROW(T.CROSS(T.VAR(a5),T.VAR(a6)), T.VAR(a6));
val t6 = T.ARROW(T.VAR(a6), T.VAR(a5));

Control.Print.printDepth := 100;

structure Tp = Typing;

(* Tests *)
(*
val it = () : unit
- Tp.unify(t1, t2);
val it = () : unit
- Print.typToString t1;
val it = "((int ) * (int )) -> (int )" : string
- Print.typToString t2;
val it = "((int ) * (int )) -> (int )" : string
- equal (t1,t2);
val it = true : bool
- unify(t3, t4);
stdIn:12.1-12.6 Error: unbound variable or constructor: unify
- Tp.unify(t3, t4);
val it = () : unit
- Print.typToString t3;
val it = "((a1) * (a1)) -> (a1)" : string
- Print.typToString t4;
val it = "((a1) * (a1)) -> (a1)" : string
- equal (t3,t4);
val it = true : bool
- equal (t5,t6);
val it = false : bool
- Tp.unify (t5,t6);

uncaught exception Error
  raised at: ...
-  

*)
