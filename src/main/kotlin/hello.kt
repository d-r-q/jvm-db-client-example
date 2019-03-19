import org.postgresql.Driver
import java.sql.DriverManager
import org.flywaydb.core.Flyway

fun main(args: Array<String>) {
    DriverManager.registerDriver(Driver())
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "mysecretpassword"
    )

    val flyway = Flyway
        .configure()
        .dataSource("jdbc:postgresql://localhost:5432/postgres", "postgres",
            "mysecretpassword")
        .load()

    // Start the migration
    flyway.migrate()

    connection.use { conn ->
        val stmt = conn.prepareStatement("SELECT count(*) cnt FROM test_table WHERE test_id < ?")
        val id = 10
        stmt.setInt(1, id)
        val resultSet = stmt.executeQuery()

        val hasNext = resultSet.next()
        if (hasNext) {
            val cnt = resultSet.getInt("cnt")
            println("Count: $cnt")
        }
    }
}