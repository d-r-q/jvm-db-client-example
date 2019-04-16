package univ

import java.lang.IllegalStateException
import java.sql.Connection

interface Group {
    val id: Long?
    val number: String
    val studentsCount: Int
}

data class ActualGroup(override val id: Long?, override val number: String, override val studentsCount: Int = 0) : Group

class GroupProxy(val conn: Connection, override val id: Long) : Group {

    private val actualGroup by lazy {
        val stmt = conn.prepareStatement("SELECT * FROM groups WHERE id = ?")
        stmt.setLong(1, id)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            ActualGroup(id, rs.getString("number"), rs.getInt("students_count"))
        } else {
            throw IllegalStateException("Group with id $id not found")
        }
    }

    override val number: String
        get() = actualGroup.number

    override val studentsCount: Int
        get() = actualGroup.studentsCount

}
