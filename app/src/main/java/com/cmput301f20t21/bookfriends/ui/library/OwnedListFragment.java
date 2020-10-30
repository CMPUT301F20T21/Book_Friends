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
import com.cmput301f20t21.bookfriends.datatransfer.BookDataTransferHelper;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.ui.add.AddEditActivity;
import com.cmput301f20t21.bookfriends.ui.request.RequestActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class OwnedListFragment extends Fragment {
    public static final String BOOK_ACTION_KEY = "com.cmput301f20t21.bookfriends.BOOK_ACTION";
    public static final String VIEW_REQUEST_KEY = "com.cmput301f20t21.bookfriends.VIEW_REQUEST";

    private OwnedViewModel vm;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int editBookIndex = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(OwnedViewModel.class);
        View root = inflater.inflate(R.layout.owned_list_book, container, false);
        final FloatingActionButton addBookButton = root.findViewById(R.id.add_button);

        addBookButton.setOnClickListener(
                view -> openAddEditActivity(null)
        );

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BOOK_ACTION.ADD.getCode()) {
                Book book = BookDataTransferHelper.receive(getActivity().getApplicationContext());
                List<Book> books = vm.getBooks().getValue();
                if (books != null) {
                    books.add(book);
                    mAdapter.notifyItemInserted(books.size() - 1);
                }
                Toast.makeText(getActivity(), getString(R.string.add_book_successful), Toast.LENGTH_SHORT).show();
            } else if (requestCode == BOOK_ACTION.EDIT.getCode()) {
                Book book = BookDataTransferHelper.receive(getActivity().getApplicationContext());
                List<Book> books = vm.getBooks().getValue();
                if (books != null && editBookIndex >= 0) {
                    books.set(editBookIndex, book);
                    mAdapter.notifyItemChanged(editBookIndex);
                    editBookIndex = -1;
                }
                Toast.makeText(getActivity(), getString(R.string.edit_book_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * function allows user to jump into the add/edit screen when click on the floating button
     */
    private void openAddEditActivity(@Nullable Book book) {
        Intent intent = new Intent(this.getActivity(), AddEditActivity.class);
        if (book == null) {
            intent.putExtra(BOOK_ACTION_KEY, BOOK_ACTION.ADD);
            startActivityForResult(intent, BOOK_ACTION.ADD.getCode());
        } else {
            intent.putExtra(BOOK_ACTION_KEY, BOOK_ACTION.EDIT);
            BookDataTransferHelper.transfer(book, getActivity().getApplicationContext());
            startActivityForResult(intent, BOOK_ACTION.EDIT.getCode());
        }
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
        recyclerView.setAdapter(new OwnedListAdapter(new ArrayList<>(), this::onItemClick, this::onDeleteBook, this::onViewRequests));

        vm.getBooks().observe(getViewLifecycleOwner(), (List<Book> books) -> {
            mAdapter = new OwnedListAdapter(books, this::onItemClick, this::onDeleteBook, this::onViewRequests);
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

    public void onItemClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            List<Book> books = vm.getBooks().getValue();
            if (books != null) {
                Book book = books.get(position);
                editBookIndex = position;
                openAddEditActivity(book);
            }
        }
    }

    public void onDeleteBook(Book book) {
        vm.deleteBook(book, this::onDeleteBookSuccess, this::onDeleteBookFail);
    }

    private void onDeleteBookFail() {
        Toast.makeText(getActivity(), getString(R.string.fail_to_delete_book), Toast.LENGTH_SHORT).show();
    }

    private void onDeleteBookSuccess(Book book) {
        Toast.makeText(getActivity(), getString(R.string.delete_book_successful), Toast.LENGTH_SHORT).show();
        List<Book> books = vm.getBooks().getValue();
        if (books != null) {
            books.remove(book);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * when user click on view requests of a book
     * go to request activity
     * @param bookId is passed to that activity to retrieving information from FireStore
     */
    private void onViewRequests(String bookId) {
        Intent intent = new Intent(this.getActivity(), RequestActivity.class);
        intent.putExtra(VIEW_REQUEST_KEY, bookId);
        startActivity(intent);
    }
}

