package Commands

import Collections.Collection
import StudyGroupInformation.StudyGroup
import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import java.sql.Connection

class CommandRegistrate(workDatabaseHandler: DatabaseHandler, workConnection: Connection) : Command() {
    var databaseHandler: DatabaseHandler
    var connection: Connection

    init {
        databaseHandler = workDatabaseHandler
        connection = workConnection
    }

    override fun commandDo(key: String): Answer {
        val answer = Answer()
        answer.result = "Successfully registration"
        return try {
            val components = key.split(" ")
            databaseHandler.registrateUser(components[0], components[1], connection)
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }
}