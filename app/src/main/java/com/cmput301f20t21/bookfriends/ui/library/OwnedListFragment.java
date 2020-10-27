package com.cmput301f20t21.bookfriends.ui.library;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class OwnedListFragment extends Fragment {
    private OwnedViewModel vm;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Book> books = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(OwnedViewModel.class);
        return inflater.inflate(R.layout.list_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_list_book);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        vm.getBooks(this::onGetBookSuccess, this::onGetBookFail);
    }

    public void onGetBookSuccess(ArrayList<Book> books) {
        this.books = books;
        mAdapter = new OwnedListAdapter(this.books);
        recyclerView.setAdapter(mAdapter);
        for (Book book : books) {
            vm.getBooksImage(book, this::onGetImageSuccess);
        }
    }

    public void onGetBookFail() {
        Toast.makeText(getContext(), getString(R.string.fail_to_get_books), Toast.LENGTH_SHORT).show();
    }

    public void onGetImageSuccess(Book book, @Nullable Uri imageUri) {
        if (imageUri != null) {
            book.setImageUri(imageUri);
            mAdapter.notifyDataSetChanged();
        }
    }

}

