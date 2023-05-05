package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.Task
import java.lang.RuntimeException
import java.util.stream.Collectors

/**
 * Класс команды очищающая коллекцию
 */
class CommandClear(workCollection: Collection<String, StudyGroup>, workTask: Task) : Command() {
    var task: Task
    var collection: Collection<String, StudyGroup>

    init {
        task = workTask
        collection = workCollection
    }

    /**
     *  Метод работы команды
     *  @param collection
     *  @param key
     */
    override fun commandDo(key: String): Answer {
        val answer = Answer()
        return try {
            collection.collection.keys.stream().collect(Collectors.toList())
                .filter { collection.collection.get(it)?.getOwner() == task.login }
                .forEach(collection.collection::remove)
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }


}