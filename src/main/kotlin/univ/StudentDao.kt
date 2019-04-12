package univ

import java.sql.Statement
import javax.sql.DataSource


class StudentDao(private val dataSource: DataSource) {

    fun createStudent(toCreate: Student): Long {
        val stmt = dataSource.connection.prepareStatement(
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

    fun findStudent(id: Long): Student? {
        val stmt = dataSource.connection.prepareStatement(
            "SELECT " +
                    "s.id, s.full_name, s.student_group, g.id, g.number, g.students_count " +
                    "FROM students AS s " +
                    "JOIN groups AS g ON s.student_group = g.id " +
                    "WHERE s.id = ?"
        )
        stmt.setLong(1, id)
        val rs = stmt.executeQuery()
        return if (rs.next()) {
            Student(
                rs.getLong("s.id"), rs.getString("s.full_name"),
                Group(rs.getLong("g.id"), rs.getString("g.number"), rs.getInt("g.students_count"))
            )
        } else {
            null
        }
    }

    fun updateStudent(student: Student) {
        val stmt = dataSource.connection.prepareStatement("UPDATE student SET full_name = ?, student_group = ? WHERE id = ?")
        stmt.setString(1, student.name)
        stmt.setLong(2, student.group.id!!)
        stmt.setLong(3, student.id!!)
        stmt.executeUpdate()
    }
}
