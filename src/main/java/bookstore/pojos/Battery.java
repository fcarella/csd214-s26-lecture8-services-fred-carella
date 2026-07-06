package bookstore.pojos;

import bookstore.entities.BatteryEntity;

import java.util.Objects;
import java.util.Scanner;

/**
 * DTO for {@link BatteryEntity}
 */
public class Battery extends VehiclePart{
    private int coldCrankingAmps;

    public Battery() {
    }

    public Battery(int coldCrankingAmps) {
        this.coldCrankingAmps = coldCrankingAmps;
    }

    public Battery(String manufacturer, double price, int coldCrankingAmps) {
        super(manufacturer, price);
        this.coldCrankingAmps = coldCrankingAmps;
    }

    @Override
    public String toString() {
        return "Battery{" +
                "coldCrankingAmps=" + coldCrankingAmps +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Battery battery = (Battery) o;
        return coldCrankingAmps == battery.coldCrankingAmps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coldCrankingAmps);
    }

    public int getColdCrankingAmps() {
        return coldCrankingAmps;
    }

    public void setColdCrankingAmps(int coldCrankingAmps) {
        this.coldCrankingAmps = coldCrankingAmps;
    }

    @Override
    public void initialize(Scanner input) {
        // Pass scanner up to parent
        super.initialize(input);
        System.out.println("Enter Cold Cranking Amps:");
        this.coldCrankingAmps = getInput(input, 0);
    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);
        System.out.println("Enter Cold Cranking Amps:");
        this.coldCrankingAmps = getInput(input, getColdCrankingAmps());
    }

    @Override
    public void sellItem() {
        System.out.println("Selling a Battery");
    }

    // Add to src/main/java/bookstore/pojos/Battery.java

    public bookstore.entities.BatteryEntity toEntity() {
        bookstore.entities.BatteryEntity entity = new bookstore.entities.BatteryEntity();
        entity.setId(this.getDbId()); // Inherited from Product DTO
        entity.setProductId(this.getProductId()); // Inherited from Product DTO
        entity.setManufacturer(this.getManufacturer()); // Inherited from VehiclePart DTO
        entity.setPrice(this.getPrice()); // Inherited from VehiclePart DTO
        entity.setColdCrankingAmps(this.getColdCrankingAmps());
        return entity;
    }

    public static Battery fromEntity(bookstore.entities.BatteryEntity entity) {
        Battery battery = new Battery(
                entity.getManufacturer(),
                entity.getPrice(),
                entity.getColdCrankingAmps()
        );
        battery.setDbId(entity.getId());
        battery.setProductId(entity.getProductId());
        return battery;
    }
}
