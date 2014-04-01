(* Printing expressions *)
signature PRINT =
sig
  type exp

  (* print an expression *)
  val expToString : exp -> string

  (* print a type *)
  val typToString : T.typ -> string
end

signature NAMEDPRINT = PRINT 
    where type exp = MinML.exp

(*--------------------------------------------------*)
structure Print :> NAMEDPRINT =
struct

    structure M = MinML
    type exp = M.exp


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
	 ("a"^ Int.toString (!counter)))

  fun resetCtr () = 
    counter := 0
  end

    fun po M.Equals = "="
      | po M.LessThan = "<"
      | po M.Plus = "+"
      | po M.Minus = "-"
      | po M.Times = "*"
      | po M.Negate = "~" 

    fun member ([], r) = NONE
      | member ((a,r)::L, r') = 
      if r = r' then SOME(a) else member (L, r')

    fun typToString'(L, T.INT) = (L, "int ")
      | typToString' (L, T.BOOL) = (L, "bool ")
      | typToString' (L, T.ARROW(d, r)) = 
	let
	  val (L', s) =  typToString' (L, d) 
	  val (L'', s') = typToString' (L',r)
	in 
	(L'', "(" ^ s ^ ") -> (" ^ s' ^ ")")
	end 

      | typToString' (L, T.CROSS(d, r)) = 
	let
	  val (L', s) =  typToString' (L, d) 
	  val (L'', s') = typToString' (L',r)
	in 

	(L'', "(" ^ s ^ ") * (" ^ s' ^ ")")
	end 
      | typToString' (L, T.VAR (r as ref NONE)) = 
	(case member (L, r) of
	  NONE => let val a = freshVar() in ((a, r)::L, a) end
	| SOME(a) => (L, a))
      | typToString' (L, T.VAR (r as ref (SOME(T)))) = 
	   typToString' (L, T)
	

    and typToString T = let val (_, t) = typToString' ([], T) in (resetCtr () ; t) end

    and expToString (M.Int i) = Int.toString i
      | expToString (M.Bool true) = "true"
      | expToString (M.Bool ftalse) = "false"
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
      | expToString (M.Fun (f, x, e)) = 
	"fun " ^ f ^ "(" ^ x ^  ") " ^
        " is " ^ expToString e ^ " end "
      | expToString (M.Fn (x, e)) = 
	"fn " ^ "(" ^ x ^  ") " ^
	" is " ^ expToString e ^ " end "
      | expToString (M.Let (e1, (x, e2))) = 
	"let " ^ x ^ " = " ^ expToString e1 ^ " in " ^
	expToString e2 ^ " end "
(*      | expToString (M.Letn (e1, (x, e2))) = 
	"let name " ^ x ^ " = " ^ expToString e1 ^ " in " ^
	expToString e2 ^ " end " *)
      | expToString (M.Letp (e1, (x, y, e2))) = 
	"let pair (" ^ x ^ " , " ^ y ^ ") = " ^ expToString e1 ^ " in " ^
	expToString e2 ^ " end "
      | expToString (M.Apply (f, e)) = 
	"(" ^ expToString f ^ " " ^ expToString e ^ ")"
      | expToString (M.Var v) = v

end;  (* structure Print *)
