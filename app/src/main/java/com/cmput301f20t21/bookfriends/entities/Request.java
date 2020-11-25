package com.cmput301f20t21.bookfriends.entities;

import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.GeoPoint;

import java.util.Objects;

public class Request {
    @DocumentId
    private String id;
    private String requester;
    private String bookId;
    private REQUEST_STATUS status;
    private GeoPoint meetingLocation;

    // empty constructor to be use by Document.toObject()
    public Request() {
    }

    // default constructor
    public Request(String id, String requester, String bookId, REQUEST_STATUS status) {
        this.id = id;
        this.requester = requester;
        this.bookId = bookId;
        this.status = status;
    }

    // constructor with a meeting location
    public Request(String id, String requester, String bookId, REQUEST_STATUS status, GeoPoint location) {
        this.id = id;
        this.requester = requester;
        this.bookId = bookId;
        this.status = status;
        this.meetingLocation = location;
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

    public GeoPoint getMeetingLocation() {
        return meetingLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return id.equals(request.id) &&
                requester.equals(request.requester) &&
                bookId.equals(request.bookId) &&
                status == request.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requester, bookId, status);
    }
}
