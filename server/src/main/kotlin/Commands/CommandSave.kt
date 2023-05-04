package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.encodeToString
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.Writer
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
            collection.collection.values.stream().forEach {
                databaseHandler.saveStudyGroup(it, connection)

            }
            answer
        } catch (e: Exception){
            answer.result="Command exception"
            answer
        }
    }


}