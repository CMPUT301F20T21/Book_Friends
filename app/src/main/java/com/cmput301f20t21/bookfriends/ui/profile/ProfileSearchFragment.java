/*
 * ProfileSearchFragment.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.User;

import java.util.ArrayList;

/**
 * Fragment for searching other user's profile
 */
public class ProfileSearchFragment extends Fragment {
    private ProfileViewModel vm;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private SearchView searchView;
    private FragmentContainerView searchContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ProfileViewModel.class);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.list_search_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.search_user_recycler);
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        vm.getSearchedUsers().observe(getViewLifecycleOwner(), (ArrayList<User> users) -> {
            adapter = new SearchedUserListAdapter(users, getContext());
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        searchContainer = getActivity().findViewById(R.id.profile_search_list_container_fragment);
        searchView = (SearchView) menu.findItem(R.id.profile_search_bar).getActionView();
        // show or hide the searched user list fragment (container)
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchContainer.setVisibility(View.VISIBLE);
            } else {
                searchContainer.setVisibility(View.GONE);
            }
        });

        // whenever the input update, let's search users
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vm.updateSearchQuery(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                vm.updateSearchQuery(newText);
                return false;
            }
        });
    }
}
