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

data class Page(val num: Int, val size: Int)













