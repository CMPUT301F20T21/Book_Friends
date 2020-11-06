package com.cmput301f20t21.bookfriends.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;

/**
 * User can see the list of all of the books and search what they need
 */
public class BrowseFragment extends Fragment {
    private BrowseViewModel bookViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter bookAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private FragmentManager fragmentManager;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
        bookViewModel = new ViewModelProvider(this).get(BrowseViewModel.class);
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.browse_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        searchView = (SearchView) menu.findItem(R.id.book_search_bar).getActionView();
    }
    private void inflateSearchedList() {
        fragmentManager = getChildFragmentManager();
    }
}
