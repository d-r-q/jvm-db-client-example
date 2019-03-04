import org.postgresql.Driver
import java.sql.DriverManager

fun main(args: Array<String>) {
    println("Hello")
    DriverManager.registerDriver(Driver())
    val c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "mysecretpassword")
    val rs = c.createStatement().executeQuery("SELECT count(*) cnt FROM test_table")
    val hasNext = rs.next()
    if (hasNext) {
        val cnt = rs.getInt("cnt")
        println("Count: $cnt")
    }
}