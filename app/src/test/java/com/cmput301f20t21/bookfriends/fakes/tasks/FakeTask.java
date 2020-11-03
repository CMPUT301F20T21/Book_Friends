package com.cmput301f20t21.bookfriends.fakes.tasks;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

public class FakeTask<T> extends Task<T>{
    private boolean complete;
    private boolean success;
    private T result;
    private Exception exception;

    public void setIsComplete(boolean c) {
        complete = c;
    }

    public void setIsSuccessful(boolean s) {
        success = s;
    }

    public void setResult(T r) {
        result = r;
    }

    public void setException(Exception e) {
        exception = e;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean isSuccessful() {
        return success;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Nullable
    @Override
    public T getResult() {
        return result;
    }

    @Nullable
    @Override
    public <X extends Throwable> T getResult(@NonNull Class<X> aClass) throws X {
        return null;
    }

    @Nullable
    @Override
    public Exception getException() {
        return exception;
    }

    @NonNull
    @Override
    public Task<T> addOnSuccessListener(@NonNull OnSuccessListener<? super T> onSuccessListener) {
        if (isSuccessful()) {
            onSuccessListener.onSuccess(getResult());
        }
        return this;
    }

    @NonNull
    @Override
    public Task<T> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super T> onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task<T> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super T> onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task<T> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        if (!isSuccessful()) {
            onFailureListener.onFailure(getException());
        }
        return this;
    }

    @NonNull
    @Override
    public Task<T> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task<T> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        return null;
    }
}
