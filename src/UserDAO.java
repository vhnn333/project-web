import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User login(String username, String password) {
        String sql = "SELECT USER_ID, USERNAME, PASSWORD, FULL_NAME FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD"),
                        rs.getString("FULL_NAME")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error login: " + e.getMessage());
        }
        return null;
    }
}

