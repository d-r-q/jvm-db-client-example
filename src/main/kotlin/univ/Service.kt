package univ


class Service(private val studentDao: StudentDao, private val groupDao: GroupDao) {

    fun createStudent(toCreate: Student): Long {
        return studentDao.createStudent(toCreate)
    }

    fun getGroupByNumber(groupNumber: String): Group? {
        return groupDao.getGroupByNumer(groupNumber)
    }

}