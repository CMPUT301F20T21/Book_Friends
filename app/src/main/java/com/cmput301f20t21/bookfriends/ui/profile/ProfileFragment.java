package com.cmput301f20t21.bookfriends.ui.profile;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.ui.login.LoginActivity;


public class ProfileFragment extends Fragment implements ProfileEditDialog.ProfileEditListener {

    private ProfileViewModel mViewModel;
    private TextView emailAddress;
    private TextView phoneNumber;
    private ImageView editProfile;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // TODO for other profile just use .setVisibility(View.GONE); to hide login button and edit icon
        emailAddress = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phone);
        editProfile = view.findViewById(R.id.image_edit);
        //click on the logout button, bring back to the login activity
        Button logout = view.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //click on the the edit icon, bring to another fragment to edit
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileEditDialog dialog = new ProfileEditDialog();
                dialog.setTargetFragment(ProfileFragment.this,1);
                dialog.show(getParentFragment().getChildFragmentManager(),"edit_profile");

            }
        });
        return view;
    }

    @Override
    public void editing(String email, String phone) {
        emailAddress.setText(email);
        phoneNumber.setText(phone);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }


}