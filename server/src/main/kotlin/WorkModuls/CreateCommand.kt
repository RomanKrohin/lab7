package WorkModuls

import Commands.Command
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
        databaseHandler: DatabaseHandler, connection: Connection,
        tokenManager: TokenManager
    ): Map<String, Command>
}