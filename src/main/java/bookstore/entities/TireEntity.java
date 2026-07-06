package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class TireEntity extends VehiclePartEntity {
    @Column(name = "diameter", nullable = true)
    private int diameter;

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public TireEntity() {
    }

    public TireEntity(String manufacturer, double price, int diameter) {
        super(manufacturer,  price);
        this.diameter = diameter;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TireEntity that = (TireEntity) o;
        return diameter == that.diameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), diameter);
    }

    @Override
    public String toString() {
        return "TireEntity{" +
                "diameter=" + diameter +
                "} " + super.toString();
    }
}