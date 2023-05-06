package WorkModuls

import StudyGroupInformation.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Hashtable
import java.util.logging.Level
import java.util.logging.Logger

class DatabaseHandler(workUser: String, workPassword: String, workUrl: String) {

    val user = workUser
    val password = workPassword
    val url = workUrl
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

    fun deleteStudyGroup(connection: Connection, id: Long){
        try {
            val preparedStatement= connection.prepareStatement("delete from roman_schema.studyGroups where(roman_schema.studyGroups.issave=false and roman_schema.studyGroups.id=?);")
            preparedStatement.setLong(1, id)
            preparedStatement.execute()
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Exception when delete Study Group")
        }
    }

    fun doStudyGroupNotSave(id: Long, connection: Connection ){
        try {
            val preparedStatement= connection.prepareStatement("update roman_schema.studyGroups set issave=false where(roman_schema.studyGroups.id=${id});")
            preparedStatement.execute()
        } catch (e: SQLException) {
            throw e
        }
    }

    fun registrateUser(login: String, password: String, connection: Connection){
        try{
            val preparedStatement =
                connection.prepareStatement("insert into roman_schema.users (login, password) values(?, ?)")
            preparedStatement.setString(1, login)
            preparedStatement.setString(2, password)
            preparedStatement.execute()
        }
        catch (e: SQLException){
            throw e
        }
    }

    fun checkUser(login: String, password: String, connection: Connection): Boolean{
        try{
            val preparedStatement =
                connection.prepareStatement("select count(*) from roman_schema.users where (login=? and password=?);")
            preparedStatement.setString(1, login)
            preparedStatement.setString(2, password)
            val resultSet=preparedStatement.executeQuery()
            while (resultSet.next()){
                return (resultSet.getInt("count")==1)
            }
        }
        catch (e: SQLException){
            throw e
        }
        return false
    }

    fun saveStudyGroup(studyGroup: StudyGroup, connection: Connection) {
        try {
            val preparedStatement =
                connection.prepareStatement(
                    "insert into roman_schema.studyGroups " +
                            "(name, coordinates_x, coordinates_y, studentscount, shouldbeexpelled, averagemark, formofeducation, adminname, adminweight, admincolor, admincountry, issave, owner, id) " +
                            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, nextval('id_seq'));"
                )
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
            preparedStatement.setBoolean(12, true)
            preparedStatement.setString(13, studyGroup.getOwner())
            val rowAffected = preparedStatement.executeUpdate()
            if (rowAffected == 0) {
                throw SQLException()
            }
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Exception when save Study Group")
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
            var formOfEducation: FormOfEducation? = null
            if (resultSet.getString("formofeducation") != null) formOfEducation = FormOfEducation.valueOf(resultSet.getString("formofeducation"))
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