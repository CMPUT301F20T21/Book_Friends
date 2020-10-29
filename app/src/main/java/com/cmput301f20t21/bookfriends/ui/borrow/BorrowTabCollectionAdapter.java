package com.cmput301f20t21.bookfriends.ui.borrow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BorrowTabCollectionAdapter extends FragmentStateAdapter {
    public enum TabType {
        Requested_BOOKS,
        Accepted_BOOKS
    }

    public static TabType getTabTypeFromPosition (int position) {
        switch (position) {
            case 0: return TabType.Requested_BOOKS;
            case 1: return TabType.Accepted_BOOKS;
            default: return null;
        }
    }

    private static final int tabCount = 2;

    public BorrowTabCollectionAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        TabType tabType = getTabTypeFromPosition(position);
        if (tabType == null) return null;
        switch (tabType) {
            case Requested_BOOKS: return new RequestedListFragment();
            case Accepted_BOOKS: return new AcceptedListFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }

}
