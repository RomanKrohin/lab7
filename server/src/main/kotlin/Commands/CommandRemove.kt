package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import WorkModuls.Task
import java.lang.RuntimeException
import java.sql.Connection
import java.util.*

/**
 * Класс команды, которая удаляет объект из коллекции по его ключу
 */

class CommandRemove(
    workCollection: Collection<String>,
    workDatabaseHandler: DatabaseHandler,
    workConnection: Connection,
    workTask: Task,
) : Command() {
    var task: Task
    var collection: Collection<String>
    var databaseHandler: DatabaseHandler
    var connection: Connection

    init {
        task = workTask
        collection = workCollection
        databaseHandler = workDatabaseHandler
        connection = workConnection
    }

    /**
     *  Метод работы команды
     *  @param collection
     *  @param key
     */
    override fun commandDo(key: String): Answer {
        val answer = Answer()
        return try {
            collection.remove(key.uppercase(), databaseHandler, connection)
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }

}