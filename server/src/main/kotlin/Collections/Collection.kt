package Collections

import StudyGroupInformation.StudyGroup
import WorkModuls.DatabaseHandler
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Класс содержащий коллекцию и методы управления ею
 *
 * @param сollection коллекция HashTable
 * @constructor Пустой
 * @property Collections
 */
class Collection<String> {

    var collection = Hashtable<String, StudyGroup>()
    private val lock = ReentrantLock()

    /**
     * Метод для добавления объекта в коллекцию
     * @param studyGroup
     * @param key
     */
    fun add(studyGroup: StudyGroup, key: String, databaseHandler: DatabaseHandler) {
        lock.lock()
        try {
            databaseHandler.putStudyGroup(studyGroup, studyGroup.isSave)
            collection[key] = studyGroup
        } finally {
            lock.unlock()
        }
    }

    /**
     * Метод удаления объекта из коллекции
     * @param key
     */
    fun remove(key: String, databaseHandler: DatabaseHandler) {
        lock.lock()
        try {
            collection.get(key)?.let { databaseHandler.doStudyGroupNotSave(it.getId())
            }
            collection.remove(key)
        } finally {
            lock.unlock()
        }
    }
}