package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.encodeToString
import java.lang.RuntimeException

/**
 * Класс команды, которая выводит объект, значение поля name которого наибольшее
 */
class CommandMaxName(workCollection: Collections.Collection<String, StudyGroup>) : Command() {
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
            val studyGroup = collection.collection.values.maxBy { it.getName() }
            answer.result=(Yaml.default.encodeToString(studyGroup))
            answer
        } catch (e: RuntimeException) {
            answer.result="Command exception"
            answer
        }
    }

}