package com.cmput301f20t21.bookfriends.fakes.tasks;

public class FakeFailTask<T> extends FakeTask<T> {
    public FakeFailTask(Exception e) {
        super.setResult(null);
        super.setIsComplete(true);
        super.setIsSuccessful(false);
        super.setException(e);
    }
}
