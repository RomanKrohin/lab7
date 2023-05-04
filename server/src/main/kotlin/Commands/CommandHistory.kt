package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import java.lang.RuntimeException

/**
 * Класс команды, который выводит последние 12 введенных команд
 */
class CommandHistory(workCollection: Collection<String, StudyGroup>) : Command(){
    var collection: Collection<String, StudyGroup>

    init {
        collection = workCollection
    }

    /**
     *  Метод работы команды
     *  @param collection
     *  @param key
     */
    override fun commandDo(key: String): Answer {
        val answer= Answer()
        try {
            answer.result=key
            return answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            return answer
        }
    }


}