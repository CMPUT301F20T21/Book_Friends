package com.cmput301f20t21.bookfriends.ui.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.cmput301f20t21.bookfriends.serializer.UriDeserializer;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.ui.add.AddEditActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class OwnedListFragment extends Fragment {
    public static final String BOOK_ACTION_KEY = "com.cmput301f20t21.bookfriends.BOOK_ACTION";
    public static final String BOOK_OBJECT_KEY = "com.cmput301f20t21.bookfriends.BOOK_OBJECT";

    private OwnedViewModel vm;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(OwnedViewModel.class);
        View root = inflater.inflate(R.layout.owned_list_book, container, false);
        final FloatingActionButton addBookButton = root.findViewById(R.id.add_button);

        addBookButton.setOnClickListener(
                view -> openAddEditActivity()
        );

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BOOK_ACTION.ADD.getCode()) {
                SharedPreferences sharedPreference =
                        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                Gson gson = new GsonBuilder()
                                .registerTypeAdapter(Uri.class, new UriDeserializer())
                                .create();
                String json = sharedPreference.getString(BOOK_OBJECT_KEY, "");
                Book book = gson.fromJson(json, Book.class);
                List<Book> books = vm.getBooks().getValue();
                books.add(book);
                mAdapter.notifyItemInserted(books.size() - 1);
            }
        }
    }

    /**
     * function allows user to jump into the add/edit screen when click on the floating button
     */
    private void openAddEditActivity() {
        // TODO: Change the enum when calling the activity for editing
        Intent intent = new Intent(this.getActivity(), AddEditActivity.class);
        intent.putExtra(BOOK_ACTION_KEY, BOOK_ACTION.ADD);
        startActivityForResult(intent, BOOK_ACTION.ADD.getCode());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.owned_recycler_list_book);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // set a temporary adapter
        recyclerView.setAdapter(new OwnedListAdapter(new ArrayList<>(), this::onDeleteBook));

        vm.getBooks().observe(getViewLifecycleOwner(), (List<Book> books) -> {
            mAdapter = new OwnedListAdapter(books, this::onDeleteBook);
            recyclerView.setAdapter(mAdapter);
        });

        vm.getUpdatedPosition().observe(getViewLifecycleOwner(), (Integer pos) -> {
            if (mAdapter != null) {
                mAdapter.notifyItemChanged(pos);
            }
        });

        vm.getErrorMessageObserver().observe(getViewLifecycleOwner(), (BOOK_ERROR error) -> {
            if (error == BOOK_ERROR.FAIL_TO_GET_BOOKS) {
                Toast.makeText(getActivity(), getString(R.string.fail_to_get_books), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDeleteBook(Book book) {
        vm.deleteBook(book, this::onDeleteBookSuccess, this::onDeleteBookFail);
    }

    private void onDeleteBookFail() {
        Toast.makeText(getActivity(), getString(R.string.fail_to_delete_book), Toast.LENGTH_SHORT).show();
    }

    private void onDeleteBookSuccess(Book book) {
        Toast.makeText(getActivity(), getString(R.string.delete_book_successful), Toast.LENGTH_SHORT).show();
        // OPTION 1: delete from LiveData and notify adapter
        vm.getBooks().getValue().remove(book);
        mAdapter.notifyDataSetChanged();
        // OPTION 2: query again, don't need book object
        // vm.fetchBooks();
    }

}

