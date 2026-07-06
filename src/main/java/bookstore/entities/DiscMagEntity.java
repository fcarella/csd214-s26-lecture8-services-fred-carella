package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Date;
import java.util.Objects;

@Entity
public class DiscMagEntity extends MagazineEntity {
    @Column(name = "has_disc", nullable = true)
    private boolean hasDisc;

    public boolean getHasDisc() {
        return hasDisc;
    }

    public void setHasDisc(boolean hasDisc) {
        this.hasDisc = hasDisc;
    }

    public DiscMagEntity() {
    }

    public DiscMagEntity(String title, double price, int copies, int orderQty, Date currentIssue, boolean hasDisc) {
        super(title, price, copies, orderQty, currentIssue);
        this.hasDisc = hasDisc;
    }

    @Override
    public String toString() {
        return "DiscMagEntity{" +
                "hasDisc=" + hasDisc +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DiscMagEntity that = (DiscMagEntity) o;
        return hasDisc == that.hasDisc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hasDisc);
    }
}