signature DICT =
  sig
    type dict
    val empty : dict
    val insert : string * dict -> dict
    val find_all : dict -> string list
    val sample_dict : dict

  end
