import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT PRODUCT_ID, NAME, PRICE, STOCK FROM PRODUCTS";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("PRODUCT_ID"),
                        rs.getString("NAME"),
                        rs.getDouble("PRICE"),
                        rs.getInt("STOCK")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error ambil produk: " + e.getMessage());
        }

        return list;
    }
}

