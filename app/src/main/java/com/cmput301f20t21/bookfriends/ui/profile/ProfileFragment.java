package com.cmput301f20t21.bookfriends.ui.profile;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment implements ProfileEditDialog.EditListener {
    private TextView emailAddress;
    private TextView phoneNumber;
    private ImageView editProfile;
    private TextView name;
    private ProfileSearchFragment searchedUserListFragment;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        inflateSearchedList();

        emailAddress = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phone);
        editProfile = view.findViewById(R.id.image_edit);
        name = view.findViewById(R.id.username);

        // get the login information from firebase
        User firebaseUser = AuthService.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();

            //document reference
            final DocumentReference docReference = FirebaseFirestore.getInstance().collection("users").document(userId);
            docReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            String username = doc.getString("username");
                            String email = doc.getString("email");
                            name.setText(username);
                            emailAddress.setText(email);
                        }
                    }
                }
            });
        }

        //click on the logout button, bring back to the login activity
        Button logout = view.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //logout from the firebase
                FirebaseAuth.getInstance().signOut();
            }
        });

        //click on the the edit icon, bring to another fragment to edit
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileEditDialog dialog = new ProfileEditDialog();
                //pass value to the dialogFragment
                Bundle args = new Bundle();
                args.putString("email", emailAddress.getText().toString());
                args.putString("phone", phoneNumber.getText().toString());
                dialog.setArguments(args);
                dialog.setTargetFragment(ProfileFragment.this, 1
                );
                dialog.show(getParentFragment().getChildFragmentManager(), "edit_profile");

            }
        });
        return view;
    }

    @Override
    public void onEdit(String email, String phone) {
        emailAddress.setText(email);
        phoneNumber.setText(phone);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void inflateSearchedList() {
        fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        searchedUserListFragment = new ProfileSearchFragment();
        fragmentTransaction
                .add(R.id.profile_search_list_container_fragment, searchedUserListFragment)
                .commit();
    }
}