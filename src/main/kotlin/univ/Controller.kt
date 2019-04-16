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

    fun createStudents(argsStr: String): String {
        val args = argsStr.split(",")
            .map { it.trim() }
        if (argsStr.isEmpty() || args.size % 2 != 0) {
            return "even args expected"
        }

        val toCreate = ArrayList<Student>()
        for (i in 0 until args.size step 2) {
            val group = service.getGroupByNumber(args[i + 1]) ?: return "Group with number ${args[i + 1]} not found"
            toCreate.add(Student(null, args[i], group))

        }
        return service.createStudents(toCreate).toString()
    }

    fun createGroup(argsStr: String): String {
        val args = argsStr.split(",")
            .map { it.trim() }
        if (argsStr.isEmpty() || args.size != 1) {
            return "1 arg expected"
        }

        val toCreate = ActualGroup(null, args[0])
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

    fun getStudents(argsStr: String): String {
        val args = argsStr.split(",")
            .map { it.trim() }
        if (argsStr.isEmpty() || args.size != 2) {
            return "2 arg expected"
        }

        val from = args[0].toInt()
        val size = args[1].toInt()
        return service.getStudents(Page(from, size)).joinToString("\n")
    }

}