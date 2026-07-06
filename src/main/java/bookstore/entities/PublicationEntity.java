package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class PublicationEntity extends ProductEntity {
    @Column(name = "title")
    private String title;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "copies", nullable = true)
    private int copies;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public PublicationEntity(String title, double price, int copies) {
        this.title = title;
        this.price = price;
        this.copies = copies;
    }

    public PublicationEntity() {
    }
}