package com.cmput301f20t21.bookfriends.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;

public class BrowseFragment extends Fragment {
    private BrowseViewModel bookViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter bookAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private FragmentContainerView searchContainer;
    private BrowseSearchFragment searchedBookListFragment;
    private FragmentManager fragmentManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
        bookViewModel = new ViewModelProvider(this).get(BrowseViewModel.class);
        inflateSearchedList();
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.browse_book_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        bookAdapter = new SearchedBookListAdapter(bookViewModel.getBooks());
        recyclerView.setAdapter(bookAdapter);
    }

    private void inflateSearchedList() {
        fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        searchedBookListFragment = new BrowseSearchFragment();
        fragmentTransaction
            .add(R.id.browse_search_list_container_fragment,searchedBookListFragment)
            .commit();
    }
}
