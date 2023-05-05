package Collections

import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Класс содержащий коллекцию и методы управления ею
 *
 * @param сollection коллекция HashTable
 * @constructor Пустой
 * @property Collections
 */
class Collection<String, StudyGroup> {

    var collection = Hashtable<String, StudyGroup>()
    private val lock= ReentrantLock()

    /**
     * Метод для добавления объекта в коллекцию
     * @param studyGroup
     * @param key
     */
    fun add(studyGroup: StudyGroup, key: String) {
        lock.lock()
        try {
            collection[key] = studyGroup
        }
        finally {
            lock.unlock()
        }
    }

    /**
     * Метод удаления объекта из коллекции
     * @param key
     */
    fun remove(key: String) {
        lock.lock()
        try {
            collection.remove(key)
        }
        finally {
            lock.unlock()
        }
    }
    //
}