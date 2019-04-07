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
    val groupDao = GroupDao(db.dataSource)
    val controller = Controller(Service(StudentDao(db.dataSource), groupDao))
    generateSequence { print("> "); readLine() }
        .takeWhile { it != "exit" }
        .map {
            try {
                if (it.startsWith("create")) {
                    if (it.contains("student")) {
                        controller.createStudent(it.substring("create student".length).trim())
                    } else if (it.contains("group")) {
                        "create group"
                    } else {
                        "Unknown command"
                    }
                } else if (it.startsWith("move")) {
                    "Move"
                } else {
                    "Unknown command: $it"
                }
            } catch (e: Exception) {
                println("Error has been occured: $e")
            }
        }.forEach { println(it) }
}