package univ

import java.sql.Statement
import javax.sql.DataSource

class StudentDao(private val dataSource: DataSource) {

    fun createStudent(toCreate: Student): Long {
        return dataSource.connection.use {
            val stmt = it.prepareStatement("INSERT INTO students (full_name, student_group) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
            stmt.setString(1, toCreate.name)
            stmt.setLong(2, toCreate.group.id!!)
            stmt.executeUpdate()
            val gk = stmt.generatedKeys
            gk.next()
            gk.getLong(1)
        }
    }

}
