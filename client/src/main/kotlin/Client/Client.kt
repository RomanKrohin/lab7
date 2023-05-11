package Client

import WorkModuls.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel

class Client {

    val listOfNewCommands = mutableListOf<String>()
    var authorization: Boolean = false
    var login = ""
    var password = ""

    fun connection(): SocketChannel {
        return try {
            val clientSocket = SocketChannel.open()
            clientSocket.socket().connect(InetSocketAddress("localhost", 8000))
            clientSocket
        } catch (e: RuntimeException) {
            println("Bad connection")
            SocketChannel.open(InetSocketAddress("localhost", 8000))
            throw e
        }
    }

    fun sendTask(task: Task) {
        try {
            val clientSocket = connection()
            if (clientSocket.isConnected) {
                val objectOutputStream = ObjectOutputStream(clientSocket.socket().getOutputStream())
                if (task.describe[0] == "registration" || task.describe[0] == "auto-authentication") putLoginAndPassword(
                    task
                )
                task.login = login
                task.password = password
                objectOutputStream.writeObject(task)
                getAnswer(clientSocket)

            }
        } catch (e: Exception) {
            println("Bad output")
        }
    }

    fun getAnswer(clientSocket: SocketChannel) {
        val objectInputStream = ObjectInputStream(clientSocket.socket().getInputStream())
        val answer = objectInputStream.readObject() as Answer
        listOfNewCommands.addAll(answer.listOfNewCommand)
        if (answer.result == "Successfully registration" || answer.result == "Successfully auto-authentication") authorization =
            true
        println(answer.result)
        clientSocket.close()
    }

    fun returnNewCommands(): MutableList<String> {
        return listOfNewCommands
    }

    fun clearNewCommands() {
        listOfNewCommands.clear()
    }

    fun putLoginAndPassword(task: Task) {
        val components = task.describe[1].split(" ")
        login = components[0]
        password = components[1]
    }
}