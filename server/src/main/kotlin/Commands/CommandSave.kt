package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import java.sql.Connection

/**
 * Класс команды, которая очищает файл и пишет, переводит объекты, сохраненные в коллекции, в строчный формат и записывает их в файл
 */
class CommandSave(workCollection: Collection<String, StudyGroup>, workDatabaseHandler: DatabaseHandler, workConnection: Connection): Command(){
    var collection: Collection<String, StudyGroup>
    var databaseHandler: DatabaseHandler
    var connection: Connection
    init {
        collection=workCollection
        databaseHandler=workDatabaseHandler
        connection= workConnection
    }

    /**
     *  Метод работы команды
     *  @param collection
     *  @param key
     */
    override fun commandDo(key: String): Answer {
        val answer= Answer()
        return try {
            databaseHandler.deleteStudyGroup(connection)
            collection.collection.values.stream().forEach {
                databaseHandler.saveStudyGroup(it, connection)
                it.isSave = true
            }
            answer
        } catch (e: RuntimeException){
            answer.result="Command exception"
            answer
        }
    }


}