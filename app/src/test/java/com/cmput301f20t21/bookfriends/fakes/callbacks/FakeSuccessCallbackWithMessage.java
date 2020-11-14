package com.cmput301f20t21.bookfriends.fakes.callbacks;

import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;

public class FakeSuccessCallbackWithMessage<T> implements OnSuccessCallbackWithMessage<T> {
    @Override
    public void run(T msg) {
    }
}
