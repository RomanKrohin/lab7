import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import WorkModuls.ExecuterOfCommands
import WorkModuls.Task
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.sql.Connection
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

class Server(){

    val reader = ExecuterOfCommands()
    val logger= Logger.getLogger("logger")

    init {
        logger.log(Level.INFO, "Старт сервера")
    }


    fun startSever(collection: Collection<String, StudyGroup>, databaseHandler: DatabaseHandler, connection: Connection) {
        logger.log(Level.INFO, "Ожидание подключения")
        try {
            val serverSocketChannel = ServerSocketChannel.open()
            serverSocketChannel.bind(InetSocketAddress("localhost", 8000))
            while (serverSocketChannel != null) {
                val clientSocketChannel = serverSocketChannel.accept()
                handlerOfInput(clientSocketChannel, collection, databaseHandler, connection)
            }
            serverSocketChannel?.close()
        } catch (e: RuntimeException) {
            logger.log(Level.SEVERE, "Ошибка подключения")
        }
    }

    fun handlerOfInput(clientSocketChannel: SocketChannel, collection: Collection<String, StudyGroup>,  databaseHandler: DatabaseHandler, connection: Connection) {
        logger.log(Level.INFO, "Получение информации")
        try {
            val objectInputStream = ObjectInputStream(clientSocketChannel.socket().getInputStream())
            val task = objectInputStream.readObject() as Task
            handlerOfOutput(clientSocketChannel, reader.reader(collection, task.describe, task, task.listOfCommands, databaseHandler, connection))
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