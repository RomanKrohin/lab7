package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.Task
import java.lang.RuntimeException

/**
 * Класс команды, которая выводит информацию о коллекции
 */
class CommandInfo(workCollection: Collection<String>) : Command() {

    var collection: Collection<String>
    var key: String = null.toString()

    init {
        collection = workCollection

    }

    /**
     *  Метод работы команды
     *  @param collection
     *  @param key
     */
    override fun commandDo(key: String, task: Task): Answer {
        val answer = Answer()
        return try {
            answer.result =
                ("Collection: HashTable\n" + "Size " + collection.collection.size + "\n" + java.time.LocalTime.now())
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }


}