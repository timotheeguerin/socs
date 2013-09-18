signature LOCK = 
  sig
    type key
    type lock

    val create_lock : string -> (key * lock)
      (* creates a lock and the key that fits it, based on a password *)

    val is_locked : lock -> bool
      (* returns true if the lock is locked, false if not *)

    val toggle_lock: (lock * key) -> lock
      (* If the key matches the lock, a new lock will be returned with
       its status changed from locked to unlocked or vice versa.   
       If not, the lock will be returned unchanged. *)

  end

functor Lock (structure Key : KEY
	      val comes_locked : bool) :> LOCK 
              where type key = Key.key = 
  struct
    type key = Key.key
    type lock = (key * bool)


    exception NotImplemented
    
(* ----------------------------------------------------------------- *)
(* Question 1.1: 5 points *)
(* ----------------------------------------------------------------- *)
   (* Implement the function  create_lock : string -> (key * lock) *)
    fun create_lock str = 
    let
        val key = Key.create_key str;
    in
        (key, (key, comes_locked))
    end
(* ----------------------------------------------------------------- *)   

    fun is_locked (lock_key, status) = status

(* ----------------------------------------------------------------- *)
(* Question 1.2: 5 points *)
(* ----------------------------------------------------------------- *)
    (* Implement the function toggle_lock *)

    fun toggle_lock (lock as (key, locked) , try_key) = 
    if(Key.keys_match(key, try_key)) then
            (key, not(locked))
    else
        lock

end
(* ----------------------------------------------------------------- *)
(* Question 1.3: 5 points *)
(* ----------------------------------------------------------------- *)
(* Create a structure SimpleLock which provides the implementation of 
   a simple lock where the key is simply a string and the lock comes
   already locked, i.e. the status comes_locked is true.

   Add your code here.
*)

structure SimpleLock = Lock(structure Key = StringKey   
                        val comes_locked = true);

(* ----------------------------------------------------------------- *)
(* Question 1.4: 5 points *)
(* ----------------------------------------------------------------- *)
(* Create a structure ComboLock which provides the implementation of 
   a combination lock where the key is a ComboKey and the lock comes 
   already locked, i.e. the status comes_locked is true.

   Add your code here.
*)

structure ComboLock = Lock(structure Key = ComboKey   
                        val comes_locked = true);
