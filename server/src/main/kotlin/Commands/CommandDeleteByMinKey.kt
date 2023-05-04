package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import java.lang.RuntimeException
import java.util.*
import java.util.stream.Collectors

/**
 * Класс, команды, которая удаляет объекты значение ключа которых меньше чем у заданного
 */
class CommandDeleteByMinKey(workCollection: Collection<String, StudyGroup>) : Command() {
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
        val answer = Answer()
        try {
            if (collection.collection.keys.contains(key.uppercase(Locale.getDefault()))) {
                collection.collection.keys.stream().collect(Collectors.toList())
                    .filter { it -> it.hashCode() < key.uppercase().hashCode() }.forEach(collection.collection::remove)
            }
            return answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            return answer
        }
    }

}