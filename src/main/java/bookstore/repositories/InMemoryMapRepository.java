package bookstore.repositories;

import bookstore.entities.ProductEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * InMemoryMapRepository: In-memory indexed storage implementation of IRepository.
 * Uses a HashMap structure, demonstrating O(1) constant-time search complexity
 * for primary key lookups.
 */
public class InMemoryMapRepository implements IRepository<ProductEntity> {
    private final Map<Long, ProductEntity> map = new HashMap<>();
    private Long idCounter = 1L; // Simulated auto-increment primary key counter

    @Override
    public ProductEntity save(ProductEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save a null entity.");
        }

        // 1. Assign ID if it's a new entity (mimicking MySQL AUTO_INCREMENT)
        if (entity.getId() == null) {
            entity.setId(idCounter++);
        }

        // 2. Constant time O(1) insert or update (upsert)
        map.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public ProductEntity findById(Long id) {
        if (id == null) {
            return null;
        }
        // Constant time O(1) lookup using hashing algorithm to resolve key bucket directly [6]
        return map.get(id);
    }

    @Override
    public ProductEntity findByProductId(String productId) {
        if (productId == null) {
            return null;
        }

        // Sequential O(n) search over map values is required here, as the primary key
        // indexing of the HashMap only applies to the Long database ID, not the business key [6].
        for (ProductEntity entity : map.values()) {
            if (Objects.equals(entity.getProductId(), productId)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<ProductEntity> findAll() {
        // Hydrates a new ArrayList using the map's active values [6]
        return new ArrayList<>(map.values());
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }
        // Constant time O(1) indexed key-value pair removal
        map.remove(id);
    }

    @Override
    public long count() {
        return map.size();
    }

    @Override
    public int deleteAll() {
        int itemsDeleted = map.size();
        map.clear();
        return itemsDeleted;
    }

    @Override
    public String getDataSourceType() {
        return "VOLATILE RAM (HashMap - Indexed Search)";
    }

    @Override
    public void close() {
        // No connection pooling or external file sockets require teardown for RAM storage.
        System.out.println("Volatile memory cleared (InMemoryMapRepository).");
    }
}