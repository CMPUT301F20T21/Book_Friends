package com.cmput301f20t21.bookfriends.ui.request;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private ArrayList<RequestItem> requestDataList;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_requests_activity);
        createExample();
        buildRecyclerView();
    }

    public void createExample() {
        requestDataList = new ArrayList<>();
        requestDataList.add(new RequestItem("Test1"));
        requestDataList.add(new RequestItem("Test2"));
        requestDataList.add(new RequestItem("Test3"));
        requestDataList.add(new RequestItem("Test4"));
        requestDataList.add(new RequestItem("Test5"));
        requestDataList.add(new RequestItem("Test6"));
        requestDataList.add(new RequestItem("Test7"));
        requestDataList.add(new RequestItem("Test8"));
        requestDataList.add(new RequestItem("Test9"));
        requestDataList.add(new RequestItem("Test10"));
        requestDataList.add(new RequestItem("Test11"));
        requestDataList.add(new RequestItem("Test12"));
        requestDataList.add(new RequestItem("Test13"));
    }

    /**
     * Function to set view by ID, set adapter and build recycler view
     */
    public void buildRecyclerView() {
        recyclerView = findViewById(R.id.request_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        requestAdapter = new RequestAdapter(requestDataList);
        recyclerView.setAdapter(requestAdapter);

        requestAdapter.setOnItemClickLisener(new RequestAdapter.OnItemClickListener() {
            @Override
            public void onRejectClick(int position) {
                removeItem(position);
            }

            @Override
            public void onAcceptClick(int position) {
                acceptItem(position);
            }
        });
    }

    /**
     * function to remove an item when we click on Reject button
     * @param position that needs removing
     */
    public void removeItem(int position) {
        requestDataList.remove(position);
        requestAdapter.notifyItemRemoved(position);
    }

    /**
     * function to accept one item and remove all other items when click on Accept button
     * @param position that we accept the item
     */
    public void acceptItem(int position) {
        int size = requestDataList.size();
        if (size > 0) {
            requestDataList.subList(0, size).clear();
            requestAdapter.notifyItemRangeRemoved(0, size);
        }
    }
}
