package Commands

import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import WorkModuls.Task
import WorkModuls.TokenManager
import java.sql.Connection

class CommandLogOut (
    workDatabaseHandler: DatabaseHandler,
    workConnection: Connection,
    workTokenManager: TokenManager,
) : Command() {
    var databaseHandler: DatabaseHandler
    var connection: Connection
    var tokenManager: TokenManager

    init {
        databaseHandler = workDatabaseHandler
        connection = workConnection
        tokenManager = workTokenManager
    }

    override fun commandDo(key: String, task: Task): Answer {
        val answer = Answer()
        return try {
            tokenManager.removeToken(key)
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }
}