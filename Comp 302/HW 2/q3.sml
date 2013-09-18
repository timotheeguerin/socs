(* -----------------------------------------------------------------------------*)
(* HOMEWORK 2                                                                   *)
(*                                                                              *)
(* NOTE: This homework has 2 parts: hw2.sml and parser.sml                      *)
(* THIS FILE CONTAINS CODE FOR QUESTION 3                                       *)
(* -----------------------------------------------------------------------------*)

exception Unimplemented

val UNIMPLEMENTED = fn s => raise Unimplemented
(*** Functional Parsers ***)

(* Functional programming is able to express many problems in very
simple and straightforward ways. On of those problems is parsing, in
this exercise we will develop from the ground up a library of parser
combinators.

A very common approach to writing parsers is to use external tools that
generate the code for parsers using a domain specific language to
represent the grammar and generate, as an output, the code for the
parsers. Often the generated code contains big automatas and it is
very cryptic. It is code that is compiled but never read by humans.

Let's try to do it better by using StandardML, let's write our on
parser library!

In SML we can describe the data relevant to our problems by declaring
datatypes that represent programming languages, data structures, many
forms of structured data. Operating on values of these data types is
made simple by pattern matching and all the facilities offered by the
language, but sometimes we need to read data from strings of
text, this is when parsing comes handy. Parsing transforms an input string into 
some internal representation. We will write a library that
will allow us to read strings and translate them to some internal 
representation. 

The first thing we need to do is choose a type for parsers:
*)

type 'a parser = string -> ('a * string) option

(* A parser is a function from string (i.e. the input) to a pair
consisting of the translated part of the parsed input and whatever remains from the input 
string. The type of parser is polymorphic, i.e. it varies in what we translate
 our input string to.

For example if we want to read a number from the string "123 foo" the
variable 'a will be instantiated with the type int, and the parsing
function will return: SOME (123, " foo"). 

One last detail is that parsers may fail. If we are trying to read a
number but the string contains something else, the parser fails. This
fact is represented by the use of the option type.

So now that we know the type, let's get busy and write and some
parsers.


The parser (return v) always succeds with the result value v, without consuming any
of the input string.
*)

fun return v : 'a parser =  fn inp => SOME (v,inp)

(* On the other hand, failure is parser that always fails.*)
val failure : 'a parser = fn inp => NONE


(* If we are going to write non-trivial parsers, we need a way of
inspecting the input string. The first non-trivial parser, is a parser that 
returns the next character in the input, it only fails when the input is empty. 
This parser effectively splits the input string into the first character and the 
rest (if possible as indicated again by the option type) 
*)

 val item : char parser = (fn inp => let val inp' = String.explode inp in 
					 case inp' of 
					     [] => NONE 
					   | x::xs => SOME (x, String.implode xs) 
				     end)


 (* If we want to use this parsers it is very simple, because parsers
 are already functions that convert from strings to our desired data
 type to use them we just apply the parser to the input. *)

fun parse  (p : 'a parser) (inp : string) = p inp

(* for example:

- parse item "first";
val it = SOME (#"f","irst") : (char * string) option

*)


(** Sequencing **)

(* We want to start combining our small little parser to build bigger
ones. Perhaps the simplest way of combining two parsers is to apply
one after the other in sequence, with the output string returned by
the first parser becoming the input string to the second.

*)

infix  3 >>=    fun p >>= f = fn inp  => case parse p inp of
                                             NONE => NONE
			                   | SOME (v, out) => parse (f v) out

(* A typical parser then has the following structure

(p1 >>= (fn v1 =>
 p2 >>= (fn v2 =>
 ...
 pn >>= (fn vn => return (f v1 v2 ... vn)

In this example we apply first p1, then p2, then p3...
*)

(* Example :
   Parser that consumes three characters. It discards the second, and returns the
   first and third as a pair. It can be defined as follows
*)

val p  = item >>= (fn x =>
         item >>= (fn _ =>
         item >>= (fn y =>
         return (x,y) )))


(* For example we can use it like this:

- parse p "abcdef";
val it = [((#"a",#"c"),"def")] : ((char * char) * string) list
*)

(** Choice **)

(* Another way of combining two parsers is to apply the first parser to the *)
(* input string and if this fails to apply the second instead *)

infix  4 +++    fun p +++ q = fn inp =>  case parse p inp of
                                           NONE => parse q inp
                                         | SOME (v,out) => SOME (v, out)

(*
- parse (item +++ return #"d") "abc";
val it = [(#"a","bc")] : (char * string) list

- parse (failure +++ return #"d") "abc";
val it = [(#"d","abc")] : (char * string) list
-
*)

(* From now on, we start to build up our library of combinators, one
simple example could be a function that takes a list of parsers and
tries them one by one until one succeds, if not parser in the list
succeeds then overall parser fails. *)

(* --------------------------------------------------------------------*)
(* Q1 Combinator oneOf                                                 *)
(* --------------------------------------------------------------------*)
(* write the oneOf combinator: *)
val rec oneOf : ('a parser) list -> 'a parser
    = fn [] => failure
        | n::ps => n +++ oneOf(ps)
        

(*
For example we can use it like this;

- parse (oneOf [failure, item]) "foobar";
val it = SOME (#"f","oobar") : (char * string) option

- parse (oneOf [failure, item >>= (fn _ => item)]) "foobar";
val it = SOME (#"o","obar") : (char * string) option

- parse (oneOf [failure, item >>= (fn _ => item)]) "f";
val it = NONE : (char * string) option
*)
(* --------------------------------------------------------------------*)

(** derived primitives **)

(* Using the item parser we can build many interesting parsers. In
fact most of our parser will depend on item.  One example of that is
sat, which is a parser that takes a predicate on characters (i.e. a
function from char to bool) and uses item to get the first character
of the string and then it succeeds only when the provided function
returns true. *)

val sat : (char -> bool) -> char parser 
    = fn p => item >>= (fn v1 =>
              if p v1 then return v1 else failure)

(* Using sat and appropriate predicates we can write many useful parsers.
digit: a parser that only accepts 0123456789
lower: that accepts lowercase characters
upper: that accepts uppercase characters
letter: that only accepts characters that are letters
alphanum: accepts numbers and letters
ch: that only accepts an specific character provided as a parameter
*)

val digit : char parser
    = sat Char.isDigit

val lower : char parser
    = sat Char.isLower
val upper : char parser
    = sat Char.isUpper
val letter : char parser
    = sat Char.isAlpha
val alphanum : char parser
    = letter +++ digit
val ch : char -> char parser
    = fn c => sat (fn c' => c' = c)

(* --------------------------------------------------------------------*)
(* Q3.2 Combinator st                                                  *)
(* --------------------------------------------------------------------*)
(* Write a combinator that takes a string and returns a parser
that only accepts the input if it starts with that string *)

val rec st : string -> string parser
    = fn "" => return ""
    | s => item >>= (fn v => if v = hd (String.explode (s)) then 
        st (String.implode (tl (String.explode s))) >>= (fn v2 => return (String.implode (hd (String.explode s) :: (String.explode v2)))) else failure) 

(* 
- parse (st "foo") "foobar";
val it = SOME ("foo","bar") : (string * string) option
- parse (st "bar") "foobar";
val it = NONE : (string * string) option
 *)
(* --------------------------------------------------------------------*)
(* Another interesting way of combining parsers is by repeatedly
applying a parser, the following two parsers apply as many times as
possible the parser passed as a parameter. 

many: applies zero or more times the parser while
many1: requires the parser to at least succeed once.
*)

val rec many : 'a parser -> ('a list) parser
    = fn p => (many1 p) +++ (return [])

and many1 : 'a parser -> ('a list) parser
    = fn p => p >>= (fn v1 =>
	      (many p) >>= (fn v2 =>
	      return (v1 :: v2)))

(** Examples **)

(* A parser that accepts white space *)
val whitespace = many (oneOf [ch #" ", ch #"\t", ch #"\n", ch #"\r"])

(* A parser that accepts natural numbers *)
val nat = many1 digit >>= (fn v1 =>
          let val SOME n = Int.fromString (String.implode v1) in
	      return n end)

(* optional tries to apply a parser but it does not fail if the
applied parser fails.
 *)

val optional : 'a -> 'a parser -> 'a parser
    = fn def => fn p => p +++ (return def)

(* Using optional and nat we can write a parser for the string
representation of integers, that is numbers that may be preceded by
the negative sign (-), this parser must return the int value of the
read value *)

val ints : int parser = optional 1 (ch #"-" >>= (fn _ => return ~1)) >>= (fn v1 => 
                     nat >>= (fn v2 => return (v1 * v2)))

(*
- parse ints "123";
val it = SOME (123,"") : (int * string) option
- parse ints "-123";
val it = SOME (~123,"") : (int * string) option
- parse ints "foo";
val it = NONE : (int * string) option
*)
		
(* --------------------------------------------------------------------*)
(* Parsing CSV (Comma separated values) *)

(* Now we will try more realistic examples of problems we might need
to solve with parsing. CSV files are a common format for exchanging
tabular data. In CSV files, each record is stored in one line, and
each line may contain many fields separated by commas (,). In this
example we will use fields with integers and fields with strings,
where strings are always inside double quotes.

One example csv file could be:

"Product", "Price", "Quantity"
"Big Foo", 12, 100
"Small Foo", 1000, 1000000

We want to write a parser for this, first let's declare the sml types
for this problem:
 *)


datatype field =
	 Number of int
       | String of string

type record = field list

type csvfile = record list

(* So a csvfile is list of records, which in turn are a list of
fields. Fields can be a Number or a String, it should not be that
difficult to parse these files. Let's do it.
*)

(* --------------------------------------------------------------------*)
(* Q3.4-a Combinator between                                           *)
(* --------------------------------------------------------------------*)
(* Write the between combinator. It takes three parameters, the
delimiter before (called b), the delimiter after (called a), and the
parser for the value between the delimiters (called e). *)

fun between b a e = b >>= (fn sb => e >>= (fn s => a >>= (fn sa => (return s)))) 


(* Using between it is easy to define a combinator for quoted parsers *)
fun quoted p = between (ch #"\"") (ch #"\"") p

(* 
- parse (quoted (many digit)) "\"123\"";
val it = SOME ([#"1",#"2",#"3"],"") : (char list * string) option
- parse (quoted (many digit)) "123";
val it = NONE : (char list * string) option
- parse (quoted (many digit)) "\"123\"foo";
val it = SOME ([#"1",#"2",#"3"],"foo") : (char list * string) option
 *)

(* --------------------------------------------------------------------*)
(* Q3.4-b Combinator for parsing CSV                                   *)
(* --------------------------------------------------------------------*)
(* Write the following combinators:

qstring : accepts a string that does not contain the double quote character (").

sepBy : a parser that takes a parser that takes two parameters the
        first one matches the separator, and the second one matches the
        content. It is important that the separator is only present between
        two elements of the content.

        For example:

        - parse (sepBy (ch #"|") ints) "123|456|789";
        val it = SOME ([123,456,789],"") : (int list * string) option
        - parse (sepBy (ch #"|") ints) "|123|456|789";
        val it = SOME ([],"|123|456|789") : (int list * string) option
        - parse (sepBy (ch #"|") ints) "123|456|789|";
        val it = SOME ([123,456,789],"|") : (int list * string) option

csvline : finally using the previously defined combinators write a
          parser for records, that is lines in the csv file, this parser 
          combinator will be used by the provided csvfile to implement the
          complete parser.


 *)
val qstring : string parser = many (sat (fn c => c <> #"\"")) >>= (fn s => return (String.implode s))

val field : field parser = (quoted qstring >>= (fn s => return (String s))) +++
			   (ints >>= (fn n => return (Number n)))

val newline : char parser = oneOf [ch #"\n", ch #"\r"]

val sepBy : 'b parser -> 'a parser -> ('a list) parser
    = fn s => fn p =>  many ( (many p) >>= (fn [] => failure
        | [s1] => optional s1 (s >>= (fn s2 => return s1))))



val csvline : record parser = (sepBy(ch #",") field ) >>= (fn s => ((st "\n") +++ (st "\r"))   >>= (fn s2 => return s))

val csvfile : csvfile parser = many csvline

(* This could be the content of a csv file *)
val csvEx = "\"John Smith\",72,1782,\"Colon, Cristobal\"\n198,2,3,4,5,64434,\"two\"\n";

(*
This would be the result of parsing the example csv file:

- parse csvfile csvEx;
val it =
  SOME
    ([[String "John Smith",Number 72,Number 1782,String "Colon, Cristobal"],
      [Number 198,Number 2,Number 3,Number 4,Number 5,Number 64434,
       String "two"]],"") : (csvfile * string) option
*)

