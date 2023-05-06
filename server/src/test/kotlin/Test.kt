import Collections.Collection
import WorkModuls.ChooseCommand
import WorkModuls.DatabaseHandler
import WorkModuls.Task
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IntegrationTest {

    val user = "postgres"
    val password = "MtCI_0609"
    val url = "jdbc:postgresql://localhost:5432/roman"

    @Test
    fun testIntegration() {
        val databaseHandler= DatabaseHandler(user, password, url)
        val task= Task(mutableListOf("show", ""), null, mutableListOf(""), true, "bogdan1234", "bogdan1234")
        val chooseCommand = ChooseCommand(
            Collection(),
            mutableListOf(""),
            task,
            databaseHandler,
            databaseHandler.connect()
        )
        val answer= chooseCommand.chooseCoomand(task.describe, mutableListOf())
        assertEquals("Success\n" +
                "----------\n",answer.result)
    }
}