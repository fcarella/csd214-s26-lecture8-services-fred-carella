package bookstore.pojos;

import bookstore.entities.TireEntity;

import java.util.Objects;
import java.util.Scanner;

/**
 * DTO for {@link bookstore.entities.TireEntity}
 */
public class Tire extends VehiclePart{
    private int diameter;

    public Tire() {
    }

    public Tire(int diameter) {
        this.diameter = diameter;
    }

    public Tire(String manufacturer, double price, int diameter) {
        super(manufacturer, price);
        this.diameter = diameter;
    }

    @Override
    public String toString() {
        return "Tire{" +
                "diameter=" + diameter +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tire tire = (Tire) o;
        return diameter == tire.diameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), diameter);
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    @Override
    public void initialize(Scanner input) {
        // Pass scanner up to parent
        super.initialize(input);

        System.out.println("Enter Diameter:");
        this.diameter = getInput(input, 0);

    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);
        System.out.println("Enter Diameter:");
        this.diameter = getInput(input, getDiameter());

    }


    @Override
    public void sellItem() {
        System.out.println("Selling a Tire");
    }

    // Add to src/main/java/bookstore/pojos/Tire.java
    public TireEntity toEntity() {
        TireEntity entity = new TireEntity();
        entity.setId(this.getDbId()); // Map base long ID
        entity.setProductId(this.getProductId()); // Map base String UUID
        entity.setManufacturer(this.getManufacturer());
        entity.setPrice(this.getPrice());
        entity.setDiameter(this.getDiameter());
        return entity;
    }

    public static Tire fromEntity(TireEntity entity) {
        Tire tire = new Tire(
                entity.getManufacturer(),
                entity.getPrice(),
                entity.getDiameter()
        );
        tire.setDbId(entity.getId());
        tire.setProductId(entity.getProductId());
        return tire;
    }
}
