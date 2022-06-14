package databases;

import java.sql.*;

public class JdbsApp {

    private Connection connection;
    private Statement statement;


    void connection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:teachers.db");
        statement = connection.createStatement();
    }

    void disconnect() {
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //      table name:   teachers      column: id	name	surname	email
//      table name:   courses     column: id	 name
//      table name:  streams     column:  id	course_id	number	started_at	students_amount
//      table name:  grades     column:  teacher_id	stream_id	performance
    void createDBTables() throws SQLException {
        try {
            createTeachersTabl();
            createCoursesTabl();
            createStreamsTabl();
            createGradesTabl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //      table name:  grades     column:  teacher_id	stream_id	performance
    private void createGradesTabl() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS grades (" +
                "teacher_id INTEGER NOT NULL," +
                "stream_id INTEGER NOT  NULL," +
                "performance REAL," +
                "FOREIGN KEY (teacher_id) REFERENCES teachers(id)," +
                "FOREIGN KEY (stream_id) REFERENCES streams(id));");
    }

    //      table name:  streams     column:  id	course_id	number	started_at	students_amount
    private void createStreamsTabl() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS streams (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "course_id INTEGER NOT NULL," +
                "number INTEGER NOT NULL UNIQUE," +
                "started_at TEXT," +
                "student_amount INTEGER," +
                "FOREIGN KEY (course_id) REFERENCES  courses(id));");
    }

    //      table name:   courses     column: id	 name
    private void createCoursesTabl() throws SQLException {
        statement.executeUpdate("CREATE TABLE  IF NOT EXISTS courses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE);");
    }

    //     table name:   teachers      column: id	name	surname	email
    private void createTeachersTabl() throws SQLException {
        statement.executeUpdate("CREATE TABLE  IF NOT EXISTS teachers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "surname TEXT NOT NULL," +
                "email TEXT );");
    }

    String getTablesNames() {
        StringBuilder sb = new StringBuilder();
        sb.append("DB tables:");
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, "%", null);
            while (rs.next()) {
                sb.append(String.format("  %s", rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(sb);
        return sb.toString();
    }


    //     table name:   teachers      column: id	name	surname	email
    void insertIntoTeachers(String name, String surname, String email) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO teachers (name, surname, email)" +
                " VALUES (?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, surname);
        ps.setString(3, email);
        ps.executeUpdate();
    }

    void insertIntoTeachers(String name, String surname) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO teachers (name, surname)" +
                " VALUES (?, ?)");
        ps.setString(1, name);
        ps.setString(2, surname);
        ps.executeUpdate();
    }

    //         table name:   teachers      column: id	name	surname	email
    void selectTeachersAll() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("Select id, name, surname, email FROM teachers");
        ResultSet rs = ps.executeQuery();
        System.out.println(String.format("%-5s%-10s%-10s%-10s", "id", "name", "surname", "email"));
        while (rs.next()) {
            String res = String.format("%-5d%-10s%-10s%-10s", rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4));
            System.out.println(res);
        }
    }

    //atr - атрибут   value - значение
    void deleteFromTeacher(String atr, String value) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM teachers WHERE ? = ?;");
        ps.setString(1, atr);
        ps.setString(2, value);
        ps.executeUpdate();
    }


}


