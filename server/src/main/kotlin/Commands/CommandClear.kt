package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import java.lang.RuntimeException
import java.util.stream.Collectors

/**
 * Класс команды очищающая коллекцию
 */
class CommandClear(workCollection: Collection<String, StudyGroup>) : Command() {
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
            collection.collection.keys.stream().collect(Collectors.toList()).forEach(collection.collection::remove)
            answer
        } catch (e: RuntimeException) {
            answer.result="Command exception"
            answer
        }
    }


}