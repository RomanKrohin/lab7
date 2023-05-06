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
 * Класс, команды, которая удаляет объекты значение ключа которых меньше чем у заданного
 */
class CommandDeleteByMinKey(
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
        try {
            if (collection.collection.keys.contains(key.uppercase(Locale.getDefault()))) {
                collection.collection.keys.stream().collect(Collectors.toList())
                    .filter { it ->
                        it.hashCode() < key.uppercase().hashCode() && collection.collection.get(it)
                            ?.getOwner() == task.login
                    }.forEach {
                        collection.collection.get(it.uppercase(Locale.getDefault()))
                            ?.let { it1 -> databaseHandler.doStudyGroupNotSave(it1.getId(), connection) }
                        collection.collection.remove(key)
                    }
            }
            return answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            return answer
        }
    }

}