package Commands

import Collections.Collection
import WorkModuls.*
import java.lang.RuntimeException
import java.sql.Connection

/**
 * Класс команды, которая добавляет объект по его ключу
 */
class CommandInsert(
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
            val listOfId = mutableListOf<Long>(0)
            for (i in collection.collection.values) {
                listOfId.add(i.getId())
            }
            task.studyGroup?.let {
                it.setId(listOfId.max() + 1)
                collection.add(it, key, databaseHandler)
            }
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }


}