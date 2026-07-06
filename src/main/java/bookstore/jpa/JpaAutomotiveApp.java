package bookstore.jpa;

import bookstore.entities.TireEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class JpaAutomotiveApp {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bookstore-pu");
        EntityManager em = emf.createEntityManager();

        try {
            // --- CREATE ---
            System.out.println("\n[Step 1] Persisting a new Tire...");
            em.getTransaction().begin();
            TireEntity myTire = new TireEntity("Michelin", 249.99, 18);
            em.persist(myTire); // Hibernate generates INSERT
            em.getTransaction().commit();
            System.out.println("Tire saved with Database Primary Key ID: " + myTire.getId());

            // --- READ ---
            listTires(em, "[Step 2] Database Tires:");

            // --- UPDATE (Dirty Checking / Managed Entities) ---
            System.out.println("\n[Step 3] Editing Tire Price (Dirty Checking)...");
            em.getTransaction().begin();
            TireEntity managedTire = em.find(TireEntity.class, myTire.getId());
            if (managedTire != null) {
                managedTire.setPrice(199.99); // Changing Java field updates DB on commit
            }
            em.getTransaction().commit();
            listTires(em, "[Step 4] After Price Update:");

            // --- DELETE ---
            System.out.println("\n[Step 5] Deleting the Tire...");
            em.getTransaction().begin();
            TireEntity target = em.find(TireEntity.class, myTire.getId());
            if (target != null) {
                em.remove(target); // Hibernate generates DELETE
            }
            em.getTransaction().commit();
            listTires(em, "[Step 6] After Deletion:");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void listTires(EntityManager em, String header) {
        System.out.println("\n" + header);
        List<TireEntity> tires = em.createQuery("SELECT t FROM TireEntity t", TireEntity.class).getResultList();
        if (tires.isEmpty()) {
            System.out.println("No tires found.");
        } else {
            tires.forEach(t -> System.out.println(" > " + t.getManufacturer() + " ($" + t.getPrice() + ")"));
        }
    }
} 

