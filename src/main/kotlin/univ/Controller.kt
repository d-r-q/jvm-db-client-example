package univ


class Controller(private val service: Service) {

    fun createStudent(argsStr: String): String {
        val args = argsStr.split(",")
            .map { it.trim() }
        if (argsStr.isEmpty() || args.size != 2) {
            return "2 arg expected"
        }

        val group = service.getGroupByNumber(args[1]) ?: return "Group with number ${args[1]} not found"
        val toCreate = Student(null, args[0], group)
        return service.createStudent(toCreate).toString()
    }

/*    fun createGroup(args: String): String {

    }

    fun moveStudent(args: String): String {

    }*/
}