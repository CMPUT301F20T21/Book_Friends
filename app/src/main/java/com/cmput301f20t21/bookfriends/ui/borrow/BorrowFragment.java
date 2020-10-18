package com.cmput301f20t21.bookfriends.ui.borrow;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f20t21.bookfriends.R;

public class BorrowFragment extends Fragment {

    private BorrowViewModel mViewModel;

    public static BorrowFragment newInstance() {
        return new BorrowFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_borrow, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BorrowViewModel.class);
        // TODO: Use the ViewModel
    }

}