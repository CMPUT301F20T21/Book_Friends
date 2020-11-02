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
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.cmput301f20t21.bookfriends.R;

public class BrowseSearchFragment extends Fragment {
    private BrowseViewModel browseSearch;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private SearchView searchView;
    private FragmentContainerView searchContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        browseSearch = new ViewModelProvider(this).get(BrowseViewModel.class);
        setHasOptionsMenu(true);
        // set the page with original book list for now
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.browse_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        searchContainer = getActivity().findViewById(R.id.browse_search_list_container_fragment);
        searchView = (SearchView) menu.findItem(R.id.book_search_bar).getActionView();
        // show or hide the searched book list fragment (container)
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchContainer.setVisibility(View.VISIBLE);
            } else {
                searchContainer.setVisibility(View.GONE);
            }
        });
    }
}
