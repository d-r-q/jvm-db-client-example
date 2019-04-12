package univ


fun main() {
    // create group "16202"
    // > 14
    // create group "16203"
    // > 15
    // create student "Фёдор", 15
    // > 1
    // move student 1 "16203" "16202"
    // > ok
    // exit
    val db = Db()
    val univDs = UnivDataSource(db.dataSource)
    val studentsDao = StudentDao(univDs)
    val groupDao = GroupDao(univDs)
    val controller = Controller(Service(univDs, studentsDao, groupDao))
    generateSequence { print("> "); readLine() }
        .takeWhile { it != "exit" }
        .map {
            try {
                if (it.startsWith("create")) {
                    if (it.contains("student")) {
                        controller.createStudent(it.substring("create student".length).trim())
                    } else if (it.contains("group")) {
                        controller.createGroup(it.substring("create group".length).trim())
                    } else {
                        "Unknown command"
                    }
                } else if (it.startsWith("move")) {
                    controller.moveStudent(it.substring("move".length).trim())
                } else {
                    "Unknown command: $it"
                }
            } catch (e: Exception) {
                println("Error has been occured: $e")
            }
        }.forEach { println(it) }
}