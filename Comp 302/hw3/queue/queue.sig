signature QUEUE =
  sig
    type 'a queue

    exception NotImplemented

    val create : unit -> 'a queue
    val top : 'a queue -> 'a option
    val pop : 'a queue -> unit
    val add : 'a * 'a queue -> unit
    val queueToList: 'a queue -> 'a list
  end;

