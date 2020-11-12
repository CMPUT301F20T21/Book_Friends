package com.cmput301f20t21.bookfriends.entities;

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
}
