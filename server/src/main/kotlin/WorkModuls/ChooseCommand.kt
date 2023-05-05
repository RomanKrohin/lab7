package WorkModuls

import Collections.Collection
import Commands.*
import StudyGroupInformation.StudyGroup
import java.nio.channels.SocketChannel
import java.sql.Connection
import java.util.logging.Level
import java.util.logging.Logger
import java.util.stream.Collectors

/**
 * Класс управления командами
 * @param collection
 * @param history
 * @param pathsForExecuteScripts
 * @param pathOfFile
 */
class ChooseCommand(
    collection: Collection<String, StudyGroup>,
    history: MutableList<String>,
    task: Task,
    databaseHandler: DatabaseHandler, connection: Connection
) : CreateCommand {
    private var listOfCommand = createCommands(collection, history, task, databaseHandler, connection)
    private val logger = Logger.getLogger("logger")


    /**
     * Метод выборки команд
     * @param collection
     * @param history
     * @param pathsForExecuteScripts
     * @param pathOfFile
     */
    fun chooseCoomand(commandComponent: MutableList<String>, listOfOldCommands: MutableList<String>): Answer {
        logger.log(Level.INFO, "Выборка команды")
        commandComponent[0].lowercase()
        val command = listOfCommand[commandComponent[0]]?.let {
            val answer = it.commandDo(commandComponent[1])
            answer.setNewCommands(
                listOfCommand.keys.stream().collect(Collectors.toList()).filter { !listOfOldCommands.contains(it) })
            return answer
        }
        return Answer(commandComponent[0])
    }

    override fun createCommands(
        collection: Collection<String, StudyGroup>,
        history: MutableList<String>,
        task: Task, databaseHandler: DatabaseHandler, connection: Connection
    ): Map<String, Command> {
        return mapOf<String, Command>(
            "show" to CommandShow(collection),
            "save" to CommandSave(collection, databaseHandler, connection),
            "history" to CommandHistory(collection),
            "help" to CommandHelp(),
            "info" to CommandInfo(collection),
            "clear" to CommandClear(collection),
            "max_by_name" to CommandMaxName(collection),
            "print_field_descending_average_mark" to CommandPrintFieldDescendingAverageMark(collection),
            "remove_greater_key" to CommandDeleteByMaxKey(collection, databaseHandler, connection),
            "remove_lower_key" to CommandDeleteByMinKey(collection, databaseHandler, connection),
            "count_less_than_group_admin" to CommandCountLessThanAdmin(collection),
            "remove" to CommandRemove(collection, databaseHandler, connection),
            "update id" to CommandUpdateId(collection),
            "insert" to CommandInsert(collection, task)
        )
    }

}