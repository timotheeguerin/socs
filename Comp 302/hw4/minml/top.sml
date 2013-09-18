signature TOP =
sig
    (* interactive loop *)
    val loop_print           : unit -> unit
    val loop_print_deBruijn  : unit -> unit
    val loop_print_closed    : unit -> unit
    val loop_print_deBruijn_closed : unit -> unit

    (* read a MinML source file *)
    val file_print          : string -> unit
    val file_print_deBruijn : string -> unit
end

structure Top :> TOP =
struct

    fun loop_print () =
        (Loop.loop Loop.show)
        handle Parse.Error s => print ("Parse error: " ^ s ^ "\n");

    fun loop_print_closed () =
        (Loop.loop Loop.showClosed)
        handle 
	MinML.Error s => print ("Error: " ^ s ^ " occurs free\n")
	| Parse.Error s => print ("Parse error: " ^ s ^ "\n");

    fun loop_print_deBruijn () =
        (Loop.loop Loop.showDeBruijn)
        handle Parse.Error s => print ("Parse error: " ^ s ^ "\n");

    fun loop_print_deBruijn_closed () =
        (Loop.loop Loop.showDeBruijnClosed)
        handle 
	MinML.Error s => print ("Error: " ^ s ^ " occurs free\n")
      | Parse.Error s => print ("Parse error: " ^ s ^ "\n");

    fun file_print f =
        (Loop.loopFile f Loop.show)
        handle Parse.Error s => print ("Parse error: " ^ s ^ "\n");

    fun file_print_deBruijn f =
        (Loop.loopFile f Loop.showDeBruijn)
        handle Parse.Error s => print ("Parse error: " ^ s ^ "\n");


    fun file_print_closed f =
        (Loop.loopFile f Loop.showClosed)
        handle 
	MinML.Error s => print ("Error: " ^ s ^ " occurs free\n")
	| Parse.Error s => print ("Parse error: " ^ s ^ "\n");

    fun file_print_deBruijn_closed f =
        (Loop.loopFile f Loop.showDeBruijnClosed)
        handle 
	MinML.Error s => print ("Error: " ^ s ^ " is not closed\n")
	| Parse.Error s => print ("Parse error: " ^ s ^ "\n");


end
