/*
 * OwnedListFragment.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library;

import android.content.Intent;
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
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.ui.add.AddEditActivity;
import com.cmput301f20t21.bookfriends.ui.request.RequestActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class OwnedListFragment extends Fragment {
    public static final String BOOK_ACTION_KEY = "com.cmput301f20t21.bookfriends.BOOK_ACTION";
    public static final String BOOK_EDIT_KEY = "com.cmput301f20t21.bookfriends.BOOK_EDIT";
    public static final String VIEW_REQUEST_KEY = "com.cmput301f20t21.bookfriends.VIEW_REQUEST";

    private OwnedViewModel vm;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Called before creating the fragment view
     * @param inflater the layout inflater
     * @param container the view container
     * @param savedInstanceState the saved objects, should contain nothing for this fragment
     * @return the inflated view
     */
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

    /**
     * called after creating the fragment view
     * @param view the fragment's view
     * @param savedInstanceState the saved objects, should contain nothing for this fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.owned_recycler_list_book);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OwnedListAdapter(vm.getBooks().getValue(), this::onItemClick, this::onDeleteBook, this::onViewRequests);
        recyclerView.setAdapter(adapter);

        vm.getBooks().observe(getViewLifecycleOwner(), (List<Book> books) -> adapter.notifyDataSetChanged());

        vm.getUpdatedPosition().observe(getViewLifecycleOwner(), (Integer pos) -> adapter.notifyItemChanged(pos));

        vm.getErrorMessageObserver().observe(getViewLifecycleOwner(), (BOOK_ERROR error) -> {
            if (error == BOOK_ERROR.FAIL_TO_GET_BOOKS) {
                Toast.makeText(getActivity(), getString(R.string.fail_to_get_books), Toast.LENGTH_SHORT).show();
            } else if (error == BOOK_ERROR.FAIL_TO_DELETE_BOOK) {
                Toast.makeText(getActivity(), getString(R.string.fail_to_delete_book), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * called upon returning from the AddEditActivity, will add or update the book to the local data
     * @param requestCode the request code that starts the activity
     * @param resultCode the result code sent from the activity
     * @param data the intent data that contains the added or updated book
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BOOK_ACTION.ADD.getCode()) {
                Book book = data.getParcelableExtra(AddEditActivity.NEW_BOOK_INTENT_KEY);
                vm.addBook(book);
                Toast.makeText(getActivity(), getString(R.string.add_book_successful), Toast.LENGTH_SHORT).show();
            } else if (requestCode == BOOK_ACTION.EDIT.getCode()) {
                Book oldBook = data.getParcelableExtra(AddEditActivity.OLD_BOOK_INTENT_KEY);
                Book updatedBook = data.getParcelableExtra(AddEditActivity.UPDATED_BOOK_INTENT_KEY);

                vm.updateBook(oldBook, updatedBook);
                Toast.makeText(getActivity(), getString(R.string.edit_book_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * function allows user to jump into the add/edit screen when click on the floating button
     * @param book the book to edit, will be null if the action is add
     */
    private void openAddEditActivity(@Nullable Book book) {
        Intent intent = new Intent(this.getActivity(), AddEditActivity.class);
        if (book == null) {
            intent.putExtra(BOOK_ACTION_KEY, BOOK_ACTION.ADD);
            startActivityForResult(intent, BOOK_ACTION.ADD.getCode());
        } else {
            intent.putExtra(BOOK_ACTION_KEY, BOOK_ACTION.EDIT);
            intent.putExtra(BOOK_EDIT_KEY, book);
            startActivityForResult(intent, BOOK_ACTION.EDIT.getCode());
        }
    }
    private void openDetailActivity(Book book){
        Intent intent = new Intent(this.getActivity(), detailLibraryActivity.class);
        intent.putExtra(BOOK_ACTION_KEY, BOOK_ACTION.VIEW);
        intent.putExtra(VIEW_REQUEST_KEY,book);
        startActivityForResult(intent, BOOK_ACTION.VIEW.getCode());
    }


    /**
     * called when the user clicks on one of the books
     * @param position the book's position
     */
    public void onItemClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Book book = vm.getBookByIndex(position);
            openDetailActivity(book);
        }
    }

    /**
     * called when the user deletes one of the book
     * @param book the book to delete
     */
    public void onDeleteBook(Book book) {
        vm.deleteBook(book);
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

