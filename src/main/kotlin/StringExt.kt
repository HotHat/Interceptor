
import java.util.*
import javax.crypto.spec.SecretKeySpec

import javax.crypto.Mac
import javax.xml.bind.DatatypeConverter
import kotlin.experimental.and


fun String.hmacSha256(secret: String): String {

    val sha256 = Mac.getInstance("HmacSHA256")
    val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
    sha256.init(secretKey)
    val thisByte = this.toByteArray()
    val byte = sha256.doFinal(thisByte)
    return DatatypeConverter.printHexBinary(byte).toLowerCase() // 重点
}

fun bytesToHex(bytes: ByteArray): String {
    val hexArray = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8',
        '9', 'a', 'b', 'c', 'd', 'e', 'f'
    )
    val hexChars = CharArray(bytes.size * 2)
    var v: Int

    for (j in bytes.indices) {
        println(bytes[j].toUInt())
        v = bytes[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v ushr 4]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}