package WorkModuls

import Collections.Collection
import Commands.WorkWithHistory
import java.sql.Connection
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Класс для чтения, выборки и вывода результатов команд
 */
class ExecutorOfCommands(collection: Collection<String>, databaseHandler: DatabaseHandler, connection: Connection, workTokenManager: TokenManager) : WorkWithHistory {

    private val history = listOf<String>().toMutableList()
    private val logger = Logger.getLogger("logger")
    val tokens = CommandComponentsManager()
    private val tokenManager= workTokenManager
    val commandHandler = CommandHandler(collection, history, databaseHandler, connection, tokenManager)

    /**
     * Класс для чтения, выборки и вывода результатов команд
     * @param collection
     * @param path
     */
    fun reader(
        command: MutableList<String>,
        task: Task,
        listOfOldCommand: MutableList<String>
    ): Answer {
        logger.log(Level.INFO, "Чтение команды")
        return if (tokenManager.getToken(task.token)!=null  || task.describe[0] == "registration" || task.describe[0] == "auto-authentication") {
            workWithArrayHistory(command)
            val commandComponents = tokens.returnCommandCommand(command, history)
            val answer = commandHandler.chooseCoomand(commandComponents, listOfOldCommand, task)
            logger.log(Level.INFO, "Перенаправка ответа")
            answer
        } else {
            Answer("You need to log in. If you don't have an account, write \"registration\" and follow the instructions or if you have an account, write \"auto-authentication\"")
        }
    }

    override fun workWithArrayHistory(coomand: MutableList<String>) {
        if (history.size > 12) {
            history.removeAt(0)
            history.add(coomand[0])
        } else {
            history.add(coomand[0])
        }
    }

}