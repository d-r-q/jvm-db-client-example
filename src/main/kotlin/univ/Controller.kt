package univ


class Controller(private val service: Service) {

    fun createStudent(argsStr: String): String {
        val args = argsStr.split(",")
        if (argsStr.length == 0 || args.size != 1) {
            return "1 arg expected"
        }

        val toCreate = Student(null, args[0])
        return service.createStudent(toCreate).toString()
    }

/*    fun createGroup(args: String): String {

    }

    fun moveStudent(args: String): String {

    }*/
}