package com.cmput301f20t21.bookfriends.ui.request;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.ui.library.OwnedListFragment;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity implements ConfirmDialog.ConfirmDialogListener {
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
//    private final ArrayList<Request> requestDataList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView descriptionTextView;
    private TextView bookStatus;
    private ImageView bookImage;

    private RequestViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_requests_activity);

        // bind the text view
        titleTextView = findViewById(R.id.title_text_view);
        authorTextView = findViewById(R.id.author_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        bookStatus = findViewById(R.id.status_text_view);
        bookImage = findViewById(R.id.book_image_view);

        vm = new ViewModelProvider(this).get(RequestViewModel.class);

        // getting book ID from previous activity
        String bookId = getIntent().getStringExtra(OwnedListFragment.VIEW_REQUEST_KEY);
        displayBookInfo(bookId);
        // set up the back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buildRecyclerView(bookId);
    }

    /**
     * display book information by getting it from FireStore based on bookId
     * @param bookId
     */
    public void displayBookInfo(String bookId) {
        // since the data is mutable live, set the observer so the content will change accordingly
        vm.getBook(bookId).observe(this, book -> {
            titleTextView.setText(book.getTitle());
            authorTextView.setText(book.getAuthor());
            descriptionTextView.setText(book.getDescription());
            bookStatus.setText(book.getBookStatus().toString());
        });

        // also getting the image
        vm.getImageUri().observe(this, uri -> {
            Glide.with(this).load(uri).into(bookImage);
        });
    }

    /**
     * setup the back button on the title
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Function to set view by ID, set adapter and build recycler view
     */
    public void buildRecyclerView(String bookId) {
        recyclerView = findViewById(R.id.request_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        vm.getRequesters(bookId).observe(this, requests -> {
            requestAdapter = new RequestAdapter((ArrayList<Request>) requests);
            recyclerView.setAdapter(requestAdapter);
        });

        vm.getUpdatedPosition().observe(this, (Integer pos) -> {
            if (requestAdapter != null) {
                requestAdapter.notifyItemChanged(pos);
            }
        });

//        requestAdapter.setOnItemClickLisener(new RequestAdapter.OnItemClickListener() {
//            @Override
//            public void onRejectClick(int position) {
////                removeItem(position);
//            }
//
//            @Override
//            public void onAcceptClick(int position) {
////                openDialog(position);
//            }
//        });
    }

//    /**
//     * function to remove an item when we click on Reject button
//     * @param position that needs removing
//     */
//    public void removeItem(int position) {
//        Request request = requestDataList.get(position);
//        vm.removeRequest(request.getId());
//        requestDataList.remove(position);
//        requestAdapter.notifyItemRemoved(position);
//    }
//
//    /**
//     * When user click on the accept button
//     * popup a dialog to prompt user about their action:
//     * accept one item and remove all other items
//     * @param position that we accept the item
//     */
//    public void openDialog(int position) {
//        String acceptedId = requestDataList.get(position).getId();
//        List<String> idsToRemove = new ArrayList<>();
//        for (int i = 0; i < requestDataList.size();i++) {
//            idsToRemove.add(requestDataList.get(i).getId());
//        }
//        idsToRemove.remove(acceptedId);
//        ConfirmDialog confirmDialog = new ConfirmDialog(acceptedId, idsToRemove);
//        confirmDialog.show(getSupportFragmentManager(), "Confirm Dialog");
//    }
//
//    /**
//     * function is called whenever the user confirms to accept a request
//     * remove all other requests
//     */
    @Override
    public void setConfirm(String id, List<String> idsToRemove) {
//        vm.acceptRequest(id);
//        int size = requestDataList.size();
//        if (size > 0) {
//            vm.removeAllRequest(idsToRemove);
//            requestDataList.subList(0, size).clear();
//            requestAdapter.notifyItemRangeRemoved(0, size);
//        }
    }
}
