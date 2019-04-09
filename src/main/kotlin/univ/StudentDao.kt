package univ

import java.sql.Connection
import java.sql.Statement
import javax.sql.DataSource


fun createStudent(conn: Connection, toCreate: Student): Long {
    val stmt = conn.prepareStatement(
        "INSERT INTO students (full_name, student_group) VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )
    stmt.setString(1, toCreate.name)
    stmt.setLong(2, toCreate.group.id!!)
    stmt.executeUpdate()
    val gk = stmt.generatedKeys
    gk.next()

    return gk.getLong(1)
}

