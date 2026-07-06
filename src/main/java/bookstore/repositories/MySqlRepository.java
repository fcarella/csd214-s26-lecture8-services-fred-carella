package bookstore.repositories;

import bookstore.entities.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

/**
 * MySqlRepository: A database-backed, persistent implementation of IRepository.
 * Delegates storage, query execution, and transactions to a MySQL instance on Port 3333 [7].
 */
public class MySqlRepository implements IRepository<ProductEntity> {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public MySqlRepository() {
        // Loads the specific production MySQL persistence unit from persistence.xml [7]
        this.emf = Persistence.createEntityManagerFactory("mysql-pu");
        this.em = emf.createEntityManager();
    }

    @Override
    public ProductEntity save(ProductEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save a null entity.");
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Upsert strategy: Persist if new, merge if record already exists in MySQL [7]
            if (entity.getId() == null) {
                em.persist(entity);
            } else {
                entity = em.merge(entity);
            }
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public ProductEntity findById(Long id) {
        if (id == null) {
            return null;
        }
        // Direct database primary key lookup
        return em.find(ProductEntity.class, id);
    }

    @Override
    public ProductEntity findByProductId(String productId) {
        if (productId == null) {
            return null;
        }

        try {
            // Optimized query using named parameter binding to defend against SQL Injection [7]
            return em.createQuery(
                            "SELECT p FROM ProductEntity p WHERE p.productId = :prodId",
                            ProductEntity.class)
                    .setParameter("prodId", productId)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null; // Gracefully return null if no matching UUID is found
        }
    }

    @Override
    public List<ProductEntity> findAll() {
        // JPQL query mapped to the ProductEntity single-table inheritance tree
        return em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ProductEntity entity = em.find(ProductEntity.class, id);
            if (entity != null) {
                em.remove(entity); // Triggers SQL DELETE
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public long count() {
        // Highly optimized aggregate query executing directly inside the SQL engine [7]
        return em.createQuery("SELECT COUNT(p) FROM ProductEntity p", Long.class)
                .getSingleResult();
    }

    @Override
    public int deleteAll() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Safe bulk delete to drop records in a single query, avoiding the N+1 problem [7]
            int deletedCount = em.createQuery("DELETE FROM ProductEntity").executeUpdate();
            tx.commit();
            return deletedCount;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public String getDataSourceType() {
        return "PERSISTENT MySQL (Production - Port 3333)";
    }

    @Override
    public void close() {
        // Safely close connection pools to prevent connection leaks
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        System.out.println("Persistent MySQL connections closed cleanly.");
    }
}