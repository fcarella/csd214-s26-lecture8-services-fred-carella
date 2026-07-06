package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class BookEntity extends PublicationEntity {
    @Column(name = "author")
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookEntity() {
    }

    public BookEntity(String title, double price, int copies, String author) {
        super(title, price, copies);
        this.author = author;
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "author='" + author + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(author);
    }
}