signature PARSER = 
sig

  datatype Exp = Sum of Exp * Exp | Prod of Exp * Exp | Int of int

  exception Error of string

  exception NotImplemented

  val parse : string -> Exp

end 