package StudyGroupInformation

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(private val x: Long, private val y: Long) : java.io.Serializable {
    fun getX(): Long {
        return x
    }

    fun getY(): Long {
        return y
    }
}