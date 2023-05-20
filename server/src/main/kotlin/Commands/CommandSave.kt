package Commands

import Collections.Collection
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import WorkModuls.Task
import java.sql.Connection

/**
 * Класс команды, которая очищает файл и пишет, переводит объекты, сохраненные в коллекции, в строчный формат и записывает их в файл
 */
class CommandSave(
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
            collection.collection.values.stream().forEach {
                it.isSave = true
                databaseHandler.doStudyGroupSave(it.getId())
            }
            databaseHandler.deleteNotSaveStudyGroups()
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }


}