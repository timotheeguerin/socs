signature LOOP =
sig

  type action = MinML.exp -> unit

  (* print the expression *)
  val show         : action

  (* ... with its type *)
  val showType     : action

  (* apply an action to a completely evaluated expression *)
  (* val evalBigStep : noDB_action -> noDB_action *)
  val eval : action -> action

 (* wait after executing an action *)
  val wait       : action -> action

  (* run an action on user input expressions, 
     without translating to deBruijn form *) 
  val loop       : action -> unit
  (* ... on a file *)
  val loopFile   : string -> action -> unit

end

structure Loop :> LOOP =
struct

  type action = MinML.exp -> unit

   
 (* fun typing e =
          (case Typing.typeOpt e
             of SOME t => " : " ^ Print.typToString t
              | NONE => " has no type.")
*)

 fun typing e =
   let val t =  Typing.typeOf e in 
     " : " ^ Print.typToString t end
   handle Typing.Error s => (" has no type.\n ERROR: " ^ s)

  (* A few actions *)
  fun show e =
      List.app print [Print.expToString e, ";\n"]

  fun showType e =
      List.app print [Print.expToString e, typing e, "\n"]

  fun eval action e = action (Eval.eval e)

  fun wait action e =
      (action e;
       print "Press return:";
       TextIO.inputLine TextIO.stdIn;
       ())

  (* Running the actions on an interactive loop or a file *)

  fun loop action =
         Stream.app action
         (Parse.parse (Lexer.lex (Input.promptkeybd "MinML> ")))

  fun loopFile name action =
         Stream.app action
         (Parse.parse (Lexer.lex (Input.readfile name)))

end
