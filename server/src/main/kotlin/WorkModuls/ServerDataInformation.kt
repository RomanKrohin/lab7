package WorkModuls

import kotlinx.serialization.Serializable

@Serializable
data class ServerDataInformation(
    val user: String,
    val password: String,
    val url: String,
    val port: String,
) : java.io.Serializable {

}