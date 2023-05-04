package WorkModuls

import Collections.Collection
import StudyGroupInformation.StudyGroup

/**
 * Класс начала выборки команды
 */
interface WorkWithServer {

    /**
     * Метод инициализации выборки команды
     */
    fun serverWork(collection: Collection<String, StudyGroup>, path: String)
}