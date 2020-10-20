package com.cmput301f20t21.bookfriends.ui.library;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LibraryTabCollectionAdapter extends FragmentStateAdapter {

    private static final int tabCount = 2;

    public LibraryTabCollectionAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        LibraryViewModel.TabType tabType = LibraryViewModel.getTabTypeFromPosition(position);
        if (tabType == null) return null;
        switch (tabType) {
            case MY_BOOKS: return new OwnedTabFragment();
            case BORROWED_BOOKS: return new BorrowedTabFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }

}
