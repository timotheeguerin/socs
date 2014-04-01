(* Parser for MinML *)
(* Generates abstract syntax in named form *)
(* Written as a transducer from token streams to expression streams *)

signature PARSE =
sig
  val parse : Lexer.token Stream.stream -> MinML.exp Stream.stream

  exception Error of string
end;  (* signature PARSE *)

structure Parse :> PARSE =
struct
    
    structure S = Stream
    structure M = MinML
    structure L = Lexer

    exception Error of string

    (* next s = (x,s'), where x is the head of s, s' the tail of s
       raises Error if stream is empty
    *)
    fun next s =
        case S.force s
          of S.Nil => raise Error "Unexpected end of stream"
           | S.Cons result => result

    (* match tok s = s', s' is the tail of s
       raises Error if head of s does not match tok
    *)
    fun match tok s =
        let
	  val (n, s') = next s
        in
	  if tok = n then s'
	  else raise Error ("Expected " ^ Lexer.tokenToString tok ^ " token")
        end

    fun build_primop ((primop, e), e') =
          M.Primop(primop, [e', e])

    (* build_primops: exp -> (primop * exp) list -> exp *)
    fun build_primops exp op_exps =
           foldl build_primop exp op_exps

    (* parse_program r = (e,s')
       where e is the result of parsing the beginning of r
       and s' the unprocessed tail of r
    *)
    fun parse_program r =
        let
            val (e, s) = parse_exp (S.delay (fn () => S.Cons r))
        in
            (e, match L.SEMICOLON s)
        end

    (* parse_factors: Recursively consume adjacent atomic factors (parse_factora),
       forming them into a chain of applications. *)
    and parse_factors s eo =
	case parse_factor_maybe s of
	    SOME (e, s) => (case eo of
				NONE => parse_factors s (SOME e)
			      | SOME f => parse_factors s (SOME (M.Apply(f, e))))
	  | NONE => (case eo of
			 NONE => raise Error "Expected expression"
		       | SOME e => (e, s))

    and parse_factor es = parse_factors es NONE

    (* parse_factora (t,s) attempts to find an atomic expression (no applications) 
       starting with the token t, perhaps continuing through the stream. 
       Returns SOME(e, s) if the exp e was successfully recognized, with s
       the stream remaining after it.
       Returns NONE if the token cannot begin any exp.
       May raise exception Error if the input stream does not represent
       any valid MinML program. *)
   (* parse_factora : L.token * L.token S.stream -> (M.exp * L.token S.stream) option *)

    and parse_factora (L.TRUE, s) = SOME (M.Bool true, s)
      | parse_factora (L.FALSE, s) = SOME (M.Bool false, s)
      | parse_factora (L.NUMBER n, s) = SOME (M.Int n, s)
      | parse_factora (L.VAR v, s) = SOME (M.Var v, s)
      | parse_factora (L.IF, s) =
          let
              val (ec, s) = parse_exp s
              val s = match L.THEN s
              val (et, s) = parse_exp s
              val s = match L.ELSE s
              val (ef, s) = parse_exp s
              val s = match L.FI s
          in
              SOME (M.If(ec, et, ef), s)
          end
      | parse_factora (L.LPAREN, s) =
          let
	    val (e, s) = parse_exp s
	    val (n,s') = next s
          in
	    if n = L.RPAREN then
              SOME (e, s')
	    else
	      if n = L.COMMA then
		let
		  val (e', s'') = parse_exp s'
		in
		  SOME(M.Pair(e,e'), match L.RPAREN s'')
		end
	      else
	        raise Error ("Expected " ^ Lexer.tokenToString L.COMMA ^ " or " ^ Lexer.tokenToString L.RPAREN ^ "token")
          end 
      | parse_factora (L.FST, s) = 
	  let
	    val (e,s') = parse_exp s
	  in 
	    SOME(M.Fst e, s')
	  end
      | parse_factora (L.SND, s) = 
	  let
	    val (e,s') = parse_exp s
	  in 
	    SOME(M.Snd e, s')
	  end
      | parse_factora (L.FUN, s) =
          let
              val (f, s) = parse_var (next s)
              val s = match L.LPAREN s
              val (x, s) = parse_var (next s)
              val s = match L.COLON s
              val (t1, s) = parse_type (next s)
              val s = match L.RPAREN s
              val s = match L.COLON s
              val (t2, s) = parse_type (next s)
              val s = match L.IS s
              val (e, s) = parse_exp s
          in
              SOME (M.Fun(t1, t2, (f, x, e)), match L.END s)
          end

      | parse_factora (L.FN, s) =
          let
              val s = match L.LPAREN s
              val (x, s) = parse_var (next s)
	      val s = match L.COLON s
              val (t1, s) = parse_type (next s)
              val s = match L.RPAREN s
              val s = match L.IS s
              val (e, s) = parse_exp s
          in
              SOME (M.Fn(t1,(x, e)), match L.END s)
          end
      | parse_factora (L.LET, s) =
	let 
	  val (n,s') = next s 
	in
(*          if n = L.NAME  then
          (* call by name *)
          let
              val (x, s) = parse_var (next s')
              val s = match L.EQUALS s
              val (e1, s) = parse_exp s
              val s = match L.IN s
              val (e2, s) = parse_exp s
          in
              SOME (M.Letn(e1, (x,e2)), match L.END s)
          end
         else
*)
	   if n = L.PAIR  then
	   (* let pair *)
	     let
	       val s = match L.LPAREN s'
	       val (x, s) = parse_var (next s)
	       val s = match L.COMMA s
 	       val (y, s) = parse_var (next s)
	       val s = match L.RPAREN s
	       val s = match L.EQUALS s		
	       val (e1,s) = parse_exp s
	       val s = match L.IN s
	       val (e2, s) = parse_exp s
	     in 
	       SOME(M.Letp (e1, (x,y, e2)), match L.END s)
	     end
	   else
          (* call by value *)
          let
              val (x, s) = parse_var (n, s')
              val s = match L.EQUALS s
              val (e1, s) = parse_exp s
              val s = match L.IN s
              val (e2, s) = parse_exp s
          in
              SOME (M.Let(e1, (x,e2)), match L.END s)
          end
	end
      | parse_factora (p, s) =
          case p of
              L.NEGATE =>
		 (case parse_factora (next s) of
		      SOME (operand, s) => SOME (M.Primop (M.Negate, [operand]), s)
		    | NONE => NONE)
            | _ => NONE
         
    (* XXX necessary? *)
    and parse_factor_maybe s = case S.force s of
        S.Nil => NONE
      | S.Cons res => parse_factora res
    
    (* parse_exp_aux : (primop * exp) list -> stream -> (primop * exp) list * stream *)
    and parse_exp_aux acc s =
	let val relop =
	    case next s of
		(L.EQUALS, s) => SOME (M.Equals, s)
	      | (L.LESSTHAN, s) => SOME (M.LessThan, s)
	      | _  => NONE
	in
	    case relop of
		SOME (relop, s) => 
		    let val (e, s) = parse_exp' s
		    in
			parse_exp_aux (acc @ [(relop, e)]) s
		    end
	      | NONE => (acc, s)  (* No more exp-primes; return what we have so far. *)
	end
    
    and parse_exp es =
	let val (e, s) = parse_exp' es
	    val (exp's, s) = parse_exp_aux [] s
        in
	    (build_primops e exp's, s)
        end

    (* parse_exp'_aux : (primop * exp) list -> stream -> (primop * exp) list * stream *)
    and parse_exp'_aux acc s =
	let val addop =
	    case next s of
		(L.PLUS, s) => SOME (M.Plus, s)
	      | (L.MINUS, s) => SOME (M.Minus, s)
	      | _  => NONE
	in
	    case addop of
		SOME (addop, s) => 
		    let val (e, s) = parse_term s
		    in
			parse_exp'_aux (acc @ [(addop, e)]) s
		    end
	      | NONE => (acc, s)  (* No more terms; return what we have so far *)
	end

    and parse_exp' es =
	let val (e, s) = parse_term es
	    val (terms, s) = parse_exp'_aux [] s
        in
	    (build_primops e terms, s)
        end

    (* parse_term_aux : (primop * exp) list -> stream -> (primop * exp) list * stream *)
    and parse_term_aux acc s =
	let val mulop =
	    case next s of
		(L.TIMES, s) => SOME (M.Times, s)
	      | _  => NONE
	in
	    case mulop of
		SOME (mulop, s) => 
		    let val (e, s) = parse_factor s
		    in
			parse_term_aux (acc @ [(mulop, e)]) s
		    end
	      | NONE => (acc, s)  (* No more factors; return what we have so far. *)
	end

    and parse_term es =
	let val (e, s) = parse_factor es
	    val (factors, s) = parse_term_aux [] s
        in
	    (build_primops e factors, s)
        end

    and parse_var (L.VAR v, s) = (v, s)
      | parse_var _ = raise Error "Expected var"

    and parse_basetype (L.INT, s) = (T.INT, s)
      | parse_basetype (L.BOOL, s) = (T.BOOL, s)
(*      | parse_basetype (L.VAR v, s) = (T.VAR v, s) *)
      | parse_basetype (L.LPAREN, s) =
	let
	  val (t, s) = parse_type (next s)
	  val s = match L.RPAREN s
	in
	  (t, s)
	end
      | parse_basetype _ = raise Error "Expected type"

    and parse_type (tok, s) =
        let
	  val (domain, s) = parse_basetype (tok, s)
        in
	  case next s
	    of (L.ARROW, s) =>
	       let 
		 val (range, s') = parse_type (next s)
	       in
		 (T.ARROW(domain, range), s')
	       end
	     | (L.TIMES, s) => 
	       let 
		 val (range, s') = parse_type (next s)
	       in
		 (T.CROSS(domain, range), s')
	       end
	     | _ => (domain, s)
        end

    (* exported parsing function *)
          
    fun parse (s : L.token S.stream) : M.exp S.stream = 
        S.delay (fn () => parse' (S.force s))

    and parse' S.Nil = S.Nil
      | parse' (S.Cons r) = 
        let 
            val (e, s) = parse_program r
        in 
            S.Cons (e, parse s)
        end

end;  (* structure Parse *)
