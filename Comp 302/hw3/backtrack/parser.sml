structure Parser :> PARSER = 
  struct

(* Implementation of a  parser for a simple context free grammar using 
   exceptions. 
   The grammar parses arithmetic expressions with +,*, and ()'s.  The n
   represents an integer and is a terminal symbol. Top-level start symbol for
   this grammar is E.   

E -> S;
S -> P + S | P   % P = plus sub-expressions
P -> A * P | A   % A = arithmetic expression, 
A -> n | (S)

Expressions are lexed into a list of tokens by the lexer. The parser's job is to
translate this list into an abstract 
syntax tree, given by type Exp also shown below. 

*)

structure L = Lexer

datatype Exp = Sum of Exp * Exp | Prod of Exp * Exp | Int of int

(* Success exceptions *)

(* SumExpr (S, toklist') : 
    Indicates that we successfully parsed a list of tokens called toklist
   into an S-Expression S and a remaining list of tokens called toklist'
   toklist' is what remains from toklist after we peeled of all the tokens
   necessary to build the S-Expression S.
 *)

exception SumExpr of Exp * L.Token list
(* ProdExpr (S, toklist') : 
    Indicates that we successfully parsed a list of tokens called toklist
   into an P-Expression P and a remaining list of tokens called toklist'
   toklist' is what remains from toklist after we peeled of all the tokens
   necessary to build the P-Expression P.
 *)

exception ProdExpr of Exp * L.Token list
(* AtomicExpr (A, toklist') : 
    Indicates that we successfully parsed a list of tokens called toklist
   into an A-Expression A and a remaining list of tokens called toklist'
   toklist' is what remains from toklist after we peeled of all the tokens
   necessary to build the A-Expression A.
 *)

exception AtomicExpr of Exp * L.Token list

(* Indicating failure to parse a given list of tokens *)

 exception Error of string

 exception NotImplemented

(* Example: 
   parse "9 + 8 * 7;"
  ===> Sum(Int 9, Prod (Int 8, Int 7))
*)


fun parseExp [] = raise Error "Expected Expression: Nothing to parse"
  | parseExp toklist = 
    parseSumExp toklist
    handle SumExpr (Exp, [L.SEMICOLON]) => Exp
         | _ => raise Error "Error: Expected Semicolon"


and parseSumExp toklist = parseProdExp toklist
    handle ProdExpr (Exp, L.PLUS::ltoken) => (parseSumExp ltoken
        handle SumExpr (Exp2, ltoken2) => (raise SumExpr (Sum(Exp, Exp2) , ltoken2)))
      |  ProdExpr (Exp, ltoken) => (raise SumExpr (Exp, ltoken))
         

and parseProdExp toklist = parseAtom toklist
    handle AtomicExpr (Exp, L.TIMES::ltoken) => ( parseProdExp ltoken
        handle ProdExpr (Exp2, ltoken2) => (raise ProdExpr (Prod(Exp, Exp2), ltoken2)))
       |  AtomicExpr (Exp, ltoken) => raise ProdExpr (Exp, ltoken)

and parseAtom (L.INT(n)::toklist) = raise AtomicExpr (Int(n), toklist)
    | parseAtom  (L.LPAREN::toklist) = (parseSumExp toklist
            handle SumExpr (Exp, L.RPAREN::ltoken) =>
            raise AtomicExpr (Exp, ltoken))
    | parseAtom toklist = raise Error "Error: Unexpedcted Token"

    



fun parse string  = 
    parseExp (L.lex string)

end


