import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import WorkModuls.ExecutorOfCommands
import WorkModuls.Task
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.sql.Connection
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.logging.Level
import java.util.logging.Logger

class Server() {

    val logger = Logger.getLogger("logger")
    val executorOfCommands = ExecutorOfCommands()

    init {
        logger.log(Level.INFO, "Старт сервера")
    }


    fun startSever(
        collection: Collection<String, StudyGroup>,
        databaseHandler: DatabaseHandler,
        connection: Connection,
    ) {
        logger.log(Level.INFO, "Ожидание подключения")
        try {
            val serverSocketChannel = ServerSocketChannel.open()
            serverSocketChannel.bind(InetSocketAddress("localhost", 8000))
            val executorService = Executors.newFixedThreadPool(10)
            val forkJoinPool = ForkJoinPool.commonPool()
            while (serverSocketChannel != null) {
                val clientSocketChannel = serverSocketChannel.accept()
                executorService.submit {
                    val task = handlerOfInput(clientSocketChannel)
                    forkJoinPool.submit {
                        Thread {
                            handlerOfOutput(
                                clientSocketChannel, executorOfCommands.reader(
                                    collection,
                                    task.describe,
                                    task,
                                    task.listOfCommands,
                                    databaseHandler,
                                    connection
                                )
                            )
                        }.start()
                    }
                }
            }
            serverSocketChannel?.close()
        } catch (e: RuntimeException) {
            logger.log(Level.SEVERE, "Ошибка подключения")
        }
    }

    fun handlerOfInput(
        clientSocketChannel: SocketChannel,
    ): Task {
        logger.log(Level.INFO, "Получение информации")
        try {
            val objectInputStream = ObjectInputStream(clientSocketChannel.socket().getInputStream())
            return objectInputStream.readObject() as Task
        } catch (e: RuntimeException) {
            logger.log(Level.SEVERE, "Ошибка получения информации")
            throw e
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