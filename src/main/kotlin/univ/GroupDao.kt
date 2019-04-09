package univ

import java.sql.Connection
import java.sql.Statement


fun getGroupByNumber(conn: Connection, groupNumber: String): Group? {
    val stmt = conn.prepareStatement("SELECT * FROM groups WHERE number = ?")
    stmt.setString(1, groupNumber)
    val rs = stmt.executeQuery()
    return if (rs.next()) {
        return Group(rs.getLong("id"), rs.getString("number"), rs.getInt("students_count"))
    } else {
        null
    }
}

fun createGroup(conn: Connection, group: Group): Long {
    val stmt = conn.prepareStatement(
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

fun addStudents(conn: Connection, group: Group, newStudents: Int) {
    val stmt = conn.prepareStatement(
        "UPDATE groups SET students_count = students_count + ? WHERE id = ?"
    )
    stmt.setInt(1, newStudents)
    stmt.setLong(2, group.id!!)
    stmt.executeUpdate()
}
