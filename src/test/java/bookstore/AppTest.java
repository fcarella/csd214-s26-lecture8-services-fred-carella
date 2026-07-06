package bookstore;

import bookstore.entities.ProductEntity;
import bookstore.pojos.*;
import bookstore.repositories.IRepository;
import bookstore.repositories.InMemoryListRepository;
import bookstore.services.AutomotiveService;
import bookstore.services.BookstoreService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    private final InputStream originalSystemIn = System.in;

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
    }

    @Test
    void testAppFlow_AddAndEditBook() {
        // 1. Build the Clean Script
        StringBuilder script = new StringBuilder();

        // --- ADD BOOK ---
        script.append("1\n");             // Main Menu: Add Items
        script.append("1\n");             // Add Menu: Add Book
        script.append("Dune\n");          // Title
        script.append("Frank Herbert\n"); // Author
        script.append("10\n");            // Copies
        script.append("25.00\n");         // Price
        script.append("99\n");            // Exit Add Menu

        // --- EDIT BOOK ---
        script.append("2\n");             // Main Menu: Edit Items
        script.append("0\n");             // Select Index 0
        script.append("Dune Messiah\n");  // Change Title
        script.append("\n");              // Price: Keep
        script.append("\n");              // Copies: Keep
        script.append("\n");              // Author: Keep

        // --- QUIT ---
        script.append("99\n");            // Quit

        // 2. Inject
        System.setIn(new ByteArrayInputStream(script.toString().getBytes()));

// Inside AppTest.java testAppFlow_AddAndEditBook()

// 1. Instantiate in-memory repository and mock services for test isolation
        IRepository<ProductEntity> repository = new InMemoryListRepository();
        BookstoreService bookstoreService = new BookstoreService(repository);
        AutomotiveService automotiveService = new AutomotiveService(repository);

// 2. Inject all three dependencies into the test instance
        App app = new App(repository, bookstoreService, automotiveService) {
            @Override
            public void populate() { /* clean start */ }
        };
        app.run();

        // 4. Verify
        Book expected = new Book("Frank Herbert", "Dune Messiah", 25.00, 10);
        SaleableItem result = app.findItem(expected);

        assertNotNull(result);
        assertEquals("Dune Messiah", ((Book)result).getTitle());
    }
}