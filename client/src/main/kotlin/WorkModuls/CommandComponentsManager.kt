package WorkModuls

import java.util.stream.Collectors


/**
 * Класс для токенизации команд
 */
class CommandComponentsManager {

    /**
     * Метод для токенизации команд
     * @param command
     * @param path
     * @param history
     * @return MutableList<String>
     */
    fun returnCommandCommand(command: String): MutableList<String> {
        val commandComponent1 = command.trim().split(" ").toMutableList()
        val commandComponent2=commandComponent1.stream().filter{it!=""}.collect(Collectors.toList())
        if (commandComponent2.size == 3) {
            commandComponent2[0] = commandComponent2[0] + " " + commandComponent2[1]
            commandComponent2[1] = commandComponent2[2]
            commandComponent2.removeAt(2)
        }
        return commandComponent2

    }


}
