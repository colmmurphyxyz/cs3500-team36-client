package xyz.colmmurphy.klaassify.collections

/**
 * Function that returns the number of genres two given artists share
 * used to programmatically construct edges in a Graph
 */
val commonGenre: (Artist, Artist) -> Int = { artist1, artist2 ->
    (artist1.genres intersect artist2.genres).size
}

val twoCommonGenres: (Artist, Artist) -> Int = { artist1, artist2 ->
    val numGenres = (artist1.genres intersect artist2.genres).size
    if (numGenres < 2) 0 else numGenres
}
