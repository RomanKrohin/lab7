package WorkModuls

import Collections.Collection
import Commands.WorkWithHistory
import StudyGroupInformation.StudyGroup
import java.sql.Connection
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Класс для чтения, выборки и вывода результатов команд
 */
class ExecuterOfCommands : WorkWithHistory {

    private val history = listOf<String>().toMutableList()
    private val logger = Logger.getLogger("logger")


    /**
     * Класс для чтения, выборки и вывода результатов команд
     * @param collection
     * @param path
     */
    fun reader(
        collection: Collection<String, StudyGroup>,
        command: MutableList<String>,
        task: Task,
        listOfOldCommand: MutableList<String>, databaseHandler: DatabaseHandler, connection: Connection,
    ): Answer {
        logger.log(Level.INFO, "Чтение команды")
        val tokens = Tokenizator()
        val chooseCommand = ChooseCommand(collection, history, task, databaseHandler, connection)
        workWithArrayHistory(command)
        val commandComponents = tokens.tokenizateCommand(command, history)
        val answer = chooseCommand.chooseCoomand(commandComponents, listOfOldCommand)
        logger.log(Level.INFO, "Перенаправка ответа")
        return answer
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