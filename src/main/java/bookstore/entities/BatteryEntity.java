package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class BatteryEntity extends VehiclePartEntity {
    @Column(name = "cold_cranking_amps", nullable = true)
    private int coldCrankingAmps;

    public int getColdCrankingAmps() {
        return coldCrankingAmps;
    }

    public void setColdCrankingAmps(int coldCrankingAmps) {
        this.coldCrankingAmps = coldCrankingAmps;
    }

    public BatteryEntity() {
    }

    public BatteryEntity(String manufacturer, double price, int coldCrankingAmps) {
        super(manufacturer, price);
        this.coldCrankingAmps = coldCrankingAmps;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BatteryEntity that = (BatteryEntity) o;
        return coldCrankingAmps == that.coldCrankingAmps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coldCrankingAmps);
    }

    @Override
    public String toString() {
        return "BatteryEntity{" +
                "coldCrankingAmps=" + coldCrankingAmps +
                "} " + super.toString();
    }
}