structure Test = 
struct

structure L = Lexer;

(* Definition of test cases *)

val TokList1 = [L.INT(9), L.PLUS, L.INT(8), L.TIMES, L.LPAREN, L.INT(4), L.PLUS,
		L.INT(7),L.RPAREN, L.SEMICOLON];
(* ==> 9 + 8 * (4 + 7)   represented as  Plus(Int(9), Times (Int(8),
		Plus(Int(4),Int(7)))) *)

val TokList2 = [L.LPAREN, L.LPAREN, L.INT(9), L.RPAREN, L.PLUS, L.INT(8), L.RPAREN, 
                L.TIMES, L.LPAREN, L.INT(2), L.TIMES, L.INT(4), L.PLUS,
		L.INT(7),L.RPAREN, L.SEMICOLON];
(* ==> 9 + 8 * (4 + 7)   represented as  Plus(Int(9), Times (Int(8), Plus(Int(4),Int(7)))) *)


end