package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import WorkModuls.Task
import java.lang.RuntimeException
import java.sql.Connection
import java.util.*
import java.util.stream.Collectors

/**
 * Класс команды, которая удаляет объекты, значение ключа которого больше чем у заданного
 */
class CommandDeleteByMaxKey(
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
        try {
            if (collection.collection.keys.contains(key.uppercase(Locale.getDefault()))) {
                collection.collection.keys.stream().collect(Collectors.toList())
                    .filter { it ->
                        it.hashCode() > key.uppercase().hashCode() && collection.collection.get(it)
                            ?.getOwner() == task.login
                    }.forEach {
                        collection.remove(it, databaseHandler)
                    }
            }
            return answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            return answer
        }
    }

}