signature TEST = 
sig

val isprefix : string -> string -> bool

end;

structure Test = 
struct

(* -------------------------------------------------------------*)
(* TEST CASE                                                    *)
(* -------------------------------------------------------------*)

structure D = Dict

fun ins_words [] = D.empty
  | ins_words (s::xs) = 
  D.insert (s, ins_words xs)

val t1 = ins_words [ "beer", "bread", "beard", "bee", "beef", "beefy", "bree"]
val t2 = ins_words ["bread", "beard", "bee", "beef", "beefy", "bree"]

fun isprefix p s = 
  let 
    fun prefix [] s = true
      | prefix p [] = false
      | prefix (h::t) (x::xs) = 
      if x = h then prefix t xs
	else false
  in 
    prefix (String.explode p) (String.explode s) 
  end 
end;