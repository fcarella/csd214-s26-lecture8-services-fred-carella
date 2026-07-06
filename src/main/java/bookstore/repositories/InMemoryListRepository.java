package bookstore.repositories;

import bookstore.entities.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * InMemoryListRepository: In-memory sequential storage implementation of IRepository.
 * Uses a linear ArrayList structure, demonstrating O(n) search time-complexity.
 */
public class InMemoryListRepository implements IRepository<ProductEntity> {
    private final List<ProductEntity> list = new ArrayList<>();
    private Long idCounter = 1L; // Simulated auto-increment primary key counter

    @Override
    public ProductEntity save(ProductEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save a null entity.");
        }

        // 1. Assign ID if it's a new entity (mimicking MySQL AUTO_INCREMENT)
        if (entity.getId() == null) {
            entity.setId(idCounter++);
            list.add(entity);
        } else {
            // 2. Upsert Strategy: If the entity has an ID, find and replace it
            int foundIndex = -1;
            for (int i = 0; i < list.size(); i++) {
                if (Objects.equals(list.get(i).getId(), entity.getId())) {
                    foundIndex = i;
                    break;
                }
            }

            if (foundIndex != -1) {
                list.set(foundIndex, entity); // Update existing
            } else {
                list.add(entity); // Insert if ID was manually provided but missing from collection
            }
        }
        return entity;
    }

    @Override
    public ProductEntity findById(Long id) {
        if (id == null) {
            return null;
        }

        // Linear O(n) search: Evaluates every element sequentially until a match is found
        for (ProductEntity entity : list) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null; // ID not present in list
    }

    @Override
    public ProductEntity findByProductId(String productId) {
        if (productId == null) {
            return null;
        }

        // Linear O(n) search by unique String Business Key (UUID)
        for (ProductEntity entity : list) {
            if (Objects.equals(entity.getProductId(), productId)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<ProductEntity> findAll() {
        // Return a shallow copy of the list to preserve encapsulation of internal list
        return new ArrayList<>(list);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        // Linear O(n) search to find the matching primary key and drop it
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i).getId(), id)) {
                list.remove(i);
                break;
            }
        }
    }

    @Override
    public long count() {
        return list.size();
    }

    @Override
    public int deleteAll() {
        int itemsDeleted = list.size();
        list.clear();
        return itemsDeleted;
    }

    @Override
    public String getDataSourceType() {
        return "VOLATILE RAM (ArrayList - Sequential Search)";
    }

    @Override
    public void close() {
        // No network connection pools or filesystem streams require teardown.
        System.out.println("Volatile memory cleared (InMemoryListRepository).");
    }
}