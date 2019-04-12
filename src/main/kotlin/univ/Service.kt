package univ

import java.lang.Exception
import java.lang.RuntimeException
import javax.sql.DataSource


class Service(val dataSource: DataSource) {

    fun createStudent(toCreate: Student): Long {
        return dataSource.connection.use {
            it.autoCommit = false
            try {
                val studentId = createStudent(it, toCreate)
                if (Math.random() < 0.5) throw RuntimeException("")
                addStudents(it, toCreate.group, 1)
                it.commit()
                studentId
            } catch (e: Exception) {
                it.rollback()
                throw e
            }
        }
    }

    fun getGroupByNumber(groupNumber: String): Group? {
        return dataSource.connection.use {
            getGroupByNumber(it, groupNumber)
        }

    }

    fun createGroup(group: Group): Long {
        return dataSource.connection.use {
            createGroup(it, group)
        }
    }

    fun findStudent(id: Long): Student? {
        return dataSource.connection.use {
            findStudent(it, id)
        }
    }

    fun move(toMove: Student, targetGroup: Group) {
        return dataSource.connection.use {
            it.autoCommit = false
            try {
                val srcGroup = toMove.group
                toMove.group = targetGroup
                updateStudent(it, toMove)
                addStudents(it, srcGroup, -1)
                addStudents(it, targetGroup, 1)
                it.commit()
            } catch (e: Exception) {
                it.rollback()
                throw e
            }
        }
    }

}