structure Queue :> QUEUE = 
struct

  datatype 'a qlist = Nil | Cons of 'a * 'a qcell
  withtype 'a qcell = ('a qlist) ref

  exception NotImplemented

(*
 queue = (front, back), where front points to the first qcell         
                        and back points to the last qcell.            
                                                                      
 We assume and maintain that qlists are not circular and back 
 points to a qcell whose content is Nil.                                                                     
 Rep Invariants:  The qlist is not circular;                          

*)
                 
  type 'a queue = ('a qcell) ref * ('a qcell) ref


(* create a new queue (5 points) *)
  fun create () = ( ref (ref Nil),   ref (ref Nil) )


(* add an element to the end of the queue (10 points) *)
fun add (x, (front, back)) = back := ref (Cons(x, ref Nil))

(* return the top element of the queue (5 points) *)
fun top ( ref (ref Nil), _) = NONE
    | top (ref( ref (Cons(a,t))), back) = SOME(a)

(* pop off the top element of the queue (5 points) *)
fun pop ( ref (ref Nil), _) = ()
    | pop (front as ref (ref (Cons(a, t))), back) = front :=  t

fun queueToList (front, back) = 
  let
    fun qtl (ref Nil) = []
      | qtl (ref (Cons (a,t))) = a:: qtl(t)
  in 
    qtl (!front)
  end


end