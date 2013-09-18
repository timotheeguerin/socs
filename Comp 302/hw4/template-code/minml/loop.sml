signature LOOP =
sig

  structure MinML : MINML 
  structure DeBruijn : MINML

  type action = MinML.exp -> unit

  (* print the expression *)
  val show         : action
  val showClosed   : action

  (* print the expression in DeBruijn form *)
  val showDeBruijn : action
  val showDeBruijnClosed : action

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

  structure MinML : MINML = MinML
  structure DeBruijn : MINML = DeBruijn

  type action = MinML.exp -> unit

  (* A few actions *)

  fun show e =
      (print (MinML.toString e); print ";\n")

  fun showClosed e =
    let 
      val _ = MinML.closed e
    in 
      print (MinML.toString e); print ";\n"
    end 

  fun showDeBruijn e =
      (print (DeBruijn.toString (Convert.toDeBruijn e));
       print ";\n")

  fun showDeBruijnClosed e =
    let 
      val db = Convert.toDeBruijn e 
      val _  = DeBruijn.closed (db)
    in  
      print (DeBruijn.toString db);
      print ";\n"
    end

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
