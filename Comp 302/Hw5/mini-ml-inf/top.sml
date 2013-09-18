signature TOP =
sig
    (* interactive loop *)
    val loop_print   : unit -> unit
    val loop_type  : unit -> unit
    val loop_eval    : unit -> unit 

    (* read a MinML source file *)
    val file_print        : string -> unit
    val file_eval         : string -> unit 
    val file_type       : string -> unit
end

structure Top :> TOP =
struct

    fun loop_eval () = 
        (Loop.loop (Loop.eval Loop.show)) 
        handle Parse.Error s => print ("Parse Error: " ^ s ^ "\n")
	     | Eval.Stuck s  => print ("Evaluation Error: " ^ s ^ "\n");

    fun loop_print () =
        (Loop.loop Loop.show)
        handle Parse.Error s => print ("Parse Error: " ^ s ^ "\n")
	     | Typing.Error s => print ("Type Error: " ^ s ^ "\n");

    fun loop_type () = 
        (Loop.loop Loop.showType)
        handle Parse.Error s => print ("Parse Error: " ^ s ^ "\n")
	     | Typing.Error s => print ("Type Error: " ^ s ^ "\n");

    fun file_print f =
        (Loop.loopFile f Loop.show)
        handle Parse.Error s => print ("Parse Error: " ^ s ^ "\n");


    fun file_eval  f =
        (Loop.loopFile f (Loop.eval Loop.show)) 
        handle Parse.Error s => print ("Parse Error: " ^ s ^ "\n")
	     | Eval.Stuck s => print ("Evaluation Error: " ^ s ^ "\n");

    fun file_type  f = 
        (Loop.loopFile f Loop.showType)
        handle Parse.Error s => print ("Parse Error: " ^ s ^ "\n")
	     | Typing.Error s => print ("Type Error: " ^ s ^ "\n");




end
