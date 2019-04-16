package univ

import org.intellij.lang.annotations.Language
import java.sql.Statement
import javax.sql.DataSource

data class Page(val num: Int, val size: Int)

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

    fun createStudents(toCreate: Iterable<Student>): List<Long> {
        val stmt = dataSource.connection.prepareStatement(
            "INSERT INTO students (full_name, student_group) VALUES (?, ?)",
            Statement.RETURN_GENERATED_KEYS

        )
        for (student in toCreate) {
            stmt.setString(1, student.name)
            stmt.setLong(2, student.group.id!!)
            stmt.addBatch()
        }

        stmt.executeBatch()
        val gk = stmt.generatedKeys
        val res = ArrayList<Long>()
        while (gk.next()) {
            res += gk.getLong(1)
        }

        return res
    }

    fun findStudent(id: Long): Student? {
        val stmt = dataSource.connection.prepareStatement(
            "SELECT " +
                    "s.id sid, s.full_name, s.student_group, g.id gid, g.number, g.students_count " +
                    "FROM students AS s " +
                    "JOIN groups AS g ON s.student_group = g.id " +
                    "WHERE s.id = ?"
        )
        stmt.setLong(1, id)
        val rs = stmt.executeQuery()
        return if (rs.next()) {
            Student(
                rs.getLong("sid"), rs.getString("full_name"),
                ActualGroup(rs.getLong("gid"), rs.getString("number"), rs.getInt("students_count"))
            )
        } else {
            null
        }
    }

    fun updateStudent(student: Student) {
        val stmt = dataSource.connection.prepareStatement("UPDATE students SET full_name = ?, student_group = ? WHERE id = ?")
        stmt.setString(1, student.name)
        stmt.setLong(2, student.group.id!!)
        stmt.setLong(3, student.id!!)
        stmt.executeUpdate()
    }

    fun getStudents(page: Page): List<Student> {
        val theQuery = "SELECT id, full_name, student_group FROM students ORDER BY full_name LIMIT ? OFFSET ?"
        val conn = dataSource.connection
        val stmt = conn.prepareStatement(theQuery)
        stmt.setInt(1, page.size)
        stmt.setInt(2, page.size * page.num)

        val res = ArrayList<Student>()
        val rs = stmt.executeQuery()
        while (rs.next()) {
            res.add(Student(
                rs.getLong("id"), rs.getString("full_name"),
                GroupProxy(conn, rs.getLong("student_group"))
            ))
        }
        return res
    }

}
