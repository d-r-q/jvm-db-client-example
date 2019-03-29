import org.postgresql.Driver
import java.sql.DriverManager
import org.flywaydb.core.Flyway
import java.sql.SQLType
import java.sql.Statement
import java.sql.Types

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
        val stmt = conn.prepareStatement("SELECT count(*) cnt FROM test_table WHERE test_id < ?")
        val id = 10
        stmt.setInt(1, id)
        val resultSet = stmt.executeQuery()

        val hasNext = resultSet.next()
        if (hasNext) {
            val cnt = resultSet.getInt("cnt")
            println("Count: $cnt")
        }

        val calStmt = conn.prepareCall("{ ? = call increment(?) } ")
        calStmt.setInt(2, 2)
        calStmt.registerOutParameter(1, Types.INTEGER)
        calStmt.execute()
        println(calStmt.getInt(1))

        val insStmt = conn.prepareStatement(
            "INSERT INTO test_table (data) VALUES ('Code')",
            Statement.RETURN_GENERATED_KEYS
        )
        insStmt.executeUpdate()
        val generatedKeys = insStmt.generatedKeys
        generatedKeys.next()
        println(generatedKeys.getInt(1))

        val updStmt = conn.prepareStatement("UPDATE test_table SET test_id = test_id + 0")
        val updated = updStmt.executeUpdate()

        println(updated)

        conn.autoCommit = false

        val deleted = conn.prepareStatement("DELETE FROM test_table where test_id < 10").executeUpdate()
        val sp = conn.setSavepoint("delete")
        conn.prepareStatement("DELETE FROM test_table where test_id < 20").executeUpdate()
        println("Deleted: $deleted")
        val rsBefore = conn.prepareStatement("SELECT count(*) FROM test_table").executeQuery()
        rsBefore.next()
        println("Count before rollback: ${rsBefore.getInt(1)}")

        conn.rollback(sp)
        val rsAfter = conn.prepareStatement("SELECT count(*) FROM test_table").executeQuery()
        rsAfter.next()
        println("Count after rollback: ${rsAfter.getInt(1)}")
    }
}















