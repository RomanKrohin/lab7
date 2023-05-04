package Client

import WorkModuls.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel

class Client {

    val listOfNewCommands= mutableListOf<String>()

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

    fun outputStreamHandler(task: Task) {
        try {
            val clientSocket = connection()
            if (clientSocket.isConnected) {
                val objectOutputStream = ObjectOutputStream(clientSocket.socket().getOutputStream())
                objectOutputStream.writeObject(task)
                inputSteamHandler(clientSocket)
            }
        } catch (e: Exception) {
            println("Bad output")
        }
    }

    fun inputSteamHandler(clientSocket: SocketChannel) {
        val objectInputStream = ObjectInputStream(clientSocket.socket().getInputStream())
        val answer = objectInputStream.readObject() as Answer
        listOfNewCommands.addAll(answer.listOfNewCommand)
        println(answer.result)
        clientSocket.close()
    }

    fun returnNewCommands(): MutableList<String>{
        return listOfNewCommands
    }

    fun resetNewCommands(){
        listOfNewCommands.clear()
    }


}