package com.cmput301f20t21.bookfriends.ui.request;

public class RequestItem {
    private String textRequest;

    public RequestItem(String requester) {
        this.textRequest = requester + " asked to borrow the book";
    }

    public String getTextRequest() {
        return textRequest;
    }
}
