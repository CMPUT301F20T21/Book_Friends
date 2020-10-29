package com.cmput301f20t21.bookfriends.ui.library;

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

import java.util.ArrayList;
import java.util.List;

public class BorrowedListFragment extends Fragment {
    private BorrowedViewModel vm;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(BorrowedViewModel.class);
        return inflater.inflate(R.layout.borrowed_list_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.borrowed_recycler_list_book);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        vm.getBooks().observe(getViewLifecycleOwner(), (List<Book> books) -> {
            adapter = new BorrowedListAdapter(books);
            recyclerView.setAdapter(adapter);
        });

        vm.getUpdatedPosition().observe(getViewLifecycleOwner(), (Integer pos) -> {
            if (adapter != null) {
                adapter.notifyItemChanged(pos);
            }
        });
    }
}

