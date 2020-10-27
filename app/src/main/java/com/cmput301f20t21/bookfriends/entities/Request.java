package com.cmput301f20t21.bookfriends.entities;

import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;

public class Request {
    private String id;
    private String requesterId;
    private String bookId;
    private REQUEST_STATUS status;

    public Request(String id, String requesterId, String bookId, String status) {
        this.id = id;
        this.requesterId = requesterId;
        this.bookId = bookId;
        this.status = REQUEST_STATUS.valueOf(status);
    }

    public String getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public REQUEST_STATUS getStatus() {
        return status;
    }
}
