package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.Task
import java.lang.RuntimeException

/**
 * Класс команды, который выводит последние 12 введенных команд
 */
class CommandHistory(workCollection: Collection<String>) : Command(){
    var collection: Collection<String>

    init {
        collection = workCollection
    }

    /**
     *  Метод работы команды
     *  @param collection
     *  @param key
     */
    override fun commandDo(key: String, task: Task): Answer {
        val answer= Answer()
        return try {
            answer.result=key
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }


}