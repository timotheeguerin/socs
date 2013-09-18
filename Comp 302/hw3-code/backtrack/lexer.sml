signature LEXER = 
sig

  datatype Token = SEMICOLON | PLUS | TIMES | LPAREN | RPAREN | INT of int

  exception Error of string

  val lex : string -> Token list


end;

structure Lexer :> LEXER = 
struct

  datatype Token = SEMICOLON | PLUS | TIMES | LPAREN | RPAREN | INT of int

    exception Error of string

  fun lexChars [] = []
    | lexChars (#";" :: L) = SEMICOLON :: lexChars L
    | lexChars (#"+" :: L) = PLUS :: lexChars L
    | lexChars (#"*" :: L) = TIMES :: lexChars L
    | lexChars (#"(" :: L) = LPAREN :: lexChars L
    | lexChars (#")" :: L) = RPAREN :: lexChars L
    | lexChars  (d :: L) =  
    if Char.isSpace d then lexChars L 
    else 
      let val (n, L') = lexDigit (d :: L) in INT(n)::lexChars L' end

  and lexDigit L = 
    let 
      fun split (d :: rest) = 
	if Char.isDigit d then 
	  let val (digit, rest') = split rest in (d :: digit , rest') end
	else 
	  ([], d :: rest)
	| split [] = ([], [])

      val (digit_list, rest) = split L  
    in 
      case Int.fromString (String.implode digit_list) of 
	  NONE => raise Error (String.implode digit_list)
	| SOME n => (n, rest)
    end 
      
  fun lex s = lexChars (String.explode s)

end;

