package WorkModuls

import StudyGroupInformation.StudyGroup
import java.io.Serializable

data class Task(
    val describe: MutableList<String>,
    var studyGroup: StudyGroup? = null,
    val listOfCommands: MutableList<String>? = null,
    val authorization: Boolean= false
) : Serializable {
}