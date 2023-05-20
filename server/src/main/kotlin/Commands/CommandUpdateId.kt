package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.*
import java.lang.RuntimeException
import java.util.stream.Collectors

/**
 * Класс команды, которая обновляет id объекта коллекции по его ключу
 */
class CommandUpdateId(workCollection: Collection<String>) : Command() {
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
        return try {
            val answer = Answer()
            val components = key.split(" ")
            collection.collection.values.stream().collect(Collectors.toList())
                .filter { it -> it.getId() == components[0].toLong() && it.getOwner() == task.login }
                .forEach { it.setId(components[1].toLong()) }
            answer

        } catch (e: RuntimeException) {
            val answer = Answer()
            answer
        }
    }

}