package com.cmput301f20t21.bookfriends.ui.borrow;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

public class RequestedListFragment extends Fragment {
    private RequestedViewModel mViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(RequestedViewModel.class);
        return inflater.inflate(R.layout.requested_list_book, container, false);
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
        mAdapter = new RequestedListAdapter(mViewModel.getBooks(), this::onItemClick);
        recyclerView.setAdapter(mAdapter);
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
            Book book = mViewModel.getBookByIndex(position);
            openDetailActivity(book);
        }
    }
}

