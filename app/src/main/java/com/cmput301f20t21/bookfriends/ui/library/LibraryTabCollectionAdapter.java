package com.cmput301f20t21.bookfriends.ui.library;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cmput301f20t21.bookfriends.ui.library.borrowed.BorrowedListFragment;
import com.cmput301f20t21.bookfriends.ui.library.owned.OwnedListFragment;

public class LibraryTabCollectionAdapter extends FragmentStateAdapter {
    public enum TabType {
        MY_BOOKS,
        BORROWED_BOOKS
    }

    public static TabType getTabTypeFromPosition (int position) {
        switch (position) {
            case 0: return TabType.MY_BOOKS;
            case 1: return TabType.BORROWED_BOOKS;
            default: return null;
        }
    }

    private static final int tabCount = 2;

    public LibraryTabCollectionAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        TabType tabType = getTabTypeFromPosition(position);
        if (tabType == null) return null;
        switch (tabType) {
            case MY_BOOKS: return new OwnedListFragment();
            case BORROWED_BOOKS: return new BorrowedListFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }

}
