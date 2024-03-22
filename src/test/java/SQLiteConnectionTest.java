import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteConnectionTest {
    public static void main(String[] args) {
        try {
            // 加载SQLite JDBC驱动
            Class.forName("org.sqlite.JDBC");
            // 建立连接
            Connection conn = DriverManager.getConnection("jdbc:sqlite:sqlite-jpa.db");
            System.out.println("数据库连接成功");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
