package Commands

import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import java.sql.Connection

class CommandAutoAuthentication(workDatabaseHandler: DatabaseHandler, workConnection: Connection) : Command() {
    var databaseHandler: DatabaseHandler
    var connection: Connection

    init {
        databaseHandler = workDatabaseHandler
        connection = workConnection
    }

    override fun commandDo(key: String): Answer {
        val answer = Answer()
        answer.result = "Successfully auto-authentication"
        return try {
            val components = key.split(" ")
            if (!databaseHandler.checkUser(components[0], components[1], connection)) answer.result="Wrong password or login"
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }
}