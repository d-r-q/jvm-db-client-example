package univ


class Service(
    private val dataSource: UnivDataSource,
    private val studentDao: StudentDao,
    private val groupDao: GroupDao
) {

    fun createStudent(toCreate: Student): Long {
        return transaction(dataSource) {
            val studentId = studentDao.createStudent(toCreate)
            if (Math.random() < 0.5) throw RuntimeException("")
            groupDao.addStudents(toCreate.group, 1)
            studentId
        }
    }

    fun getGroupByNumber(groupNumber: String): Group? {
        return transaction(dataSource) {
            groupDao.getGroupByNumber(groupNumber)
        }

    }

    fun createGroup(group: Group): Long {
        return transaction(dataSource) {
            groupDao.createGroup(group)
        }
    }

    fun findStudent(id: Long): Student? {
        return transaction(dataSource) {
            studentDao.findStudent(id)
        }
    }

    fun move(toMove: Student, targetGroup: Group) {
        return transaction(dataSource) {
            val srcGroup = toMove.group
            toMove.group = targetGroup
            studentDao.updateStudent(toMove)
            groupDao.addStudents(srcGroup, -1)
            groupDao.addStudents(targetGroup, 1)
        }
    }

}

fun <T> transaction(ds: UnivDataSource, body: () -> T): T {
    ds.realGetConnection().use {
        it.autoCommit = false
        ds.connection = it
        try {
            val res = body()
            it.commit()
            return res
        } catch (e: Exception) {
            it.rollback()
            throw e
        }
    }
}