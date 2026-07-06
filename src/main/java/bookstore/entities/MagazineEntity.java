package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Date;
import java.util.Objects;

@Entity
public class MagazineEntity extends PublicationEntity {
    @Column(name = "order_qty", nullable = true)
    private int orderQty;

    @Column(name = "current_issue")
    private Date currentIssue;

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public Date getCurrentIssue() {
        return currentIssue;
    }

    public void setCurrentIssue(Date currentIssue) {
        this.currentIssue = currentIssue;
    }

    public MagazineEntity() {
    }

    public MagazineEntity(String title, double price, int copies, int orderQty, Date currentIssue) {
        super(title, price, copies);
        this.orderQty = orderQty;
        this.currentIssue = currentIssue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MagazineEntity that = (MagazineEntity) o;
        return orderQty == that.orderQty && Objects.equals(currentIssue, that.currentIssue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderQty, currentIssue);
    }

    @Override
    public String toString() {
        return "MagazineEntity{" +
                "orderQty=" + orderQty +
                ", currentIssue=" + currentIssue +
                "} " + super.toString();
    }
}