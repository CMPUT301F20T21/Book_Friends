package com.cmput301f20t21.bookfriends.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.User;

import java.util.ArrayList;

public class ProfileSearchFragment extends Fragment {
    private ProfileViewModel vm;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // use getActivity to share the same profile vm
        vm = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        return inflater.inflate(R.layout.list_search_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.search_user_recycler);
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        vm.getSearchedUsers().observe(getViewLifecycleOwner(), (ArrayList<User> users) -> {
            adapter = new SearchedUserListAdapter(users);
            recyclerView.setAdapter(adapter);
        });
    }
}
