package univ

import java.sql.Connection
import javax.sql.DataSource


class UnivDataSource(private val delegate: DataSource) : DataSource by delegate {

   private val conn = ThreadLocal<Connection>()

    fun realGetConnection(): Connection {
        return delegate.connection
    }

    fun setConnection(c: Connection) {
        conn.set(c)
    }

    override fun getConnection(): Connection {
        return conn.get() ?: delegate.connection
    }

    override fun getConnection(username: String?, password: String?): Connection {
        return conn.get() ?: delegate.getConnection(username, password)
    }
}