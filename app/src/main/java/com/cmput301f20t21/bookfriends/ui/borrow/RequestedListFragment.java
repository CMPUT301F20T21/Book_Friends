package com.cmput301f20t21.bookfriends.ui.borrow;

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
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

import java.util.List;

public class RequestedListFragment extends Fragment {
    private RequestedViewModel vm;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(RequestedViewModel.class);
        return inflater.inflate(R.layout.list_requested_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = (RecyclerView) view.findViewById(R.id.request_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        adapter = new RequestedListAdapter(vm.getBooks().getValue(), this::onItemClick);
        recyclerView.setAdapter(adapter);

        vm.getBooks().observe(getViewLifecycleOwner(), (List<Book> books) -> adapter.notifyDataSetChanged());

        vm.getErrorMessage().observe(getViewLifecycleOwner(), (BOOK_ERROR error) -> {
            if (error == BOOK_ERROR.FAIL_TO_GET_BOOKS) {
                Toast.makeText(getActivity(), getString(R.string.fail_to_get_books), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDetailActivity(Book book){
        Intent intent = new Intent(this.getActivity(), DetailRequestedActivity.class);
        intent.putExtra(BaseDetailActivity.BOOK_DATA_KEY, book);
        startActivity(intent);
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
}

