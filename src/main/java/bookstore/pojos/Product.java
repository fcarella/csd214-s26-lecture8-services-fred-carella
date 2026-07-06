package bookstore.pojos;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link bookstore.entities.ProductEntity}
 * Separation of Concerns: Handles Console Presentation & I/O
 */
public abstract class Product extends Editable implements SaleableItem, Serializable {
    private Long dbId; // Primary key link for database synchronization
    private String productId; // UUID

    public Product() {
        setProductId(UUID.randomUUID().toString());
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "dbId=" + dbId +
                ", productId='" + productId + '\'' +
                "} ";
    }
}
