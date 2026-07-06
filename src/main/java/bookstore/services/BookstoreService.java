package bookstore.services;

import bookstore.entities.ProductEntity;
import bookstore.entities.PublicationEntity;
import bookstore.entities.TicketEntity;
import bookstore.entities.VehiclePartEntity;
import bookstore.repositories.IRepository;

/**
 * BookstoreService: This is the generalist "Chef" of our system.
 * Houses business rules regarding general store capabilities [6].
 */
public class BookstoreService {
    private final IRepository<ProductEntity> repository;

    // CONSTRUCTOR DEPENDENCY INJECTION:
    // The Service explicitly declares what it needs to function [6].
    // It does NOT use "new MySqlRepository()".
    public BookstoreService(IRepository<ProductEntity> repository) {
        this.repository = repository;
    }

    /**
     * Business Logic: Process a sale safely.
     * Decrements inventory stock only if the product is available.
     */
    public void performSale(Long id) {
        ProductEntity item = repository.findById(id);
        if (item == null) {
            throw new IllegalArgumentException("Service Error: Product ID not found.");
        }

        // Business Rule: Only Publications (Books/Magazines) have decrementable copies [6]
        if (item instanceof PublicationEntity pub) {
            if (pub.getCopies() > 0) {
                pub.setCopies(pub.getCopies() - 1); // Apply recipe
                repository.save(pub); // Save modified state back to the repository [6]
                System.out.println("Service: Inventory successfully decremented for: " + pub.getTitle());
            } else {
                System.out.println("Service Warning: Product is out of stock!");
            }
        } else {
            // General sale logic for non-inventory products (Tickets)
            System.out.println("Service: Processed sale for non-inventory item: " + item.getId());
        }
    }

    //    public void applyDiscount(Long id, double percent) {
//        ProductEntity item = repository.findById(id);
//        if (item != null) {
//            double oldPrice = item.getPrice();
//            double newPrice = oldPrice * (1.0 - percent);
//            item.setPrice(newPrice);
//            repository.save(item); // Update database safely
//            System.out.printf("Service: %s price dropped from $%.2f to $%.2f\n",
//                    item.getId(), oldPrice, newPrice);
//        } else {
//            System.out.println("Service Error: Product ID not found!");
//        }
//    }
    public void applyDiscount(Long id, double percent) {
        ProductEntity item = repository.findById(id);
        if (item == null) {
            System.out.println("Service Error: Product ID not found!");
            return;
        }

        double oldPrice = 0.0;
        double newPrice = 0.0;
        String label = "Product";

        // Polymorphically inspect concrete subclasses to safely mutate prices [7]
        if (item instanceof PublicationEntity pub) {
            oldPrice = pub.getPrice();
            newPrice = oldPrice * (1.0 - percent);
            pub.setPrice(newPrice);
            repository.save(pub); // Save persistent update
            label = pub.getTitle();
        } else if (item instanceof VehiclePartEntity vp) {
            oldPrice = vp.getPrice();
            newPrice = oldPrice * (1.0 - percent);
            vp.setPrice(newPrice);
            repository.save(vp);
            label = vp.getManufacturer();
        } else if (item instanceof TicketEntity ticket) {
            oldPrice = ticket.getPrice();
            newPrice = oldPrice * (1.0 - percent);
            ticket.setPrice(newPrice);
            repository.save(ticket);
            label = ticket.getDescription();
        }

        System.out.printf("Service: Discount applied to %s. Price dropped from $%.2f to $%.2f\n",
                label, oldPrice, newPrice);
    }

}

