import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.DatabaseHandler
import java.sql.Connection
import java.util.logging.Level

/**
 * Точка вхождения в программу
 * @param args
 */
fun main() {
    val collection= Collection<String, StudyGroup>()
    val databaseHandler= DatabaseHandler()
    val connection= databaseHandler.connect()
    collection.collection= databaseHandler.getAllStudyGroup(connection)
    executeStartServer(collection, databaseHandler, connection)
}

fun executeStartServer(collection: Collection<String, StudyGroup>, databaseHandler: DatabaseHandler, connection: Connection){
    val server= Server()
    Thread{
        server.startSever(collection, databaseHandler, connection)
    }.start()
}
