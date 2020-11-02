package com.cmput301f20t21.bookfriends.entities;

import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;

public class Request {
    private String id;
    private String requester;
    private String bookId;
    private REQUEST_STATUS status;

    public Request(String id, String requester, String bookId, String status) {
        this.id = id;
        this.requester = requester;
        this.bookId = bookId;
        this.status = REQUEST_STATUS.valueOf(status);
    }

    public String getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public String getRequester() {
        return requester;
    }

    public REQUEST_STATUS getStatus() {
        return status;
    }
}
