(* Type checking MinML *)
(* Operates on MinML expressions 
(where variables are encoded as strings) *)

signature TYPING =
sig

  exception Error of string

  (* typeOf (e) = t for the t such that |- e : t *)
  (* raises Error if no such t exists *)
  val typeOf : MinML.exp -> T.typ
  
end;  (* signature TYPING *)

structure Typing :> TYPING =
struct

  open T
  open MinML

  exception Error of string

  (* Contexts *)
  datatype 'a Ctx =			(* Contexts                   *)
    Null				(* G ::= .                    *)
  | Decl of 'a Ctx * 'a			(*     | G, D                 *)


(* The context x:int, y:bool is then represented as

   Decl(Decl(Null,("y", BOOL)), ("x", INT))
*)

fun lookup (Null, x) = NONE
  | lookup (Decl (G, (y, t)), x) = if x = y then SOME t else lookup (G, x)

(* ------------------------------------------------------------ *)
(* QUESTION : Type Checking                                     *) 
(* -------------------------------------------------------------*)
(* typeOf: MinML.exp  -> T.typ
 
   Implement a function typeOf which infers the type for
   an annotated expression. NOTE: we use type annotations
   to resolve ambiguity. You do not need to consider full
   type inference for expressions without annotations!

   raises exception Error if the input expression e has
   no type.

   Implement typeOf, using the auxiliary function 

  typeOf' (G,e) = T   s.t. G |- e:T 

  typeOf' infers the type T for expression e in the typing context G 
  If e has no type, it will raise the exception typeError.

  typeOf : (string * typ) Ctx * MinML.exp -> T.typ

*)

    fun typeOf e = typeOf'(Null, e)
    
    and typeOf' (G, (M.Int v)) = (T.INT)
    | typeOf' (G, (M.Bool b)) = (T.BOOL)
    | typeOf' (G, (M.If(e, e1, e2))) = 
        let 
            val b = typeOf'(G, e)
            val t1 = typeOf'(G, e1)
            val t2 = typeOf'(G, e2)
        in
            if(b = T.BOOL) then
                if(t1 = t2) then 
                    t1 
                else 
                    raise Error "typeError: e1 and e2 are not of the same type"
            else raise Error "typeError: e must be bool"
        end
    | typeOf' (G, (M.Primop (p, (e::l)))) = 
        let 
           val (x, r) = (typeOfOp p)
        in
            r
        end
    | typeOf' (G, (M.Pair (e1, e2))) = T.CROSS((typeOf e1) , (typeOf e2))
    | typeOf' (G, (M.Fst e)) =
        let 
            val T.CROSS(t1,t2) = typeOf'(G, e)
        in 
            t1
        end
    | typeOf' (G, (M.Snd e)) = 
        let 
            val T.CROSS(t1,t2) = typeOf'(G, e)
        in 
            t2
        end
  
    | typeOf' (G, (M.Fun(t1, t2, (f, x, e)))) = 
        if(typeOf'(Decl(Decl(G, (x, t1)), (f, T.ARROW(t1, t2))), e) = t2) then
            T.ARROW(t1, t2)
        else
            raise Error "TypeError: Fun, T2 and T must be the same"
        
    | typeOf' (G, (M.Fn(t1, (x, e)))) =
        T.ARROW(t1, typeOf'(G, e))
    | typeOf' (G, (M.Let(e1, (x,e2)))) = typeOf'(Decl(G, (x, typeOf'(G, e1))),e2)
    
    | typeOf' (G, (M.Letp(e1, (x,y,e2)))) = 
        let
            val xt = typeOf'(G, M.Fst(e1))
            val yt = typeOf'(G, M.Snd(e1))
        in
            typeOf'(Decl(Decl(G, (x, xt)), (y,yt)), e2)
        end
    | typeOf' (G, M.Apply(e1,e2)) = 
        let 
            val t1 as T.ARROW(t', t) = typeOf'(G, e1)
            val t2 = typeOf'(G, e2)
        in
            if(t2 = t') then
                t
            else
                raise Error "TypeError: Aplly, T2 and T' must be the same"
        end
    | typeOf' (G, (M.Var v)) = 
        let 
            val t =  lookup(G, v)
            
        in
           if(t = NONE) then
                raise Error ("TypeError: Unknow variable: " ^ v)
           else
                (valOf t)
        end
    
end;  (* structure Typing *)
