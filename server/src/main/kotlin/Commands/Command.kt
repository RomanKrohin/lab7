package Commands

import WorkModuls.Answer
import WorkModuls.Task

/**
 * Класс-родитель команд
 */
abstract class Command() {

    /**
     *  Метод работы команды
     *  @param collection
     *  @param key
     */
    abstract fun commandDo(key: String, task: Task): Answer

}