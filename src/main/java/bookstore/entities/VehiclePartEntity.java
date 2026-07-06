package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class VehiclePartEntity extends ProductEntity {
    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "price", nullable = false)
    private double price;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public VehiclePartEntity() {
    }

    public VehiclePartEntity(String manufacturer, double price) {
        this.manufacturer = manufacturer;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VehiclePartEntity that = (VehiclePartEntity) o;
        return Double.compare(price, that.price) == 0 && Objects.equals(manufacturer, that.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manufacturer, price);
    }

    @Override
    public String toString() {
        return "VehiclePartEntity{" +
                "manufacturer='" + manufacturer + '\'' +
                ", price=" + price +
                "} " + super.toString();
    }
}