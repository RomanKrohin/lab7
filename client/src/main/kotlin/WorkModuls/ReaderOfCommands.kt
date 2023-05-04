package WorkModuls

import Client.Client
import kotlin.system.exitProcess

/**
 * Класс для чтения, выборки и вывода результатов команд
 */
class ReaderOfCommands {

    val listOfCommands = mutableListOf("show", "info", "help", "history", "save", "clear", "exit")
    val asker = Asker()
    val client = Client()

    /**
     * Класс для чтения, выборки и вывода результатов команд
     * @param collection
     * @param path
     */
    fun readCommand() {
        val tokens = Tokenizator()
        val readerOfScripts = ReaderOfScripts()
        while (true) {
            val command = asker.askCommand()
            val commandComponents = tokens.tokenizateCommand(command)
            if (commandComponents[0] == "execute_script") {
                val listOfTasks =
                    readerOfScripts.readScript(commandComponents[1], tokens, mutableListOf())
                for (i in listOfTasks) {
                    addSpecialComponents(i)
                    client.outputStreamHandler(i)
                }
            } else {
                if (listOfCommands.contains(commandComponents[0])) {
                    val task =Task(commandComponents, listOfCommands = listOfCommands)
                    addSpecialComponents(task)
                    client.outputStreamHandler(task)
                    listOfCommands.addAll(client.returnNewCommands())
                    client.resetNewCommands()
                } else {
                    println("Command ${commandComponents[0]} does not exist")
                }
            }
        }
    }

    fun addSpecialComponents(task: Task) {
        if (task.describe[0] == "exit") {
            exitProcess(0)
        }
        if (task.describe[0] == "update id") {
            println("Enter new ID")
            task.describe.add(asker.askLong().toString())
        }
        if (task.describe[0] == "insert") {
            task.studyGroup = asker.askStudyGroup()
        }
    }
}