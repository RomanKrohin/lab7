import Collections.Collection
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
import java.util.concurrent.LinkedBlockingQueue
import java.util.logging.Level
import java.util.logging.Logger

class Server(workPort: String) {

    val logger = Logger.getLogger("logger")
    val executorOfCommands = ExecutorOfCommands()
    val port: String = workPort
    private val fixJoinPool = Executors.newFixedThreadPool(10)
    private val forkJoinPool = ForkJoinPool.commonPool()
    private val blockingQueueTask = LinkedBlockingQueue<Task>()
    private val blockingQueueAnswer = LinkedBlockingQueue<Answer>()

    init {
        logger.log(Level.INFO, "Старт сервера")
    }


    fun startSever(
        collection: Collection<String>,
        databaseHandler: DatabaseHandler,
        connection: Connection,
    ) {
        logger.log(Level.INFO, "Ожидание подключения")
        try {
            val serverSocketChannel = ServerSocketChannel.open()
            serverSocketChannel.bind(InetSocketAddress("localhost", port.toInt()))
            while (serverSocketChannel != null) {
                val clientSocketChannel = serverSocketChannel.accept()
                fixJoinPool.submit()
                { GetTask(clientSocketChannel) }
                forkJoinPool.submit()
                { processTask(collection, databaseHandler, connection) }
                var sendThread = Thread { ReturnAnswer(clientSocketChannel) }.start()
            }
            serverSocketChannel?.close()
        } catch (e: RuntimeException) {
            logger.log(Level.SEVERE, "Ошибка подключения")
        }
    }

    fun processTask(collection: Collection<String>, databaseHandler: DatabaseHandler, connection: Connection) {
        val task = blockingQueueTask.take()
        blockingQueueAnswer.put(
            executorOfCommands.reader(
                collection,
                task.describe,
                task,
                task.listOfCommands,
                databaseHandler,
                connection
            )
        )
    }

    fun GetTask(
        clientSocketChannel: SocketChannel,
    ) {
        logger.log(Level.INFO, "Получение информации")
        try {
            val objectInputStream = ObjectInputStream(clientSocketChannel.socket().getInputStream())
            blockingQueueTask.put(objectInputStream.readObject() as Task)
        } catch (e: RuntimeException) {
            logger.log(Level.SEVERE, "Ошибка получения информации")
            throw e
        }
    }

    fun ReturnAnswer(clientSocketChannel: SocketChannel) {
        logger.log(Level.INFO, "Передача информации")
        try {
            val objectOutputStream = ObjectOutputStream(clientSocketChannel.socket().getOutputStream())
            objectOutputStream.writeObject(blockingQueueAnswer.take())
        } catch (e: RuntimeException) {
            logger.log(Level.SEVERE, "Ошибка передачи информации")
        }
    }
}