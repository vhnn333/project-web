import java.sql.*;

public class OrderDAO {

    public boolean createOrder(int userId, Product product, int quantity) {

        String insertOrder = "INSERT INTO ORDERS (USER_ID, TOTAL_AMOUNT) VALUES (?, ?)";
        String insertItem = "INSERT INTO ORDER_ITEMS (ORDER_ID, PRODUCT_ID, QUANTITY, PRICE) VALUES (?, ?, ?, ?)";
        String getOrderId = "SELECT MAX(ORDER_ID) AS ORDER_ID FROM ORDERS WHERE USER_ID = ?";
        String updateStock = "UPDATE PRODUCTS SET STOCK = STOCK - ? WHERE PRODUCT_ID = ? AND STOCK >= ?";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // TOTAL
            double total = product.getPrice() * quantity;

            // 1. Insert order
            try (PreparedStatement ps = conn.prepareStatement(insertOrder)) {
                ps.setInt(1, userId);
                ps.setDouble(2, total);
                ps.executeUpdate();
            }

            // 2. Get order ID
            int orderId = 0;
            try (PreparedStatement ps = conn.prepareStatement(getOrderId)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    orderId = rs.getInt("ORDER_ID");
                }
            }

            // 3. Insert order item
            try (PreparedStatement ps = conn.prepareStatement(insertItem)) {
                ps.setInt(1, orderId);
                ps.setInt(2, product.getProductId());
                ps.setInt(3, quantity);
                ps.setDouble(4, product.getPrice());
                ps.executeUpdate();
            }

            // 4. Update stock
            try (PreparedStatement ps = conn.prepareStatement(updateStock)) {
                ps.setInt(1, quantity);
                ps.setInt(2, product.getProductId());
                ps.setInt(3, quantity);
                int updated = ps.executeUpdate();

                if (updated == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            System.out.println("Gagal checkout: " + e.getMessage());
            try { conn.rollback(); } catch (Exception ex) {}
        } finally {
            try { conn.setAutoCommit(true); conn.close(); } catch (Exception e) {}
        }
        return false;
    }
}

