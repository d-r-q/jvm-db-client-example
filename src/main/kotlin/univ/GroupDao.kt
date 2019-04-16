package univ

import java.sql.Statement
import javax.sql.DataSource

class GroupDao(private val dataSource: DataSource) {

    fun getGroupByNumber(groupNumber: String): Group? {
        val stmt = dataSource.connection.prepareStatement("SELECT * FROM groups WHERE number = ?")
        stmt.setString(1, groupNumber)
        val rs = stmt.executeQuery()
        return if (rs.next()) {
            return ActualGroup(rs.getLong("id"), rs.getString("number"), rs.getInt("students_count"))
        } else {
            null
        }
    }

    fun createGroup(group: Group): Long {
        val stmt = dataSource.connection.prepareStatement(
            "INSERT INTO groups (number, students_count) VALUES (?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )
        stmt.setString(1, group.number)
        stmt.setInt(2, group.studentsCount)
        stmt.executeUpdate()
        val gk = stmt.generatedKeys
        gk.next()
        return gk.getLong(1)
    }

    fun addStudents(groupId: Long, newStudents: Int) {
        val stmt = dataSource.connection.prepareStatement(
            "UPDATE groups SET students_count = students_count + ? WHERE id = ?"
        )
        stmt.setInt(1, newStudents)
        stmt.setLong(2, groupId)
        stmt.executeUpdate()
    }
}
