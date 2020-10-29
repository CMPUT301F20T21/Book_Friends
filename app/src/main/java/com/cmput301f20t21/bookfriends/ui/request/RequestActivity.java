package com.cmput301f20t21.bookfriends.ui.request;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.services.RequestService;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity implements ConfirmDialog.ConfirmDialogListener {
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private ArrayList<RequestItem> requestDataList;
    private RecyclerView.LayoutManager layoutManager;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView descriptionTextView;
    private TextView bookStatus;

    private RequestViewModel requestViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_requests_activity);
        titleTextView = findViewById(R.id.title_text_view);
        authorTextView = findViewById(R.id.author_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        bookStatus = findViewById(R.id.status_text_view);

        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        // TODO: getting book ID from previous activity
        String bookId = "RmJi5i1sav1B4fKQcNHP"; // temporary book ID
        requestViewModel.handleBookInfo(bookId, this::onSuccess, this::onFailure);

        titleTextView.setText(requestViewModel.getBookName());
        authorTextView.setText(requestViewModel.getAuthor());
        descriptionTextView.setText(requestViewModel.getDescription());
        bookStatus.setText(requestViewModel.getStatus());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createExample();
        buildRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createExample() {
        requestDataList = new ArrayList<>();
        requestDataList.add(new RequestItem("Lucas"));
        requestDataList.add(new RequestItem("Meillin"));
        requestDataList.add(new RequestItem("Khang"));
        requestDataList.add(new RequestItem("Ze Hui"));
        requestDataList.add(new RequestItem("Trung"));
        requestDataList.add(new RequestItem("Qi"));
        requestDataList.add(new RequestItem("John"));
        requestDataList.add(new RequestItem("Anna"));
        requestDataList.add(new RequestItem("Smith"));
        requestDataList.add(new RequestItem("Kevin"));
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
                openDialog(position);
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
     * When user click on the accept button
     * popup a dialog to prompt user about their action:
     * accept one item and remove all other items
     * @param position that we accept the item
     */
    public void openDialog(int position) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.show(getSupportFragmentManager(), "Confirm Dialog");
    }

    /**
     * function is called whenever the user confirms to accept a request
     * remove all other requests
     */
    @Override
    public void setConfirm() {
        int size = requestDataList.size();
        if (size > 0) {
            requestDataList.subList(0, size).clear();
            requestAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    public void onSuccess() {

    }

    public void onFailure() {

    }
}
