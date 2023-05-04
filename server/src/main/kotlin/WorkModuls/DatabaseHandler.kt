package WorkModuls

import StudyGroupInformation.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.Hashtable
import java.util.LinkedList
import java.util.logging.Level
import java.util.logging.Logger

class DatabaseHandler {

    val user = "postgres"
    val password = "MtCI_0609"
    val schema = "roman_schema"
    val url = "jdbc:postgresql://localhost:5432/roman"
    val logger = Logger.getLogger("logger")

    fun connect(): Connection {
        try {
            val connection = DriverManager.getConnection(url, user, password)
            logger.log(Level.INFO, "Successfully connect to database")
            return connection
        } catch (e: SQLException) {
            throw e
        }
    }

    fun saveStudyGroup(studyGroup: StudyGroup, connection: Connection) {
        try {
            val preparedStatement =
                connection.prepareStatement("insert into roman_schema.studyGroups " +
                        "(name, coordinates_x, coordinates_y, studentscount, shouldbeexpelled, averagemark, formofeducation, adminname, adminweight, admincolor, admincountry) " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")
            preparedStatement.setString(1, studyGroup.getName())
            preparedStatement.setFloat(2, studyGroup.getCoordinates().getX().toFloat())
            preparedStatement.setFloat(3, studyGroup.getCoordinates().getY().toFloat())
            preparedStatement.setLong(4, studyGroup.getStudentcount())
            preparedStatement.setInt(5, studyGroup.getShouldBeExpelled())
            preparedStatement.setInt(6, studyGroup.getAverageMark())
            preparedStatement.setString(7, studyGroup.getFormOfEducation().toString())
            preparedStatement.setString(8, studyGroup.getAdmin().getName())
            preparedStatement.setInt(9, studyGroup.getAdmin().getWeight())
            preparedStatement.setString(10, studyGroup.getAdmin().getColor().toString())
            preparedStatement.setString(11, studyGroup.getAdmin().getCountry().toString())
            val rowAffected= preparedStatement.executeUpdate()
            if (rowAffected==0){
                throw SQLException()
            }
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Fail when add studyGroup to database")
        }
    }

    fun getAllStudyGroup(connection: Connection): Hashtable<String, StudyGroup> {
        val checkModule = CheckModule()
        val listOfStudyGroup = Hashtable<String, StudyGroup>()
        val listOfId = mutableListOf<Long>(0)
        val SELECT_ALL_STUDYGROUP = "SELECT * FROM roman_schema.studyGroups;"
        val preparedStatement = connection.prepareStatement(SELECT_ALL_STUDYGROUP)
        val resultSet = preparedStatement.executeQuery()
        while (resultSet.next()) {
            val name = resultSet.getString("name")
            val coordinatesX = resultSet.getFloat("coordinates_x")
            val coordinatesY = resultSet.getFloat("coordinates_y")
            val studentsCount = resultSet.getInt("studentscount")
            val shouldBeExpeled = resultSet.getInt("shouldbeexpelled")
            val averageMark = resultSet.getInt("averagemark")
            var formOfEducation: FormOfEducation? = null
            if (resultSet.getString("formofeducation") != "null") {
                formOfEducation = FormOfEducation.valueOf(resultSet.getString("formofeducation"))

            }
            val adminName = resultSet.getString("adminname")
            val adminWeight = resultSet.getInt("adminweight")
            val adminColor = Color.valueOf(resultSet.getString("admincolor"))
            val adminCountry = Country.valueOf(resultSet.getString("admincountry"))
            val studyGroup = StudyGroup(
                name,
                Coordinates(coordinatesX.toLong(), coordinatesY.toLong()),
                studentsCount.toLong(),
                shouldBeExpeled,
                averageMark,
                formOfEducation,
                Person(adminName, adminWeight, adminColor, adminCountry)
            )
            if (checkModule.check(studyGroup)) {
                listOfStudyGroup.put("-${name}", studyGroup)
            }
        }
        listOfStudyGroup.values.stream().forEach {
            it.setId(listOfId.max()+1)
            listOfId.add(listOfId.max()+1)
        }
        return listOfStudyGroup
    }

}