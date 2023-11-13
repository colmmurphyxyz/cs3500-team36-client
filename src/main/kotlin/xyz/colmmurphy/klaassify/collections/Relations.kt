package xyz.colmmurphy.klaassify.collections

val commonGenre: (Artist, Artist) -> Set<String> = { artist1, artist2 ->
    artist1.genres intersect artist2.genres
}
