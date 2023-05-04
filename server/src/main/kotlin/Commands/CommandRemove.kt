package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import java.lang.RuntimeException
import java.util.*

/**
 * Класс команды, которая удаляет объект из коллекции по его ключу
 */

class CommandRemove(workCollection: Collection<String, StudyGroup>) : Command() {
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
        return try {
            collection.remove(key.uppercase(Locale.getDefault()))
            answer
        } catch (e: RuntimeException) {
            answer.result="Command exception"
            answer
        }
    }

}