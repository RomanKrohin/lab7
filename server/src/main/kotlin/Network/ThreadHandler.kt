package Network

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import WorkModuls.ExecuterOfCommands
import WorkModuls.Task
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.channels.SocketChannel
import java.sql.Connection
import java.util.logging.Level
import java.util.logging.Logger

class ThreadHandler(workClientSocketChannel: SocketChannel, workCollection: Collection<String, StudyGroup>, workDatabaseHandler: DatabaseHandler, workConnection: Connection) : Runnable {

    val clientSocketChannel: SocketChannel
    val collection: Collection<String, StudyGroup>
    val databaseHandler: DatabaseHandler
    val connection: Connection

    init {
        clientSocketChannel=workClientSocketChannel
        collection=workCollection
        databaseHandler=workDatabaseHandler
        connection=workConnection
    }


    val logger= Logger.getLogger("logger")
    val executorOfCommands = ExecuterOfCommands()
    override fun run() {
        handlerOfInput(clientSocketChannel, collection, databaseHandler, connection)
    }

    fun handlerOfInput(clientSocketChannel: SocketChannel, collection: Collection<String, StudyGroup>,  databaseHandler: DatabaseHandler, connection: Connection) {
        logger.log(Level.INFO, "Получение информации")
        try {
            val objectInputStream = ObjectInputStream(clientSocketChannel.socket().getInputStream())
            val task = objectInputStream.readObject() as Task
            handlerOfOutput(clientSocketChannel, executorOfCommands.reader(collection, task.describe, task, task.listOfCommands, databaseHandler, connection))
        } catch (e: RuntimeException) {
            logger.log(Level.SEVERE, "Ошибка получения информации")
        }
    }

    fun handlerOfOutput(clientSocketChannel: SocketChannel, answer: Answer) {
        logger.log(Level.INFO, "Передача информации")
        try {
            val objectOutputStream = ObjectOutputStream(clientSocketChannel.socket().getOutputStream())
            objectOutputStream.writeObject(answer)
        } catch (e: RuntimeException) {
            logger.log(Level.SEVERE, "Ошибка передачи информации")
        }
    }

}