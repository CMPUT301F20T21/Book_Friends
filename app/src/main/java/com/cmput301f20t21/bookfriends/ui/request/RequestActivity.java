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
    }

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

            }
        });
    }

    public void removeItem(int position) {
        requestDataList.remove(position);
        requestAdapter.notifyItemRemoved(position);
    }

    public void acceptItem(int position) {

    }
}
