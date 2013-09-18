structure List_Stream_Lib :> LIST_STREAM_LIB = 
  struct
   

 (* Suspended computation *)
datatype 'a stream' = Susp of unit -> 'a stream

(* Lazy stream construction *)
and 'a stream = Empty | Cons of 'a * 'a stream'

exception notImplemented

(* Lazy stream construction and exposure *)
fun delay (d) = Susp(d)
fun force (Susp(d)) = d ()

(* Eager stream construction *)
val empty = Susp (fn () => Empty)
fun cons (x, s) = Susp (fn () => Cons (x, s))


(* smap: ('a -> 'b) -> 'a stream' -> 'b stream' *)
fun smap f s = 
 let
   fun mapStr (s) = mapStr'(force s)     
   and mapStr'(Cons(x, s)) = delay(fn () => Cons((f x), mapStr(s)))
     | mapStr' Empty = empty
 in
   mapStr(s)
 end

(* Appending two streams 

 append  : 'a stream' * 'a stream' -> 'a stream'
 append' : 'a stream * 'a stream' -> 'a stream'

*)
fun append (s,s') = append'(force s, s')
and append' (Empty, yq) = yq
 | append' (Cons(x,xf),yq) = 
  delay(fn () => Cons(x, append(xf, yq)));

(* Inspect a stream up to n elements 
   take : int -> 'a stream' susp -> 'a list
   take': int -> 'a stream' -> 'a list
*)
fun take 0 s = []
  | take n (s) = take' n (force s)
and take' 0 s = []
  | take' n (Cons(x,xs)) = x::(take (n-1) xs)

(* -------------------------------------------------------------*)
(* Q 4.1 Interleave (10 points)                                 *)
(* -------------------------------------------------------------*)
(* interleave: 'a -> 'a list -> 'a list stream' *)

fun interleave x [] = cons([x], empty)
  | interleave x (h::t) = cons((x::h::t), (smap (fn l => h::l) (interleave x t)))

(* -------------------------------------------------------------*)
(* Q 4.2 Flatten a stream (10 points)                           *)
(* -------------------------------------------------------------*)

(* flattenStream: ('a stream') stream' -> 'a stream'
   flattenStream': 'a stream' stream -> 'a stream'

*)

fun flattenStream s' = flattenStream' (force s')
and flattenStream' Empty = empty
  | flattenStream' (Cons(x, y)) = append(x, flattenStream y)


(* -------------------------------------------------------------*)
(* Q 4.3 Permutations (10 points)                               *)
(* -------------------------------------------------------------*)
(* permute: 'a list -> 'a list stream' *)
fun permute [] = empty
  | permute (h::t) = flattenStream(smap (fn l => interleave h l) (permute t))

end