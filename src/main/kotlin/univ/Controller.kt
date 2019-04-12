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

    fun createGroup(argsStr: String): String {
        val args = argsStr.split(",")
            .map { it.trim() }
        if (argsStr.isEmpty() || args.size != 1) {
            return "1 arg expected"
        }

        val toCreate = Group(null, args[0])
        return service.createGroup(toCreate).toString()
    }

    fun moveStudent(argsStr: String): String {
        val args = argsStr.split(",")
            .map { it.trim() }
        if (argsStr.isEmpty() || args.size != 2) {
            return "2 arg expected"
        }

        val id = args[0].toLong()

        val toMove = service.findStudent(id) ?: return "Student with $id does not exist"
        val targetGroup = service.getGroupByNumber(args[1]) ?: return "Group with number ${args[1]} does not exist"
        service.move(toMove, targetGroup)
        return "Moved"
    }

}