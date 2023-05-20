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
) : Command() {
    var collection: Collection<String>
    var databaseHandler: DatabaseHandler
    var connection: Connection

    init {
        collection = workCollection
        databaseHandler = workDatabaseHandler
        connection = workConnection
    }

    /**
     *  Метод работы команды
     *  @param collection
     *  @param key
     */
    override fun commandDo(key: String, task: Task): Answer {
        val answer = Answer()
        return try {
            collection.remove(key.uppercase(), databaseHandler)
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }

}