signature LIST_STREAM_LIB =
  sig

    (* Suspended computation *)
    datatype 'a stream' = Susp of unit -> 'a stream

    (* Lazy stream construction *)
    and 'a stream = Empty | Cons of 'a * 'a stream'

    val delay : (unit -> 'a stream) -> 'a stream'
    val force : 'a stream' -> 'a stream

    val smap  : ('a -> 'b) -> 'a stream' -> 'b stream'
    val append: 'a stream' * 'a stream' -> 'a stream'

    val take  : int -> 'a stream' -> 'a list

    val interleave: 'a -> 'a list -> ('a list) stream'
    val flattenStream : ('a stream') stream' -> 'a stream'

    val permute : 'a list -> ('a list) stream'
      

  end
