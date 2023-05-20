package WorkModuls

import StudyGroupInformation.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class DatabaseHandler(workUser: String, workPassword: String, workUrl: String) {

    val user = workUser
    val password = workPassword
    val url = workUrl
    val logger = Logger.getLogger("logger")
    val connection: Connection = DriverManager.getConnection(url, user, password)
    val SELECT_ALL_STUDYGROUP = connection.prepareStatement("SELECT * FROM roman_schema.studyGroups;")
    val DELETE_NOTSAVE_GROUPS =
        connection.prepareStatement("delete from roman_schema.studyGroups where(roman_schema.studyGroups.issave=false);")
    val DO_STUDYGRIUP_NOTSAVE =
        connection.prepareStatement("update roman_schema.studyGroups set issave=false where(roman_schema.studyGroups.id=?);")
    val DO_STUDYGROUP_SAVE =
        connection.prepareStatement("update roman_schema.studyGroups set issave=true where(roman_schema.studyGroups.id=?);")
    val REGISTRATE_USER = connection.prepareStatement("insert into roman_schema.users (login, password) values(?, ?)")
    val CHECK_USER =
        connection.prepareStatement("select count(*) from roman_schema.users where (login=? and password=?);")
    val INSERT_STUDYGROUP = connection.prepareStatement(
        "insert into roman_schema.studyGroups " +
                "(name, coordinates_x, coordinates_y, studentscount, shouldbeexpelled, averagemark, formofeducation, adminname, adminweight, admincolor, admincountry, issave, owner, id) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
    )

    fun connect(): Connection {
        try {
            if (!connection.isClosed) {
                logger.log(Level.INFO, "Successfully connect to database")
                return connection
            } else {
                throw SQLException()
            }
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Something is wrong with database")
            throw e
        }
    }

    fun deleteNotSaveStudyGroups() {
        try {
            DELETE_NOTSAVE_GROUPS.execute()
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Exception when delete Study Group")
        }
    }

    fun doStudyGroupNotSave(id: Long) {
        try {
            DO_STUDYGRIUP_NOTSAVE.setLong(1, id)
            DO_STUDYGRIUP_NOTSAVE.execute()
        } catch (e: SQLException) {
            throw e
        }
    }

    fun doStudyGroupSave(id: Long) {
        try {
            DO_STUDYGROUP_SAVE.setLong(1, id)
            DO_STUDYGROUP_SAVE.execute()
        } catch (e: SQLException) {
            throw e
        }
    }

    fun registrateUser(login: String, password: String) {
        try {
            REGISTRATE_USER.setString(1, login)
            REGISTRATE_USER.setString(2, password)
            REGISTRATE_USER.execute()
        } catch (e: SQLException) {
            throw e
        }
    }

    fun checkUser(login: String, password: String): Boolean {
        try {
            CHECK_USER.setString(1, login)
            CHECK_USER.setString(2, password)
            val resultSet = CHECK_USER.executeQuery()
            while (resultSet.next()) {
                return (resultSet.getInt("count") == 1)
            }
        } catch (e: SQLException) {
            throw e
        }
        return false
    }

    fun putStudyGroup(studyGroup: StudyGroup, issave: Boolean) {
        try {
            INSERT_STUDYGROUP.setString(1, studyGroup.getName())
            INSERT_STUDYGROUP.setFloat(2, studyGroup.getCoordinates().getX().toFloat())
            INSERT_STUDYGROUP.setFloat(3, studyGroup.getCoordinates().getY().toFloat())
            INSERT_STUDYGROUP.setLong(4, studyGroup.getStudentcount())
            INSERT_STUDYGROUP.setInt(5, studyGroup.getShouldBeExpelled())
            INSERT_STUDYGROUP.setInt(6, studyGroup.getAverageMark())
            INSERT_STUDYGROUP.setString(7, studyGroup.getFormOfEducation().toString())
            INSERT_STUDYGROUP.setString(8, studyGroup.getAdmin().getName())
            INSERT_STUDYGROUP.setInt(9, studyGroup.getAdmin().getWeight())
            INSERT_STUDYGROUP.setString(10, studyGroup.getAdmin().getColor().toString())
            INSERT_STUDYGROUP.setString(11, studyGroup.getAdmin().getCountry().toString())
            INSERT_STUDYGROUP.setBoolean(12, issave)
            INSERT_STUDYGROUP.setString(13, studyGroup.getOwner())
            INSERT_STUDYGROUP.setLong(14, studyGroup.getId())
            INSERT_STUDYGROUP.execute()
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Exception when save Study Group")
        }
    }

    fun getAllStudyGroup(): Hashtable<String, StudyGroup> {
        val checkModule = CheckModule()
        val listOfStudyGroup = Hashtable<String, StudyGroup>()
        val resultSet = SELECT_ALL_STUDYGROUP.executeQuery()
        while (resultSet.next()) {
            val name = resultSet.getString("name")
            var formOfEducation: FormOfEducation? = null
            if (resultSet.getString("formofeducation") != "null") {
                formOfEducation =
                    FormOfEducation.valueOf(resultSet.getString("formofeducation"))
            }
            val studyGroup = StudyGroup(
                resultSet.getLong("id"),
                name,
                Coordinates(resultSet.getFloat("coordinates_x").toLong(), resultSet.getFloat("coordinates_y").toLong()),
                resultSet.getInt("studentscount").toLong(),
                resultSet.getInt("shouldbeexpelled"),
                resultSet.getInt("averagemark"),
                formOfEducation,
                Person(
                    resultSet.getString("adminname"),
                    resultSet.getInt("adminweight"),
                    Color.valueOf(resultSet.getString("admincolor")),
                    Country.valueOf(resultSet.getString("admincountry"))
                ),
                resultSet.getBoolean("issave"),
                resultSet.getString("owner")
            )
            if (checkModule.check(studyGroup)) {
                listOfStudyGroup.put("-${name}", studyGroup)
            }
        }
        return listOfStudyGroup
    }

}