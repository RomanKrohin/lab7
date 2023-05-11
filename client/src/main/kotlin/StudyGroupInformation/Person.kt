package StudyGroupInformation

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Person(
    private val name: String,
    private val weight: Int,
    private val color: Color,
    private val country: Country,
) : Serializable {
    fun getName(): String {
        return name
    }

    fun getWeight(): Int {
        return weight
    }

    fun getColor(): Color {
        return color
    }

    fun getCountry(): Country {
        return country
    }
}