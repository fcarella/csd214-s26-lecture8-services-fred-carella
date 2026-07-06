package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class TicketEntity extends ProductEntity {
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TicketEntity() {
    }

    public TicketEntity(String description, double price) {
        this.description = description;
        this.price = price;
    }
}
