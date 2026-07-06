package bookstore.jdbc;

import bookstore.pojos.Widget;
import java.sql.*;

public class JdbcWidgetApp {
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASS = "itstudies12345";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            // 1. Setup Table
            createTable(conn);

            // 2. Create (Insert)
            System.out.println("\n--- INSERTING ---");
            Widget w1 = new Widget("Industrial Widget", 49.99);
            insertWidget(conn, w1);

            // 3. Read (Select)
            System.out.println("\n--- READING ---");
            listWidgets(conn);

            // 4. Update
            System.out.println("\n--- UPDATING ---");
            updateWidgetPrice(conn, "Industrial Widget", 55.00);

            // 5. Delete
            System.out.println("\n--- DELETING ---");
            deleteWidget(conn, "Industrial Widget");
            listWidgets(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS widgets (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "product_id VARCHAR(36), " + // Column for our UUID
                "widget_name VARCHAR(255), " +
                "price DOUBLE)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'widgets' is ready.");
        }
    }

    private static void insertWidget(Connection conn, Widget w) throws SQLException {
        // SECURITY: Use ? placeholders to prevent SQL Injection
        String sql = "INSERT INTO widgets (product_id, widget_name, price) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, w.getProductId()); // The UUID String from the POJO
            ps.setString(2, w.getWidgetName());
            ps.setDouble(3, w.getPrice());
            ps.executeUpdate();
            System.out.println("Widget saved to DB: " + w.getWidgetName());
        }
    }

    private static void listWidgets(Connection conn) throws SQLException {
        String sql = "SELECT * FROM widgets";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("DB ID: %d | UUID: %s | Name: %s | Price: $%.2f%n",
                        rs.getInt("id"),
                        rs.getString("product_id"),
                        rs.getString("widget_name"),
                        rs.getDouble("price"));
            }
        }
    }

    private static void updateWidgetPrice(Connection conn, String name, double newPrice) throws SQLException {
        String sql = "UPDATE widgets SET price = ? WHERE widget_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newPrice);
            ps.setString(2, name);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Updated " + rowsAffected + " record(s).");
        }
    }

    private static void deleteWidget(Connection conn, String name) throws SQLException {
        String sql = "DELETE FROM widgets WHERE widget_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
            System.out.println("Deleted widget: " + name);
        }
    }
}

