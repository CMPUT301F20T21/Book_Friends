package com.cmput301f20t21.bookfriends.fakes.repositories;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeFailTask;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeSuccessTask;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.google.android.gms.internal.firebase_auth.zzff;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.MultiFactor;
import com.google.firebase.auth.MultiFactorInfo;
import com.google.firebase.auth.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

interface OnDeleteCallback {
    void run(String uid);
}

public class FakeAuthRepository implements AuthRepository {
    private Map<String, FakeFirebaseUser> authMap;
    private String username;
    private String uid;

    public FakeAuthRepository() {
        authMap = new HashMap<>();
//        authMap.put("1", new FakeFirebaseUser("1", "test@gmail.com", "123456"));
    }

    @Override
    public User getCurrentUser() {
        FakeFirebaseUser user = authMap.get(uid);
        return new User(uid, username, user.getEmail());
    }

    @Override
    public Task<AuthResult> createUserAuth(String email, String password) {
        boolean isEmailValid = true;
        for (FakeFirebaseUser user : authMap.values()) {
            if (user.getEmail().equals(email)) {
                isEmailValid = false;
                break;
            }
        }
        if (isEmailValid) {
            String id = UUID.randomUUID().toString();
            FakeFirebaseUser user = new FakeFirebaseUser(id, email, password, this::delete);
            authMap.put(id, user);
            return new FakeSuccessTask(new FakeAuthResult(user));
        } else {
            return new FakeFailTask(new Exception());
        }
    }

    @Override
    public Task<AuthResult> signIn(String username, String email, String password) {
        for (FakeFirebaseUser user : authMap.values()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                this.username = username;
                this.uid = user.getUid();
                return new FakeSuccessTask(null);
            }
        }

        return new FakeFailTask(new Exception());
    }

    @Override
    public void signOut() {
        username = null;
        uid = null;
    }

    @Override
    public Task<Void> updateEmail(String email) {
        authMap.get(uid).setEmail(email);
        return new FakeSuccessTask(null);
    }

    private void delete(String uid) {
        authMap.remove(uid);
    }
}

@SuppressLint("ParcelCreator")
class FakeFirebaseUser extends FirebaseUser {
    private String uid;
    private String email;
    private String password;
    private OnDeleteCallback onDelete;

    public FakeFirebaseUser(String uid, String email, String password, OnDeleteCallback onDelete) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.onDelete = onDelete;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public Task<Void> delete() {
        onDelete.run(uid);
        return new FakeSuccessTask(null);
    }

    @NonNull
    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Nullable
    @Override
    public List<String> zza() {
        return null;
    }

    @NonNull
    @Override
    public List<? extends UserInfo> getProviderData() {
        return null;
    }

    @NonNull
    @Override
    public FirebaseUser zza(@NonNull List<? extends UserInfo> list) {
        return null;
    }

    @Override
    public FirebaseUser zzb() {
        return null;
    }

    @NonNull
    @Override
    public FirebaseApp zzc() {
        return null;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public Uri getPhotoUrl() {
        return null;
    }

    public String getEmail() {
        return email;
    }

    @Nullable
    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }

    @Nullable
    @Override
    public String getTenantId() {
        return null;
    }

    @NonNull
    @Override
    public zzff zzd() {
        return null;
    }

    @Override
    public void zza(@NonNull zzff zzff) {

    }

    @NonNull
    @Override
    public String zze() {
        return null;
    }

    @NonNull
    @Override
    public String zzf() {
        return null;
    }

    @Nullable
    @Override
    public FirebaseUserMetadata getMetadata() {
        return null;
    }

    @NonNull
    @Override
    public MultiFactor getMultiFactor() {
        return null;
    }

    @Override
    public void zzb(List<MultiFactorInfo> list) {

    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String newEmail) {
        email = newEmail;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

@SuppressLint("ParcelCreator")
class FakeAuthResult implements AuthResult {
    private FirebaseUser user;

    public FakeAuthResult(FirebaseUser user) {
        this.user = user;
    }

    @Nullable
    @Override
    public FirebaseUser getUser() {
        return user;
    }

    @Nullable
    @Override
    public AdditionalUserInfo getAdditionalUserInfo() {
        return null;
    }

    @Nullable
    @Override
    public AuthCredential getCredential() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}