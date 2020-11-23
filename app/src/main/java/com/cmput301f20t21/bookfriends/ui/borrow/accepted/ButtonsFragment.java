package com.cmput301f20t21.bookfriends.ui.borrow.accepted;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.ui.component.DetailButtonModel;

import java.util.ArrayList;
import java.util.List;

public class ButtonsFragment extends Fragment {
    private AcceptedDetailViewModel vm;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public ButtonsFragment(AcceptedDetailViewModel vm) {
        this.vm = vm;
    }

    private ArrayList<DetailButtonModel> getButtons(@NonNull View root) {
        ArrayList<DetailButtonModel> buttonModels = new ArrayList<>();
        buttonModels.add(
                new DetailButtonModel(
                        "View meetup location",
                        "see where the owner wants to meet",
                        (view) -> {
                            Log.e("bfriends", "pressed button");
                        }
                ));
        return buttonModels;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View list = inflater.inflate(R.layout.list_detail_buttons, container, false);
        return list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.detail_buttons_recycler);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<DetailButtonModel> buttons = getButtons(view);
        adapter = new ButtonsAdapter(buttons);
        recyclerView.setAdapter(adapter);
    }
}
