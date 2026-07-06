package bookstore.services;

import bookstore.entities.BookEntity;
import bookstore.entities.ProductEntity;
import bookstore.repositories.InMemoryMapRepository;
import bookstore.repositories.IRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookstoreServiceTest {
    @Test
    void testPerformSaleReducesInventoryCount() {
        // 1. ARRANGE: Inject the in-memory map repo (No network overhead) [6]
        IRepository<ProductEntity> mockRepo = new InMemoryMapRepository();
        BookEntity testBook = new BookEntity("Dune", 25.00, 10, "Frank Herbert");
        mockRepo.save(testBook);

        BookstoreService service = new BookstoreService(mockRepo);

        // 2. ACT: Execute Business Rule [6]
        service.performSale(testBook.getId());

        // 3. ASSERT: Verify state change
        BookEntity updatedBook = (BookEntity) mockRepo.findById(testBook.getId());
        assertEquals(9, updatedBook.getCopies());
    }
}

