import org.postgresql.Driver
import org.flywaydb.core.Flyway
import java.sql.*

fun main(args: Array<String>) {
    DriverManager.registerDriver(Driver())
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "mysecretpassword"
    )

    val flyway = Flyway
        .configure()
        .dataSource(
            "jdbc:postgresql://localhost:5432/postgres", "postgres",
            "mysecretpassword"
        )
        .load()

    // Start the migration
    flyway.migrate()

    connection.use { conn ->
        println(selectPage(conn, Page(10, 3)))

        var from: Pair<String, Int>? = null
        for (i in 0..10){
            val res = selectPage2(conn, Page2(from, 6))
            if (res.isEmpty()) {
                break
            }
            print("Page $i: ")
            println(res)
            from = res.last()
        }
    }

}

fun selectPage(con: Connection, page: Page): List<Int> {
    val query = "SELECT test_id FROM test_table ORDER BY test_id LIMIT ? OFFSET ?"
    val limit = page.size
    val offset = page.size * page.num
    val stmt = con.prepareStatement(query)
    stmt.setInt(1, limit)
    stmt.setInt(2, offset)
    val rs = stmt.executeQuery()
    val res = ArrayList<Int>()
    while (rs.next()) {
        res.add(rs.getInt(1))
    }
    return res
}

fun selectPage2(con: Connection, page: Page2): ArrayList<Pair<String, Int>> {
    var query = "select data, test_id from test_table "
    if (page.from != null) {
        query += "where (data, test_id) > (?, ?)"
    }
    query += "order by data, test_id limit ?"

    val stmt = con.prepareStatement(query)
    if (page.from != null) {
        stmt.setString(1, page.from.first)
        stmt.setInt(2, page.from.second)
        stmt.setInt(3, page.size)
    } else {
        stmt.setInt(1, page.size)
    }

    val rs = stmt.executeQuery()
    val res = ArrayList<Pair<String, Int>>()
    while (rs.next()) {
        res.add(rs.getString(1) to rs.getInt(2))
    }
    return res

}

data class Page(val num: Int, val size: Int)
data class Page2(val from: Pair<String, Int>?, val size: Int)












