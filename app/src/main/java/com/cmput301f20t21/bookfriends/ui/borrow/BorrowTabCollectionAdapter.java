/*
 * BorrowTabCollectionAdapter.java
 * Version: 1.0
 * Date: October 20, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.borrow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cmput301f20t21.bookfriends.ui.borrow.accepted.AcceptedListFragment;
import com.cmput301f20t21.bookfriends.ui.borrow.requested.RequestedListFragment;

/**
 * {@link FragmentStateAdapter} for {@link BorrowFragment}
 */
public class BorrowTabCollectionAdapter extends FragmentStateAdapter {
    public enum TabType {
        REQUESTED_BOOKS,
        ACCEPTED_BOOKS
    }

    public static TabType getTabTypeFromPosition (int position) {
        switch (position) {
            case 0: return TabType.REQUESTED_BOOKS;
            case 1: return TabType.ACCEPTED_BOOKS;
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
            case REQUESTED_BOOKS: return new RequestedListFragment();
            case ACCEPTED_BOOKS: return new AcceptedListFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }

}
