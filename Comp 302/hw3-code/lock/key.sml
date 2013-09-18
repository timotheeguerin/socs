signature KEY = 
sig 
  type key   (* abstract *)

  val create_key : string -> key
    (* returns a unique key based on a password each time it is called, 
       using references to ensure unique keys *) 

  val keys_match : (key * key) -> bool
    (* determines whether two keys are equal *)
end

structure StringKey :> KEY = 
  struct 

  type key = string

  val key_index = ref 0 

  fun inc_index () = key_index := !key_index +1;
  fun create_key str = (inc_index () ; str ^ Int.toString (!key_index))

(*   fun keys_match k1 k2 = (k1 = k2) *)
    val keys_match = op=

  end


structure ComboKey :> KEY = 
  struct

  type key = int list

  val key_index = ref 0 

  fun inc_index () = key_index := !key_index +1;
  fun create_key str = 
    let
      val clist = explode str
    in 
      List.map (fn x => (inc_index () ; Char.ord(x) +
				    !(key_index))) clist
    end 

  val keys_match = op=

  end

