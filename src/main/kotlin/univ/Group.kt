package univ

import java.sql.Connection

interface Group {
    val id: Long?
    val number: String
    val studentsCount: Int
}

data class ActualGroup(override val id: Long?, override val number: String, override val studentsCount: Int = 0) : Group

class GroupProxy(val conn: Connection, override val id: Long) : Group {

    override val number: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val studentsCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

}
