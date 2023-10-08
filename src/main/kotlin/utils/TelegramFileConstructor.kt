package utils

import com.github.kotlintelegrambot.entities.TelegramFile
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.nio.charset.StandardCharsets
import java.util.*

fun String.urlAsTelegramFile() : TelegramFile {
    val url = URL(this).apply {
        val connection: URLConnection = this.openConnection()
        val httpConn: HttpURLConnection = connection as HttpURLConnection

        httpConn.setRequestProperty("Authorization", "Bearer ${GlobalTokenSaver.token}")
    }
    val imageData = url.readBytes()
    return TelegramFile.ByByteArray(imageData)
}