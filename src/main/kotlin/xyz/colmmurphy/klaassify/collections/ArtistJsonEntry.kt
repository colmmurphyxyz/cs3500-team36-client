/***
 * In no way is this the most efficient or most effective way of deserializing data, but it works
 */
package xyz.colmmurphy.klaassify.collections

import kotlinx.serialization.Serializable

@Serializable
data class ImageData(
    val width: Int,
    val url: String,
    val height: Int
)
@Serializable
data class FollowerData(
    val total: Long,
    val href: String?
)
@Serializable
data class ExternalUrlData(
    val spotify: String
)
@Serializable
data class ArtistJsonEntry(
    val images: List<ImageData>,
    val followers: FollowerData,
    val genres: List<String>,
    val popularity: Int,
    val name: String,
    val href: String,
    val id: String,
    val type: String,
    val uri: String,
    val external_urls: ExternalUrlData
) {
    fun toArtist(): Artist {
        return Artist(
            name,
            genres.toSet(),
            followers.total,
            href,
            popularity
        )
    }
}