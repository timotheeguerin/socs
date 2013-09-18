(* MinML lexer *)
(* Written as a stream transducer *)

signature LEXER =
sig
    datatype token =
        INT
      | BOOL
      | ARROW
      | DARROW
      | TRUE
      | FALSE

      | ELSE
      | END
      | FN
      | FST
      | FUN
      | IF
      | IN
      | LET
      | NAME
      | SND
      | THEN
      | VAL

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
      | DARROW
      | TRUE
      | FALSE

      | ELSE
      | END
      | FN
      | FST
      | FUN
      | IF
      | IN
      | LET
      | NAME
      | SND
      | THEN
      | VAL

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
        
    fun isalpha #"_" = true
      | isalpha #"'" = true
      | isalpha c = Char.isAlphaNum c

    fun token (#",", s) = (COMMA, s)
      | token (#"(", s) = (LPAREN, s)
      | token (#")", s) = (RPAREN, s)
      | token (#";", s) = (SEMICOLON, s)
      | token (#"=", s) =
        (* might be equals or the start of a darrow *)
        (case next s of
             (#">", s) => (DARROW, s)
           | _ => (EQUALS, s))
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
             if Char.isDigit c then
                 (foldfamily 
                  Char.isDigit
                  0
                  (fn (a, b) => b * 10 + (ord a - ord #"0"))
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
    and keywords (VAR "bool") = BOOL
      | keywords (VAR "else") = ELSE
      | keywords (VAR "end") = END
      | keywords (VAR "false") = FALSE
      | keywords (VAR "fn") = FN
      | keywords (VAR "fst") = FST
      | keywords (VAR "fun") = FUN
      | keywords (VAR "if") = IF
      | keywords (VAR "in") = IN
      | keywords (VAR "int") = INT
      | keywords (VAR "let") = LET
      | keywords (VAR "name") = NAME
      | keywords (VAR "snd") = SND
      | keywords (VAR "then") = THEN
      | keywords (VAR "true") = TRUE
      | keywords (VAR "val") = VAL
      | keywords t = t

    and lex (s : char S.stream) : token S.stream =
        S.delay (fn () => lex' (S.force s))

    (* n = nesting depth *)
    and eatComment n S.Nil = raise LexError "unclosed comment"
      | eatComment n (S.Cons (#"*", s)) = 
            (case S.force s of
                S.Cons (#")", s) => if n = 1 then S.force s else eatComment (n - 1) (S.force s)
              | rest => eatComment n rest)
      | eatComment n (S.Cons (#"(", s)) = 
            (case S.force s of
                S.Cons (#"*", s) => eatComment (n + 1) (S.force s)
              | rest => eatComment n rest)
      | eatComment n (S.Cons (ch, s)) = eatComment n (S.force s)

    (* process characters, skipping whitespace and comments *)
    and lex' S.Nil = S.Nil
      | lex' (S.Cons (#" ", s)) = lex' (S.force s)
      | lex' (S.Cons (#"\r", s)) = lex' (S.force s)
      | lex' (S.Cons (#"\t", s)) = lex' (S.force s)
      | lex' (S.Cons (#"\v", s)) = lex' (S.force s)
      | lex' (S.Cons (#"\n", s)) = lex' (S.force s)
      | lex' (S.Cons (stuff as (#"(", s))) =
            (case S.force s of
                S.Cons (#"*", s) =>
                   lex' (eatComment 1 (S.force s))
              | _ => let val (t, s) = token stuff
                     in
                           S.Cons (keywords t, lex s)
                     end)
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
          | DARROW => "DARROW"
          | TRUE => "TRUE"
          | FALSE => "FALSE"

          | ELSE => "ELSE"
          | END => "END"
          | FN => "FN"
          | FST => "FST"
          | FUN => "FUN"
          | IF => "IF"
          | IN => "IN"
          | LET => "LET"
          | NAME => "NAME"
          | SND => "SND"
          | THEN => "THEN"
          | VAL => "VAL"

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

