package com.cmput301f20t21.bookfriends.fakes.tasks;

public class FakeSuccessTask<T> extends FakeTask<T> {
    public FakeSuccessTask(T res) {
        super.setResult(res);
        super.setIsComplete(true);
        super.setIsSuccessful(true);
    }
}
