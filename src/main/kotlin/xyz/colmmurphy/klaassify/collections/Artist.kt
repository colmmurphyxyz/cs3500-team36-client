package xyz.colmmurphy.klaassify.collections

data class Artist(
    val name: String,
    val genres: Set<String>,
    val followers: Long,
    val spotifyUrl: String,
    val popularity: Int,
    val images: List<ArtistImage>
)
