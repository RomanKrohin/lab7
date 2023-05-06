import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.DatabaseHandler
import WorkModuls.ServerDataInformation
import kotlinx.serialization.decodeFromString
import org.yaml.snakeyaml.Yaml
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.sql.Connection
import java.util.logging.Level

/**
 * Точка вхождения в программу
 * @param args
 */
fun main() {
    val collection = Collection<String>()
    val serverDataInformation = readData()
    val databaseHandler =
        DatabaseHandler(serverDataInformation.user, serverDataInformation.password, serverDataInformation.url)
    val connection = databaseHandler.connect()
    collection.collection = databaseHandler.getAllStudyGroup(connection)
    executeStartServer(collection, databaseHandler, connection, serverDataInformation.port)
}

fun readData(): ServerDataInformation {
    val bufferedReader = BufferedReader(FileReader(File("/home/roman/data.yaml")))
    var data = ""
    while (bufferedReader.ready()) {
        data += bufferedReader.readLine() + "\n"
    }
    val list = com.charleskorn.kaml.Yaml.default.decodeFromString<Map<String, ServerDataInformation>>(string = data)
    return list.get("data")!!
}

fun executeStartServer(collection: Collection<String>, databaseHandler: DatabaseHandler, connection: Connection, port: String) {
    val server = Server(port)
    Thread {
        server.startSever(collection, databaseHandler, connection)
    }.start()
}
