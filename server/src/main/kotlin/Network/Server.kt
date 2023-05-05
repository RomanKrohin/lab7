import Collections.Collection
import Network.ThreadHandler
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import WorkModuls.ExecuterOfCommands
import WorkModuls.Task
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.sql.Connection
import java.util.concurrent.Executors
import java.util.logging.Level
import java.util.logging.Logger

class Server(){

    val logger= Logger.getLogger("logger")

    init {
        logger.log(Level.INFO, "Старт сервера")
    }


    fun startSever(collection: Collection<String, StudyGroup>, databaseHandler: DatabaseHandler, connection: Connection) {
        logger.log(Level.INFO, "Ожидание подключения")
        try {
            val serverSocketChannel = ServerSocketChannel.open()
            serverSocketChannel.bind(InetSocketAddress("localhost", 8000))
            val executorService= Executors.newFixedThreadPool(3)
            while (serverSocketChannel != null) {
                val clientSocketChannel = serverSocketChannel.accept()
                executorService.submit(ThreadHandler(clientSocketChannel, collection, databaseHandler, connection))
            }
            serverSocketChannel?.close()
        } catch (e: RuntimeException) {
            logger.log(Level.SEVERE, "Ошибка подключения")
        }
    }

}