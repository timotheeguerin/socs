fun app_tl ([], [], acc) = acc
    | app_tl ([], n::other, acc) = app_tl([], other, acc@[n])
    | app_tl (n::l, other, acc) = app_tl(l, other, acc@[n])
    
fun app_cont([], other, cont) = cont other 
    | app_cont(n::l, other, cont) = app_cont(l, other, (fn x => cont(n::x)))
    
fun rev[] = []
    | rev(h::t) = (rev t)@[h]
    
fun rev([], acc) = acc
    | rev(h::t, acc) = rev(t, h::acc)

type complex = int * int    

fun congugate(a, b):complex = (a, ~b):complex

fun add ((a1, b1):complex) ((a2, b2):complex)= (a1 + a2, b1 +b2):complex

fun multiply ((a, b):complex) ((c, d):complex)= (a*c - b*d, b*c +a*d):complex

fun isValid([]) = false
    | isValid([[]]) = false
    | isValid(matrix) = List.all(fn s => List.lenght(hd(matrix))) (List.map(fn x => List.lenght x) matrix)