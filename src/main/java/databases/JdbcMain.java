package databases;

import java.sql.SQLException;

public class JdbcMain {

    public static void main(String[] args) {

        JdbsApp jdbsApp = new JdbsApp();

        try {
            jdbsApp.connection();
            jdbsApp.createDBTables();
            System.out.println(jdbsApp.getTablesNames());
            jdbsApp.insertIntoTeachers("OP", "POPL", "rt@kl.ui");
            jdbsApp.insertIntoTeachers("YTU", "SLKJH");
            jdbsApp.deleteFromTeacher("name", "YTU");
            jdbsApp.deleteFromTeacher( "id","1");
            jdbsApp.selectTeachersAll();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbsApp.disconnect();
        }


    }
}
