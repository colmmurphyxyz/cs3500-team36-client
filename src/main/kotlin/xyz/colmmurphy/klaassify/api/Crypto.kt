package xyz.colmmurphy.klaassify.api

import java.security.MessageDigest
import java.util.*

/**
 * @param input String to be hashed
 * @return byte array of the SHA256 digest
 */
fun sha256(input: String): ByteArray {
    val bytes = input.toByteArray(Charsets.UTF_8)
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(bytes)
}

/**
 * Converts a byte array to a hexadecimal string
 * @param bytes byte array
 * @return Hexadecimal representation of the byte array, in String format
 */
private fun bytesToHex(bytes: ByteArray): String {
    val hexChars = "0123456789ABCDEF".toCharArray()
    val result = StringBuilder(bytes.size * 2)

    for (byte in bytes) {
        val value = byte.toInt() and 0xFF
        result.append(hexChars[value ushr 4])
        result.append(hexChars[value and 0x0F])
    }

    return result.toString()
}

/**
 * Encode a given byte array in base 64
 * @param input data to be encoded
 * @param urlSafe flag to determine if the encoded text must be url safe i.e contains no characters that have
 * special meaning in URLs (=, /, +,)
 * @return base64 encoded string
 */
fun base64Encode(input: ByteArray, urlSafe: Boolean): String {
    val encodedBytes = Base64.getEncoder().encode(input)
    val encodedString = String(encodedBytes, Charsets.UTF_8)
    if (!urlSafe) return String(encodedBytes, Charsets.UTF_8)
    return encodedString
        .replace("=", "")
        .replace("+", "-")
        .replace("/", "_")
}

/**
 * Decodes a base64 encoded text
 * @param input base64 encoded text
 * @param urlSafe has the encoded text been sanitied in a way that ensures it is safe to use in a URL
 * i.e contains no =, / or + characters
 */
fun base64Decode(input: String, urlSafe: Boolean): String {
    val parsedInput = if (!urlSafe) {
        input
    } else {
        input
            .replace("=", "")
            .replace("+", "-")
            .replace("/", "_")
    }
    val decodedBytes = Base64.getDecoder().decode(parsedInput.toByteArray(Charsets.UTF_8))
    return String(decodedBytes, Charsets.UTF_8)
}
