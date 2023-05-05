package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import java.lang.RuntimeException
import java.sql.Connection
import java.util.*

/**
 * Класс команды, которая удаляет объект из коллекции по его ключу
 */

class CommandRemove(
    workCollection: Collection<String, StudyGroup>,
    workDatabaseHandler: DatabaseHandler,
    workConnection: Connection,
) : Command() {
    var collection: Collection<String, StudyGroup>
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
    override fun commandDo(key: String): Answer {
        val answer = Answer()
        return try {
            collection.collection.get(key.uppercase(Locale.getDefault()))?.let { databaseHandler.doStudyGroupNotSave(it.getId(), connection) }
            collection.remove(key.uppercase(Locale.getDefault()))
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }

}