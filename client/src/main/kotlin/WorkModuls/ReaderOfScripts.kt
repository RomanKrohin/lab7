package WorkModuls

import java.io.BufferedReader
import java.io.FileReader

class ReaderOfScripts {
    val listOfCommands = mutableListOf<MutableList<String>>()
    fun readScript(
        path: String,
        tokenizator: CommandComponentsManager,
        historyOfPaths: MutableList<String>,
    ): MutableList<MutableList<String>> {
        try {
            if (!historyOfPaths.contains(path)) {
                historyOfPaths.add(path)
                val bufferedReader = BufferedReader(FileReader(path))
                while (true) {
                    val components = tokenizator.returnCommandCommand(bufferedReader.readLine())
                    if (components[0] == "execute_script") {
                        val extensionListOfCommands = readScript(components[1], tokenizator, historyOfPaths)
                        listOfCommands.addAll(extensionListOfCommands)
                    } else {
                        listOfCommands.add(components)
                    }
                }
            } else {
                println("Данный файл был использован ${path}")
                return mutableListOf()
            }
        } catch (e: Exception) {
            return listOfCommands
        }
    }


}