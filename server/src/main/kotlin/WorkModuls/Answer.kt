package WorkModuls

import java.io.Serializable

/**
 * Класс ответа, в его экземплярах хранятся результаты выполнения команд
 * @param checkError
 * @param nameError
 * @param result
 */
data class Answer(
    var result: String = "Success\n----------\n",
): Serializable {

    val listOfNewCommand= mutableListOf<String>()

    fun setNewCommands(list: List<String>){
        listOfNewCommand.addAll(list)
    }
}