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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class OwnedListFragment extends Fragment {
    public static final String BOOK_ACTION_KEY = "com.cmput301f20t21.bookfriends.BOOK_ACTION";
    public static final String BOOK_EDIT_KEY = "com.cmput301f20t21.bookfriends.BOOK_EDIT";

    private OwnedViewModel vm;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.owned_recycler_list_book);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OwnedListAdapter(vm.getBooks().getValue(), this::onItemClick, this::onDeleteBook);
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

    public void onItemClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Book book = vm.getBookByIndex(position);
            openAddEditActivity(book);
        }
    }

    public void onDeleteBook(Book book) {
        vm.deleteBook(book);
    }
}

