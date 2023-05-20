package WorkModuls

import Collections.Collection
import Commands.*
import StudyGroupInformation.StudyGroup
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
class CommandHandler(
    collection: Collection<String>,
    history: MutableList<String>,
    databaseHandler: DatabaseHandler, connection: Connection,
    tokenManager: TokenManager
) : CreateCommand {
    private var listOfCommand = createCommands(collection, history, databaseHandler, connection, tokenManager)
    private val logger = Logger.getLogger("logger")

    /**
     * Метод выборки команд
     * @param collection
     * @param history
     * @param pathsForExecuteScripts
     * @param pathOfFile
     */
    fun chooseCoomand(commandComponent: MutableList<String>, listOfOldCommands: MutableList<String>, task: Task): Answer {
        logger.log(Level.INFO, "Выборка команды")
        commandComponent[0].lowercase()
        val command = listOfCommand[commandComponent[0]]?.let {
            val answer = it.commandDo(commandComponent[1], task)
            answer.setNewCommands(
                listOfCommand.keys.stream().collect(Collectors.toList()).filter { !listOfOldCommands.contains(it) })
            return answer
        }
        return Answer(commandComponent[0])
    }

    override fun createCommands(
        collection: Collection<String>,
        history: MutableList<String>,
        databaseHandler: DatabaseHandler,
        connection: Connection,
        tokenManager: TokenManager
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
            "insert" to CommandInsert(collection, databaseHandler, connection),
            "registration" to CommandRegistrate(databaseHandler, connection, tokenManager),
            "auto-authentication" to CommandAutoAuthentication(databaseHandler, connection, tokenManager),
            "log_out" to CommandLogOut(databaseHandler, connection, tokenManager)
        )
    }

}