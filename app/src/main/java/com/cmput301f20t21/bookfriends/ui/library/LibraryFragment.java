package com.cmput301f20t21.bookfriends.ui.library;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f20t21.bookfriends.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.cmput301f20t21.bookfriends.ui.add.AddEditActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LibraryFragment extends Fragment {

    private LibraryTabCollectionAdapter tabsAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        final FloatingActionButton addBookButton = root.findViewById(R.id.add_button);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddEditActivity();
            }
        });
        return root;
    }

    /**
     * function allows user to jump into the add/edit screen when click on the floating button
     */
    private void openAddEditActivity() {
        Intent intent = new Intent(this.getActivity(), AddEditActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabsAdapter = new LibraryTabCollectionAdapter(this);

        viewPager = view.findViewById(R.id.viewpager2_library);
        tabLayout = view.findViewById(R.id.tab_layout_library);

        viewPager.setAdapter(tabsAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(getResources().getString(R.string.tab_title_my_library));
                    break;
                case 1:
                    tab.setText(getResources().getString(R.string.tab_title_borrowed_library));
                    break;
                default: break;
            }
        }).attach();

    }
}