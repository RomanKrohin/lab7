package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import java.lang.RuntimeException
import java.util.stream.Collectors

/**
 * Класс команды, которая выводит в порядке убывания значение поля average mark всех объектов
 */
class CommandPrintFieldDescendingAverageMark(workCollection: Collections.Collection<String, StudyGroup>) : Command(){
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
            answer.result=(collection.collection.values.stream().map { it -> it.getAverageMark() }
                .collect(Collectors.toList()).sorted().reversed().toString())
            return answer
        } catch (e: RuntimeException) {
            answer.result="Command exception"
            return answer
        }
    }


}