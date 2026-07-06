package bookstore.jdbc;
import bookstore.pojos.*;

import java.sql.*;

public class JdbcFullApp {
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASSWORD = "itstudies12345";

    public static void main(String[] args) {
        try {
            // 1. Setup
            createTable();

            // 2. Populate
            System.out.println("\n=== INSERTING DATA ===");
            insertBook(new Book("George Orwell", "1984", 15.99, 10));
            insertTicket(new Ticket()); // Description set inside helper
            listAllItems();

            // 3. LOGIC + PERSISTENCE (The "SaleableItem" demo)
            System.out.println("\n=== SELLING ITEM (Logic + DB Update) ===");
            sellItemFromDB("1984");
            
            listAllItems();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void sellItemFromDB(String title) throws SQLException {
        String selectSql = "SELECT * FROM products WHERE title = ?";
        String updateSql = "UPDATE products SET copies = ? WHERE title = ?"; // Only updates copies for now
        
        try (Connection conn = getConnection(); 
             PreparedStatement psSelect = conn.prepareStatement(selectSql)) {
            
            // A. LOAD
            psSelect.setString(1, title);
            ResultSet rs = psSelect.executeQuery();
            
            if (rs.next()) {
                String type = rs.getString("product_type");
                SaleableItem item = null;

                // factory logic (simplified)
                if ("BOOK".equals(type)) {
                    Book b = new Book();
                    b.setTitle(rs.getString("title"));
                    b.setCopies(rs.getInt("copies"));
                    b.setPrice(rs.getDouble("price"));
                    b.setAuthor(rs.getString("author"));
                    item = b;
                }
                
                if (item != null) {
                    System.out.println("Loaded from DB: " + item);
                    
                    // B. LOGIC (Interface method)
                    item.sellItem(); // Decrements copies in Java object
                    
                    // C. PERSISTENCE (Save changes)
                    if (item instanceof Publication) {
                        try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                            psUpdate.setInt(1, ((Publication) item).getCopies());
                            psUpdate.setString(2, title);
                            psUpdate.executeUpdate();
                            System.out.println("Database updated with new copy count.");
                        }
                    }
                }
            } else {
                System.out.println("Item not found: " + title);
            }
        }
    }

    private static void createTable() throws SQLException {
        String dropSql = "DROP TABLE IF EXISTS products";
        String createSql = "CREATE TABLE products (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "product_type VARCHAR(50), " + 
                     "price DOUBLE, " +
                     "title VARCHAR(255), " +      
                     "copies INT, " +              
                     "author VARCHAR(255), " +
                     "order_qty INT, " +
                     "issue_date DATE, " +
                     "has_disc BOOLEAN, " +
                     "description VARCHAR(255)" +  
                     ")";
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(dropSql);
            stmt.execute(createSql);
            System.out.println("Table 'products' reset.");
        }
    }

    // --- INSERT METHODS (Simplified for brevity) ---
    private static void insertBook(Book b) throws SQLException {
        String sql = "INSERT INTO products (product_type, title, price, copies, author) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "BOOK");
            ps.setString(2, b.getTitle());
            ps.setDouble(3, b.getPrice());
            ps.setInt(4, b.getCopies());
            ps.setString(5, b.getAuthor());
            ps.executeUpdate();
        }
    }

    private static void insertTicket(Ticket t) throws SQLException {
        t.setDescription("Concert Ticket");
        t.setPrice(75.00);
        String sql = "INSERT INTO products (product_type, description, price) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "TICKET");
            ps.setString(2, t.getDescription());
            ps.setDouble(3, t.getPrice());
            ps.executeUpdate();
        }
    }

    private static void listAllItems() throws SQLException {
        String sql = "SELECT * FROM products";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("--- DB Inventory ---");
            while (rs.next()) {
                String type = rs.getString("product_type");
                if ("BOOK".equals(type)) {
                    System.out.println("Book: " + rs.getString("title") + " | Copies: " + rs.getInt("copies"));
                } else if ("TICKET".equals(type)) {
                    System.out.println("Ticket: " + rs.getString("description"));
                }
            }
        }
    }
}
