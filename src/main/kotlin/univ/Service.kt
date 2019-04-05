package univ


class Service(private val studentDao: StudentDao) {

    fun createStudent(toCreate: Student): Long {
        return studentDao.createStudent(toCreate)
    }

}