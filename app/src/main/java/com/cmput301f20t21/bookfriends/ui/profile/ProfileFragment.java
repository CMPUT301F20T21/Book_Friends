package com.cmput301f20t21.bookfriends.ui.profile;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.ui.login.LoginActivity;

public class ProfileFragment extends Fragment implements ProfileEditDialog.EditListener {
    private TextView emailAddress;
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
        editProfile = view.findViewById(R.id.image_edit);
        name = view.findViewById(R.id.username);


        // get the login information from firebase
        User firebaseUser = AuthRepository.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            String username = firebaseUser.getUsername();
            String email = firebaseUser.getEmail();
            name.setText(username);
            emailAddress.setText(email);
        }


        //click on the logout button, bring back to the login activity
        Button logout = view.findViewById(R.id.logout_button);
        logout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //logout from the firebase
            AuthRepository.getInstance().signOut();
        });

        //click on the the edit icon, bring to another fragment to edit
        editProfile.setOnClickListener(v -> {
            ProfileEditDialog dialog = new ProfileEditDialog();
            //pass value to the dialogFragment
            Bundle args = new Bundle();
            args.putString("email", emailAddress.getText().toString());
            dialog.setArguments(args);
            dialog.setTargetFragment(ProfileFragment.this, 1
            );
            dialog.show(getParentFragment().getChildFragmentManager(), "edit_profile");

        });
        return view;
    }

    @Override
    public void onEdit(String email) {
        emailAddress.setText(email);
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