(* Printing expressions *)
signature PRINT =
sig
  type exp

  (* print a type *)
  val typToString : T.typ -> string

  (* print an expression *)
  val expToString : exp -> string
end

signature NAMEDPRINT = PRINT 
    where type exp = MinML.exp

(*--------------------------------------------------*)
structure Print :> NAMEDPRINT =
struct

    structure M = MinML
    type exp = M.exp

    fun po M.Equals = "="
      | po M.LessThan = "<"
      | po M.Plus = "+"
      | po M.Minus = "-"
      | po M.Times = "*"
      | po M.Negate = "~"

    fun typToString T.INT = "int "
      | typToString T.BOOL = "bool "
(*      | typToString (T.VAR a) = a *)
      | typToString (T.ARROW(d, r)) = 
	"(" ^ typToString d ^ ") -> (" ^ typToString r ^ ")"
      | typToString (T.CROSS(d, r)) = 
	"(" ^ typToString d ^ ") * (" ^ typToString r ^ ")"
 

    and expToString (M.Int i) = Int.toString i
      | expToString (M.Bool true) = "true"
      | expToString (M.Bool false) = "false"
      | expToString (M.If (ec, et, ef)) = 
	"if " ^ expToString ec ^ " then " ^
	expToString et ^ " else " ^ expToString ef ^ " fi "
      | expToString (M.Primop (p, nil)) = "(bad primop)"
      | expToString (M.Primop (p, e::es)) = 
	"(" ^ foldl (fn (a, b) => "(" ^ b ^ ")" ^ po p ^ "(" ^ expToString a ^ ")")
	(expToString e) es ^ ")"
      | expToString (M.Pair(e1,e2)) = "(" ^ expToString e1 ^ " , " ^ expToString e2 ^ ")"
      | expToString (M.Fst e) = "(fst " ^ expToString e ^ ")"
      | expToString (M.Snd e) = "(snd " ^ expToString e ^ ")"

      | expToString (M.Fun (t1, t2, (f, x, e))) = 
	"fun " ^ f ^ "(" ^ x ^ " : " ^ typToString t1 ^ ") : " ^
	typToString t2 ^ " is " ^ expToString e ^ " end "

      | expToString (M.Fn (t1, (x, e))) = 
	"fn " ^ "(" ^ x ^ " : " ^ typToString t1 ^ ")  " ^
	" is " ^ expToString e ^ " end "
      | expToString (M.Let (e1, (x, e2))) = 
	"let " ^ x ^ " = " ^ expToString e1 ^ " in " ^
	expToString e2 ^ " end "
(*      | expToString (M.Letn (e1, (x, e2))) = 
	"let name " ^ x ^ " = " ^ expToString e1 ^ " in " ^
	expToString e2 ^ " end "*)
      | expToString (M.Letp (e1, (x, y, e2))) = 
	"let pair (" ^ x ^ " , " ^ y ^ ") = " ^ expToString e1 ^ " in " ^
	expToString e2 ^ " end "
      | expToString (M.Apply (f, e)) = 
	"(" ^ expToString f ^ " " ^ expToString e ^ ")"
      | expToString (M.Var v) = v

end;  (* structure Print *)
