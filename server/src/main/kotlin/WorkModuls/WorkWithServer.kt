package WorkModuls

import Collections.Collection

/**
 * Класс начала выборки команды
 */
interface WorkWithServer {

    /**
     * Метод инициализации выборки команды
     */
    fun serverWork(collection: Collection<String>, path: String)
}