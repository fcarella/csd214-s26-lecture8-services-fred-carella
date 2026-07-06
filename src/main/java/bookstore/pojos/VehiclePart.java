package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

/**
 * DTO for {@link bookstore.entities.VehiclePartEntity}
 */
public abstract class VehiclePart extends Product {
    private String manufacturer;
    private double price;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VehiclePart that = (VehiclePart) o;
        return Double.compare(price, that.price) == 0 && Objects.equals(manufacturer, that.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manufacturer, price);
    }

    @Override
    public String toString() {
        return "VehiclePart{" +
                "manufacturer='" + manufacturer + '\'' +
                ", price=" + price +
                "} " + super.toString();
    }

    public VehiclePart() {
    }
    public VehiclePart(String manufacturer, double price) {
        this.manufacturer = manufacturer;
        this.price = price;
    }
    @Override
    public void initialize(Scanner input) {
        System.out.println("Enter Manufacturer");
        this.manufacturer= getInput(input, "Unknown Manufacturer");

        System.out.println("Enter Price");
        this.price= getInput(input, 0.0);

    }

    @Override
    public void edit(Scanner input) {
        System.out.println("Enter Manufacturer");
        this.manufacturer= getInput(input, getManufacturer());
        System.out.println("Enter Price");
        this.price= getInput(input, getPrice());
    }



}
