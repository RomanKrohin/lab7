package WorkModuls

import Client.Client
import java.security.MessageDigest
import kotlin.system.exitProcess

/**
 * Класс для чтения, выборки и вывода результатов команд
 */
class ReaderOfCommands {

    val listOfCommands =
        mutableListOf("show", "info", "help", "history", "save", "clear", "exit", "auto-authentication", "registration")
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
                    specialActions(i, asker)
                    client.sendTask(i)
                }
            } else {
                if (listOfCommands.contains(commandComponents[0])) {
                    val task =
                        Task(commandComponents, listOfCommands = listOfCommands, authorization = client.authorization)
                    specialActions(task, asker)
                    client.sendTask(task)
                    listOfCommands.addAll(client.returnNewCommands())
                    client.clearNewCommands()
                } else {
                    println("Command ${commandComponents[0]} does not exist")
                }
            }
        }
    }

    fun specialActions(task: Task, asker: Asker) {
        if (task.describe[0] == "exit") {
            exitProcess(0)
        }
        if (task.describe[0] == "update id") {
            println("Enter new ID")
            task.describe.add(asker.askLong().toString())
        }
        if (task.describe[0] == "insert") {
            task.studyGroup = asker.askStudyGroup()
            task.studyGroup?.setOwner(client.login)
        }
        if (task.describe[0] == "registration" || task.describe[0] == "auto-authentication") {
            println("Enter login, after enter password")
            task.describe.add("${asker.askLoginAndPasswordForRegistration()} ${sha384(asker.askLoginAndPasswordForRegistration())}")
        }
    }

    fun sha384(input: String): String {
        val bytes = input.toByteArray()
        val md = MessageDigest.getInstance("SHA-384")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

}