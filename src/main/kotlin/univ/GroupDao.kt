package univ

import javax.sql.DataSource


class GroupDao(private val dataSource: DataSource) {

    fun getGroupByNumber(groupNumber: String): Group? {
        return dataSource.connection.use {
            val stmt = it.prepareStatement("SELECT * FROM groups WHERE number = ?")
            stmt.setString(1, groupNumber)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return Group(rs.getLong("id"), rs.getString("number"), rs.getInt("students_count"))
            } else {
                null
            }
        }
    }

}
