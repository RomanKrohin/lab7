package Commands

import WorkModuls.Answer
import WorkModuls.DatabaseHandler
import WorkModuls.Task
import WorkModuls.TokenManager
import java.security.MessageDigest
import java.sql.Connection

class CommandAutoAuthentication(
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
            val components = key.split(" ")
            val token = sha384(
                components[0].subSequence(0, 9).toString() + components[1].subSequence(0, 9).toString()
            ).subSequence(0, 9).toString()
            if (!databaseHandler.checkUser(components[0], components[1])) {
                answer.result =
                    "Wrong password or login"
            }
            else{
                if (tokenManager.getToken(token)!=null){
                    answer.result="This account already used"
                }
                else {
                    tokenManager.createToken(token)
                    answer.result = "Successfully auto-authentication. Your token is: ${token}"
                }
            }
            answer
        } catch (e: RuntimeException) {
            answer.result = "Command exception"
            answer
        }
    }

    fun sha384(input: String): String {
        val bytes = input.toByteArray()
        val md = MessageDigest.getInstance("SHA-384")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}