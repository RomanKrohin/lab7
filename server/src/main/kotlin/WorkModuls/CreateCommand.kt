package WorkModuls

import Commands.Command
import StudyGroupInformation.StudyGroup
import java.nio.channels.SocketChannel
import java.sql.Connection

/**
 * Интерфейс для создания массива с командами
 */

interface CreateCommand {
    /**
     * Метод для создания массива команд
     * @return Map<String, Command>
     */
    fun createCommands(
        collection: Collections.Collection<String>,
        history: MutableList<String>,
        task: Task, databaseHandler: DatabaseHandler, connection: Connection,
        tokenManager: TokenManager
    ): Map<String, Command>
}