import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost/postgres?user=postgres&password=starlight";
        Connection conn = DriverManager.getConnection(url);

        PreparedStatement st = conn.prepareStatement("CREATE TABLE IF NOT EXISTS hollowcub (player VARCHAR(100))");
        st.execute();
    }
}
