package com.cmput301f20t21.bookfriends.entities;

import com.google.firebase.firestore.DocumentId;

public class UserDocument {
    @DocumentId
    private String documentId;
    private String email;
    private String username;

    public UserDocument() {
        this("", "", "");
    }

    public UserDocument(String documentId, String email, String username) {
        this.documentId = documentId;
        this.email = email;
        this.username = username;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
