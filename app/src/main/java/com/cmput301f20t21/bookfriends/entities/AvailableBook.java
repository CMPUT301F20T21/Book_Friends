package com.cmput301f20t21.bookfriends.entities;

import java.util.Objects;

public class AvailableBook extends Book {
    private boolean requested;

    public AvailableBook() {
        super();
        requested = false;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public boolean getRequested() {
        return requested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableBook book = (AvailableBook) o;
        return this.getId().equals(book.getId()) &&
                this.getIsbn().equals(book.getIsbn()) &&
                this.getTitle().equals(book.getTitle()) &&
                this.getAuthor().equals(book.getAuthor()) &&
                Objects.equals(this.getDescription(), book.getDescription()) &&
                this.getOwner().equals(book.getOwner()) &&
                this.getStatus() == book.getStatus() &&
                this.requested == book.requested;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(),
                this.getIsbn(),
                this.getTitle(),
                this.getAuthor(),
                this.getDescription(),
                this.getOwner(),
                this.getStatus(),
                this.requested);
    }
}
