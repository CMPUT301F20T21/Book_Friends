package com.cmput301f20t21.bookfriends.ui.request;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class RequestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter requestAdapter;
    ArrayList<String> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_requests_activity);
        recyclerView = findViewById(R.id.request_recycler_view);

        String [] requestText = {"Test 1", "Test 2"};
        dataList.addAll(Arrays.asList(requestText));
        requestAdapter = new RequestAdapter(requestText);

        recyclerView.setAdapter(requestAdapter);

    }
}
