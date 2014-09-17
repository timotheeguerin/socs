(* Representation of variables *)
signature VAR = 
sig
  type bind
  type var
    
  val inbound : var -> bind list -> bool
  val varToString : var -> string
  val bindToString : bind -> string

end

(* Variables and binders are represented as strings *)
structure Named : VAR = 
struct 
  type bind = string
  type var = string
    
  fun inbound x [] = false 
 |inbound x (h::t) = if x = h then true else inbound x t
  fun varToString x = x
  fun bindToString x = x

end 

(* Variables are represented using integers and binders 
   for pretty printing purposes keep a string *)
structure DB : VAR = 
struct 
  type bind = string
  type var = int
    
  fun inbound x [] = false
  | inbound x l = if x <= List.length l then true else false
  fun bindToString x = x
  fun varToString x = "(DB " ^ Int.toString x ^ ")"

end

