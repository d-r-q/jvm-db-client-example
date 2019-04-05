package univ

import java.sql.Statement
import javax.sql.DataSource

class StudentDao(val dataSource: DataSource) {

    fun createStudent(toCreate: Student): Long {
        return dataSource.connection.use {
            val stmt = it.prepareStatement("INSERT INTO students (full_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)
            stmt.setString(1, toCreate.name)
            stmt.executeUpdate()
            val gk = stmt.generatedKeys
            gk.next()
            gk.getLong(1)
        }
    }

}
