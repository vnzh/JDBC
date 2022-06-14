package databases;

import java.sql.*;
import java.util.ArrayList;

public class JdbsApp2 {

    private static Connection con;
    private static Statement stmt;

    public static void main(String[] args) {

        try {
            connection();
            readEx();
            ArrayList<String> tables = showTables();
            System.out.println(tables);
            showTablesMetaData();
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();

        } finally {
            disConnection();
        }


    }

    private static void connection() throws SQLException {
        con = DriverManager.getConnection("jdbc:sqlite:demobase.db");
        stmt = con.createStatement();
    }

    private static void disConnection() {
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void readEx() throws SQLException {

        ResultSet rs = stmt.executeQuery("SELECT * FROM students;");
        columnNames(rs);
        while (rs.next()) {
//            System.out.printf(" %d  %s  %d\n", rs.getInt(1),
//                    rs.getString("name"), rs.getInt(3));
            System.out.printf(" %d  %s  %d\n", rs.getInt(1),
                    rs.getString(2), rs.getInt(3));
        }
    }

    private static void columnNames(ResultSet rs) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String tableNames = rsmd.getTableName(columnCount);
        System.out.println(tableNames);
        System.out.println("Columns:  ");
        StringBuilder names = new StringBuilder();
// The column count starts from 1
        for (int i = 1; i <= columnCount; i++) {
            names.append(rsmd.getColumnName(i));
            names.append("  ");
        }
        System.out.println(names);
    }

    private static ArrayList<String> showTables() throws SQLException {
        ArrayList<String> tables = new ArrayList<>();
//        ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_schema WHERE type ='table' AND name NOT LIKE 'sqlite_%';");

//       ResultSet rs = stmt.executeQuery("SELECT name  FROM sqlite_schema WHERE type = 'table' AND name NOT LIKE 'sqlite_%';");
//               ResultSet rs = stmt.executeQuery("SELECT sql FROM sqlite_master " +
//                       "WHERE tbl_name = 'students' AND type = 'table';");
        ResultSet rs = stmt.executeQuery("PRAGMA table_info(students);");

        while (rs.next()) {
            for (int i = 1; i < rs.getFetchSize(); i++) {
                System.out.print(String.format("%s  ", rs.getString(i)));
            }
            System.out.println();
            tables.add(rs.getString(2));
        }
        return tables;
    }

    private static void showTablesMetaData() throws SQLException {
        DatabaseMetaData m = con.getMetaData();
        ResultSet rs =
                m.getTables(con.getCatalog(), con.getSchema(), "%", null);
        while (rs.next()) {
            System.out.println("table = " + rs.getMetaData().getTableName(3));
            System.out.println(rs.getString(3));
        }
        rs = m.getSchemas();
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }

    }
}