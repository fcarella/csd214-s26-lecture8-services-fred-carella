package bookstore.repositories;

import java.util.List;

public interface IRepository<T> {
    T save(T entity);                  // Performs polymorphic Insert or Update (Upsert)
    T findById(Long id);               // Retrieves a record by internal DB Primary Key
    T findByProductId(String productId); // Optimized lookup using UUID Business Key [7]
    List<T> findAll();                 // Retrieves all polymorphic records
    void delete(Long id);              // Deletes a record by internal DB Primary Key
    long count();                      // Optimized aggregate database row count [7]
    int deleteAll();                   // Performance-safe bulk delete (prevents N+1) [7]
    String getDataSourceType();        // Returns the string label of the active engine
    void close();                      // Safely releases resources/connections
}
