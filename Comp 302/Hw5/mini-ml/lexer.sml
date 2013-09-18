(* MinML lexer *)
(* Written as a stream transducer *)

signature LEXER =
sig
    datatype token =
        INT
      | BOOL
      | ARROW
      | TRUE
      | FALSE
      | FST
      | SND
      | FUN
      | FN
      | LET
      | NAME
      | PAIR
      | IN
      | IS
      | END
      | IF
      | THEN
      | ELSE
      | FI
      | COMMA
      | LPAREN
      | RPAREN
      | SEMICOLON
      | EQUALS
      | LESSTHAN
      | TIMES
      | MINUS
      | NEGATE
      | PLUS
      | COLON
      | NUMBER of int
      | VAR of string

    val lex : char Stream.stream -> token Stream.stream

    val tokenToString : token -> string
end;  (* signature LEXER *)


structure Lexer :> LEXER =
struct

    structure S = Stream

    exception LexError of string

    datatype token =
        INT
      | BOOL
      | ARROW
      | TRUE
      | FALSE
      | FST
      | SND
      | FUN
      | FN
      | LET
      | NAME
      | PAIR
      | IN
      | IS
      | END
      | IF
      | THEN
      | ELSE
      | FI
      | COMMA
      | LPAREN
      | RPAREN
      | SEMICOLON
      | EQUALS
      | LESSTHAN
      | NEGATE
      | TIMES
      | MINUS
      | PLUS
      | COLON
      | NUMBER of int
      | VAR of string

    fun next s =
        case S.force s of
            S.Nil => raise LexError "Unexpected end of stream."
          | S.Cons result => result
   
    (* foldfamily 
     : ('a -> bool) -> ('a * 'b -> 'b) -> 'b -> ('b -> 'c) ->
       ('a * 'a stream) -> ('c * 'a stream) 

       This handles a string of characters matching a certain
       function, folding them into a result.
    *)
    fun foldfamily test base combine wrap (c, ins) =
        let
            fun ff b s =
                case next s of
                    (c, ss) =>
                        if test c then
                            ff (combine (c, b)) ss
                        else (wrap b, s)
        in
            ff (combine (c, base)) ins
        end
        
    (* Assumes ASCII-like ordering *)
    fun isnum c = ord c >= ord #"0" andalso ord c <= ord #"9"
    fun isalpha #"_" = true
      | isalpha #"'" = true
      | isalpha c = (ord c >= ord #"a" andalso ord c <= ord #"z")
        orelse      (ord c >= ord #"A" andalso ord c <= ord #"Z")
        orelse      isnum c

    fun token (#",", s) = (COMMA, s)
      | token (#"(", s) = (LPAREN, s)
      | token (#")", s) = (RPAREN, s)
      | token (#";", s) = (SEMICOLON, s)
      | token (#"=", s) = (EQUALS, s)
      | token (#"<", s) = (LESSTHAN, s)
      | token (#"*", s) = (TIMES, s)
      | token (#"~", s) = (NEGATE, s)
      | token (#"-", s) = 
        (* might be minus or the start of an arrow *)
        (case next s of
             (#">", s) => (ARROW, s)
           | _ => (MINUS, s))
      | token (#"+", s) = (PLUS, s)
      | token (#":", s) = (COLON, s)
      | token (c, s) =
             if isnum c then
                 (foldfamily 
                  isnum
                  0
                  (fn (a, b) => (b * 10 + (ord a - ord #"0")))
                  NUMBER
                  (c, s))
             else if isalpha c then
                 (foldfamily
                  isalpha
                  ""
                  (fn (a, b) => b ^ str a)
                  VAR
                  (c, s))
             else raise LexError "illegal character"

    (* some "variables" are actually keywords *)
    and keywords (VAR("int")) = INT
      | keywords (VAR("bool")) = BOOL
      | keywords (VAR("true")) = TRUE
      | keywords (VAR("false")) = FALSE
      | keywords (VAR("fst")) = FST
      | keywords (VAR("snd")) = SND
      | keywords (VAR("if")) = IF
      | keywords (VAR("then")) = THEN
      | keywords (VAR("else")) = ELSE
      | keywords (VAR("fi")) = FI
      | keywords (VAR("fun")) = FUN
      | keywords (VAR("fn")) = FN
      | keywords (VAR("is")) = IS
      | keywords (VAR("let")) = LET
      | keywords (VAR("name")) = NAME
      | keywords (VAR("pair")) = PAIR
      | keywords (VAR("in")) = IN
      | keywords (VAR("end")) = END
      | keywords t = t

    and lex (s : char S.stream) : token S.stream =
        S.delay (fn () => lex' (S.force s))

    (* process characters, skipping whitespace *)
    and lex' S.Nil = S.Nil
      | lex' (S.Cons (#" ", s)) = lex' (S.force s)
      | lex' (S.Cons (#"\r", s)) = lex' (S.force s)
      | lex' (S.Cons (#"\t", s)) = lex' (S.force s)
      | lex' (S.Cons (#"\v", s)) = lex' (S.force s)
      | lex' (S.Cons (#"\n", s)) = lex' (S.force s)
      | lex' (S.Cons r) =
        let
            val (t, s) = token r
        in 
            S.Cons (keywords t, lex s)
        end

    fun tokenToString t = 
        case t of
            INT => "INT"
          | BOOL => "BOOL"
          | ARROW => "ARROW"
          | TRUE => "TRUE"
          | FALSE => "FALSE"
          | FST => "FST"
          | SND => "SND"
          | FUN => "FUN"
          | FN => "FN"
          | IS => "IS"
          | LET => "LET"
          | NAME => "NAME"
          | PAIR => "PAIR"
          | IN => "IN"
          | END => "END"
          | IF => "IF"
          | THEN => "THEN"
          | ELSE => "ELSE"
          | FI => "FI"
          | COMMA => "COMMA"
          | LPAREN => "LPAREN"
          | RPAREN => "RPAREN"
          | SEMICOLON => "SEMICOLON"
          | EQUALS => "EQUALS"
          | LESSTHAN => "LESSTHAN"
          | TIMES => "TIMES"
          | MINUS => "MINUS"
          | PLUS => "PLUS"
          | COLON => "COLON"
          | NEGATE => "NEGATE"
          | NUMBER n => "NUMBER(" ^ Int.toString n ^ ")"
          | VAR v => "VAR(" ^ v ^ ")"

end;  (* structure Lexer *)
