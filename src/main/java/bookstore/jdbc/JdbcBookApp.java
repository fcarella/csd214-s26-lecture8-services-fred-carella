package bookstore.jdbc;
import bookstore.pojos.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcBookApp {
    // Note: Port 3333 as defined in your docker-compose.yml
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASSWORD = "itstudies12345";

    public static void main(String[] args) {
        try {
            // 1. Ensure Table Exists
            createTable();

            // 2. Create (Insert) a Book
            System.out.println("--- INSERTING BOOK ---");
            Book newBook = new Book("J.R.R. Tolkien", "The Hobbit", 19.99, 10);
            insertBook(newBook);
            listBooks();

            // 3. Edit (Update) a Book
            // We'll update the price and copies of 'The Hobbit'
            System.out.println("\n--- UPDATING BOOK ---");
            newBook.setPrice(99.99);
            newBook.setCopies(50);
            updateBookByTitle(newBook);
            listBooks();

            // 4. Delete a Book
            System.out.println("\n--- DELETING BOOK ---");
            deleteBookByTitle("The Hobbit");
            listBooks();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "title VARCHAR(255), " +
                     "author VARCHAR(255), " +
                     "price DOUBLE, " +
                     "copies INT)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'books' checked/created.");
        }
    }

    private static void insertBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, price, copies) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setDouble(3, book.getPrice());
            pstmt.setInt(4, book.getCopies());

            int rows = pstmt.executeUpdate();
            System.out.println("Inserted " + rows + " row(s).");
        }
    }

    private static void updateBookByTitle(Book book) throws SQLException {
        // Updating based on Title for demonstration (In real apps, use ID)
        String sql = "UPDATE books SET price = ?, copies = ?, author = ? WHERE title = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, book.getPrice());
            pstmt.setInt(2, book.getCopies());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getTitle());

            int rows = pstmt.executeUpdate();
            System.out.println("Updated " + rows + " row(s) for title: " + book.getTitle());
        }
    }

    private static void deleteBookByTitle(String title) throws SQLException {
        String sql = "DELETE FROM books WHERE title = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);

            int rows = pstmt.executeUpdate();
            System.out.println("Deleted " + rows + " row(s) with title: " + title);
        }
    }

    private static void listBooks() throws SQLException {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Current Database Content:");
            boolean empty = true;
            while (rs.next()) {
                empty = false;
                // Reconstruct Book object from DB
                String title = rs.getString("title");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                int copies = rs.getInt("copies");
                int id = rs.getInt("id"); // Captured but not stored in POJO currently

                System.out.printf("  [ID: %d] %s by %s ($%.2f) - %d copies%n",
                        id, title, author, price, copies);
            }
            if (empty) System.out.println("  (Table is empty)");
        }
    }
}
