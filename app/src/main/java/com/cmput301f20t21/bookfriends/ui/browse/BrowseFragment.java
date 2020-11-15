package com.cmput301f20t21.bookfriends.ui.browse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User can see the list of all of the books and search what they need
 */
public class BrowseFragment extends Fragment {
    private BrowseViewModel vm;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(BrowseViewModel.class);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.browse_book_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        adapter = new AvailableBookListAdapter(vm.getBooks().getValue(), this::onItemClick);
        recyclerView.setAdapter(adapter);

        vm.getBooks().observe(getViewLifecycleOwner(), (List<Book> books) -> adapter.notifyDataSetChanged());

        vm.getUpdatedPosition().observe(getViewLifecycleOwner(), updatedPosition -> adapter.notifyItemChanged(updatedPosition));

        vm.getErrorMessage().observe(getViewLifecycleOwner(), (BOOK_ERROR error) -> {
            if (error == BOOK_ERROR.FAIL_TO_GET_BOOKS) {
                Toast.makeText(getActivity(), getString(R.string.fail_to_get_books), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDetailActivity(Book book) {
        Intent intent = new Intent(this.getActivity(), BrowseDetailActivity.class);
        intent.putExtra(BaseDetailActivity.BOOK_DATA_KEY, book);
        startActivity(intent);
    }


    /**
     * called when the user clicks on one of the books
     *
     * @param position the book's position
     */
    public void onItemClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Book book = vm.getBookByIndex(position);
            openDetailActivity(book);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.browse_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        searchView = (SearchView) menu.findItem(R.id.book_search_bar).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vm.filterBookWithKeyword(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                vm.filterBookWithKeyword(newText);
                return true;
            }
        });
    }
}
