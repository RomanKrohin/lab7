import Collections.Collection
import WorkModuls.CommandHandler
import WorkModuls.DatabaseHandler
import WorkModuls.ExecutorOfCommands
import WorkModuls.Task
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CommandHandlerTest {

    val user = "postgres"
    val password = "MtCI_0609"
    val url = "jdbc:postgresql://localhost:5432/roman"

    @Test
    fun CommandTest() {
        val databaseHandler= DatabaseHandler(user, password, url)
        val task= Task(mutableListOf("show", ""), null, mutableListOf(""), true, "bogdan1234", "bogdan1234")
        val commandHandler = CommandHandler(
            Collection(),
            mutableListOf(""),
            task,
            databaseHandler,
            databaseHandler.connect()
        )
        val answer= commandHandler.chooseCoomand(task.describe, mutableListOf())
        assertEquals("Success\n" +
                "----------\n",answer.result)
    }


    @Test
    fun RegistrationTestFalse() {
        val databaseHandler= DatabaseHandler(user, password, url)
        val task= Task(mutableListOf("show", ""), null, mutableListOf(""), false, "bogdan1234", "bogdan1234")
        val answer= ExecutorOfCommands().reader(Collection(),
            mutableListOf(""),
            task,
            mutableListOf(""),
            databaseHandler,
            databaseHandler.connect())
        assertEquals("You need to log in. If you don't have an account, write \"registration\" and follow the instructions or if you have an account, write \"auto-authentication\"",answer.result)
    }

    @Test
    fun RegistrationTestTrue() {
        val databaseHandler= DatabaseHandler(user, password, url)
        val task= Task(mutableListOf("show", ""), null, mutableListOf(""), true, "bogdan1234", "bogdan1234")
        val answer= ExecutorOfCommands().reader(Collection(),
            mutableListOf(""),
            task,
            mutableListOf(""),
            databaseHandler,
            databaseHandler.connect())
        assertEquals("Success\n" +
                "----------\n",answer.result)    }
}