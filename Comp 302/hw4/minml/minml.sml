(* MinML Abstract Syntax abstracting over the concrete representation
   of variables which is defined in the structure Var with signature VAR.
*)
signature MINML =
sig

  structure Var : VAR 

  datatype primop = Equals | LessThan | Plus | Minus | Times | Negate

  (* Expression *)
  (* Binders are grouped with their scope *)
  datatype exp =
      Int of int			(* k *)
    | Bool of bool			(* true or false *)
    | If of exp * exp * exp		(* if e then e1 else e2 fi *)
    | Pair of exp * exp                 (* (e1, e2) *)
    | Fst of exp                        (* fst exp *)
    | Snd of exp                        (* snd exp *)
    | Primop of primop * exp list	(* e1 <op> e2  or  <op> e *)
    | Fun of Var.bind * Var.bind * exp  (* fun f(x) is e end *)
    | Let of exp * (Var.bind * exp)     (* let x be e1 in e2 end *)
    | Apply of exp * exp		(* e1 e2 *)
    | Var of Var.var			(* x *)

  exception Error of string 

  val closed : exp -> unit

  val toString : exp -> string
end;  (* signature MINML *)


(* MinML *)
(* Shared between representations *)
functor MinML (structure V : VAR)
 :> MINML where type Var.bind = V.bind and type Var.var = V.var
=
struct

  structure Var : VAR = V

  (* Variables *)
  (* type bind = Variable.bind
  type var = Variable.var *)

  datatype primop = Equals | LessThan | Plus | Minus | Times | Negate

  (* Expressions *)
  (* Binders are grouped with their scope *)
  datatype exp =
      Int of int			(* k *)
    | Bool of bool			(* true or false *)
    | If of exp * exp * exp		(* if e then e1 else e2 fi *)
    | Pair of exp * exp                 (* (e1, e2) *)
    | Fst of exp                        (* fst exp *)
    | Snd of exp                        (* snd exp *)
    | Primop of primop * exp list	(* e1 <op> e2  or  <op> e *)
    | Fun of Var.bind * Var.bind * exp          (* fun f(x) is e end *)
    | Let of exp * (Var.bind * exp)		(* let x be e1 in e2 end *)
    | Apply of exp * exp		(* e1 e2 *)
    | Var of Var.var			(* x *)

(* ******************************************************************* *)

(* Pretty printing *)

  fun paren lvl oplvl string =
    if oplvl < lvl then "(" ^ string ^ ")"
    else string

  fun po_prec Equals = 3
    | po_prec LessThan = 3
    | po_prec Plus = 4
    | po_prec Minus = 4
    | po_prec Times = 5
    | po_prec Negate = 7

  fun po Equals = "="
    | po LessThan = "<"
    | po Plus = "+"
    | po Minus = "-"
    | po Times = "*"
    | po Negate = "~"

  and expstr lvl e = let val f = expstr in case e of
      Int i => Int.toString i
    | Bool true => "true"
    | Bool false => "false"
    | If (ec, et, ef) =>
        paren lvl 1 ("if " ^ f 2 ec ^ " then " ^ f 2 et ^ " else " ^ f 2 ef)
    | Primop (p, []) => "(bad primop)"
    | Primop (p, [e]) => paren lvl 7 (po p ^ f 7 e)
    | Primop (p, e::es) =>
        paren lvl (po_prec p) (foldl (fn (a, b) => b ^ " " ^ po p ^ " " ^ f (po_prec p) a) (f (po_prec p) e) es)
    | Pair (e1, e2) => "(" ^ (f 0 e1) ^ "," ^ (f 0 e2) ^ ")"
    | Fun (g, x, e) =>
        paren lvl 2 ("fun " ^ 
	V.bindToString g ^ " " ^ V.bindToString x ^  " => " ^ f 0 e)

    | Let (e1, (x,e)) =>
       "let val " ^ V.bindToString x ^ "=" ^ f 0 e1 ^ " in " ^ f 0 e ^ " end"
    | Apply (e1, e2) =>
        paren lvl 6 ((f 6 e1) ^ " " ^ (f 7 e2))
    | Var v => V.varToString v
    | Fst e => paren lvl 1 ("fst " ^ f 2 e)
    | Snd e => paren lvl 1 ("snd " ^ f 2 e)
  end

  and toString e = expstr 0 e


(* **************************************************************************)
  exception Error of string 
(* closed: exp -> bool 
 
 Implement a function closed which checks  whether an expression 
 is closed, i.e. it does not contain any free variables - for the
 variable case use the function inbound defined in var.sml         *)


  fun closed e = 
  let
    fun closed' ((Int n), l) k = k ()
      | closed' ((Bool b), l) k = k()
      | closed' ((If (e, e1, e2)), l) k = 
     closed' (e , l) (fn () => closed' (e1, l) (fn() => closed' (e2, l) k))
      | closed' ((Pair (e1, e2), l)) k =
     closed' (e1, l) (fn() => closed' (e2, l) k)
      | closed' ((Fst (e)), l) k = 
     closed' (e, l) k
      | closed' ((Snd (e)), l) k = 
     closed' (e, l) k
      | closed' ((Primop (pop, x::xs)), l) k = 
     closed' (x, l) (fn() => closed'(Primop (pop, xs), l) k)
      | closed' ((Primop (pop, [])), l) k = k()
      | closed' ((Fun (x1, x2, e)), l) k =
     closed' (e, x1::x2::l) k
      | closed' ((Let (e1, (x, e2))), l) k =
     let val ls = x::l in closed' (e1, ls) (fn() => closed' (e2, ls) k) end
      | closed' ((Apply (e1, e2)), l) k =
     closed' (e1, l) (fn() => closed' (e2, l) k)
      | closed' ((Var x), l) k = 
     if Var.inbound x l then k() else (let val s = Var.varToString x in raise Error s end)

  in
    closed' (e, []) (fn () => ())
  end


end;  (* functor MinML *)


(* Specializing the functor to obtain a structure 
   where variables are represented with names *)
structure MinML = MinML (structure V = Named);

(* Specializing the functor to obtain a structure 
   where variables are represented with de Bruijn indices *)
structure DeBruijn = MinML (structure V = DB);
