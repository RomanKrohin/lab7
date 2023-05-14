package WorkModuls

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
    fun returnCommandCommand(command: MutableList<String>, history: MutableList<String>): MutableList<String> {
        val commandComponent1 = command
        val commandComponent2: MutableList<String> = listOf<String>().toMutableList()
        for (i in commandComponent1) {
            if (!(i.equals(""))) commandComponent2.add(i)
        }
        if (commandComponent2[0] == "history") {
            commandComponent2.add(history.toString())
        }
        if (commandComponent2.size == 3 && commandComponent2[0] != "update id") {
            commandComponent2[0] = commandComponent2[0] + " " + commandComponent2[1]
            commandComponent2[1] = commandComponent2[2]
            commandComponent2.removeAt(2)
        }
        if (commandComponent2[0].equals("update id") && commandComponent2.size == 3) {
            commandComponent2[1] = commandComponent2[1] + " " + commandComponent2[2]
            commandComponent2.removeAt(2)
        }
        if (commandComponent2.size == 1) {
            commandComponent2.add("")
        }
        return commandComponent2

    }
}
