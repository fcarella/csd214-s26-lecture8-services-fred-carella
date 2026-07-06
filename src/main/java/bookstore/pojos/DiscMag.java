package bookstore.pojos;

import bookstore.entities.DiscMagEntity;
import java.util.Date;
import java.util.Scanner;

/**
 * DTO for {@link bookstore.entities.DiscMagEntity}
 */
public class DiscMag extends Magazine {
    private boolean hasDisc;

    public DiscMag() {
    }

    public DiscMag(boolean hasDisc, int orderQty, Date currentIssue, String title, double price, int copies) {
        super(orderQty, currentIssue, title, price, copies);
        this.hasDisc = hasDisc;
    }

    // Mapping: DTO to Database Entity
    public DiscMagEntity toEntity() {
        DiscMagEntity entity = new DiscMagEntity();
        entity.setId(this.getDbId());
        entity.setProductId(this.getProductId());
        entity.setTitle(this.getTitle());
        entity.setPrice(this.getPrice());
        entity.setCopies(this.getCopies());
        entity.setOrderQty(this.getOrderQty());
        entity.setCurrentIssue(this.getCurrentIssue());
        entity.setHasDisc(this.isHasDisc());
        return entity;
    }

    // Mapping: Database Entity to DTO
    public static DiscMag fromEntity(DiscMagEntity entity) {
        DiscMag dm = new DiscMag(
                entity.getHasDisc(),
                entity.getOrderQty(),
                entity.getCurrentIssue(),
                entity.getTitle(),
                entity.getPrice(),
                entity.getCopies()
        );
        dm.setDbId(entity.getId());
        dm.setProductId(entity.getProductId());
        return dm;
    }

    public void initialize(Scanner input) {
        super.initialize(input);

        System.out.println("Has Disc? (true/false):");
        this.hasDisc = getInput(input, false);
    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);

        System.out.println("Edit Has Disc [" + this.hasDisc + "]:");
        this.hasDisc = getInput(input, this.hasDisc);
    }

    @Override
    public void sellItem() {
        System.out.println("Selling Disc Magazine (Disc: " + hasDisc + ")");
        setCopies(getCopies() - 1);
    }

    public boolean isHasDisc() { return hasDisc; }
    public void setHasDisc(boolean h) { this.hasDisc = h; }

    @Override
    public String toString() {
        return "DiscMag{hasDisc=" + hasDisc + ", " + super.toString() + "}";
    }
}
