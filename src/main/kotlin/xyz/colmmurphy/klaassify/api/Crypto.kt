package xyz.colmmurphy.klaassify.api

import java.security.MessageDigest
import java.util.*

fun sha256(input: String): ByteArray {
    val bytes = input.toByteArray(Charsets.UTF_8)
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(bytes)
}

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

fun base64Encode(input: ByteArray, urlSafe: Boolean): String {
    val encodedBytes = Base64.getEncoder().encode(input)
    val encodedString = String(encodedBytes, Charsets.UTF_8)
    if (!urlSafe) return String(encodedBytes, Charsets.UTF_8)
    return encodedString
        .replace("=", "")
        .replace("+", "-")
        .replace("/", "_")
}

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
