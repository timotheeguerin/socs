(* Convert MinML abstract syntax to DeBruijn abstract syntax *)
signature CONV = 
sig

  val toDeBruijn : MinML.exp -> DeBruijn.exp

end;

structure Convert :> CONV = 
struct

  structure DB = DeBruijn
  structure M  = MinML

  exception Error of string

  exception notImplemented

  fun position []      _ = raise Error "Error : free variable in expressions"
    | position (x::xs) y = 
    if x = y then 1 else 1 + position xs y

  fun toDB_primop (M.Equals)   = DB.Equals
    | toDB_primop (M.LessThan) = DB.LessThan
    | toDB_primop (M.Plus)     = DB.Plus
    | toDB_primop (M.Minus)    = DB.Minus
    | toDB_primop (M.Times)    = DB.Times
    | toDB_primop (M.Negate)   = DB.Negate

  
  (* toDB: name list -> M.exp -> DB.exp  *)
  fun toDB G (M.Int v) = (DB.Int v)
   | toDB G (M.Bool b) = (DB.Bool b)
   | toDB G (M.If(e1, e2, e3)) = DB.If((toDB G e1), (toDB G e2), (toDB G e3))
   | toDB G (M.Pair (e1, e2)) = DB.Pair((toDB G e1), (toDB G e2))
   | toDB G (M.Fst e) = (DB.Fst (toDB G e))
   | toDB G (M.Snd e) = (DB.Snd (toDB G e))
   | toDB G (M.Primop (p, el)) = DB.Primop (toDB_primop(p) , (List.map(fn e => (toDB G e)) el)) 
   | toDB G (M.Fun(x, y, e)) = DB.Fun((x), (y), (toDB G e))
   | toDB G (M.Let(e1, (x,e))) = DB.Let((toDB G e1), ((x), (toDB G e)))
   | toDB G (M.Apply(e1, e2)) = DB.Apply((toDB G e1), (toDB G e2))
   | toDB G (M.Var v) = DB.Var (position G v)
        handle Error _ => (DB.Var (position (G@[v]) v))

fun toDeBruijn e = toDB [] e


end;