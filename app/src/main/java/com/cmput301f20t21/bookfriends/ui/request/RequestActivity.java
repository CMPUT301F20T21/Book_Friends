package com.cmput301f20t21.bookfriends.ui.request;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.ui.library.OwnedListFragment;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity implements ConfirmDialog.ConfirmDialogListener {
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private final ArrayList<Request> requestDataList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView descriptionTextView;
    private TextView bookStatus;
    private ImageView bookImage;

    private RequestViewModel requestViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_requests_activity);

        // getting book ID from previous activity
        String bookId = getIntent().getStringExtra(OwnedListFragment.VIEW_REQUEST_KEY);
        displayBookInfo(bookId);
        displayRequest(bookId);
        // set up the back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buildRecyclerView();
    }

    /**
     * display book information by getting it from FireStore based on bookId
     * @param bookId
     */
    public void displayBookInfo(String bookId) {
        // bind the text view
        titleTextView = findViewById(R.id.title_text_view);
        authorTextView = findViewById(R.id.author_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        bookStatus = findViewById(R.id.status_text_view);
        bookImage = findViewById(R.id.book_image_view);

        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        // since the data is mutable live, set the observer so the content will change accordingly
        requestViewModel.getBook(bookId).observe(this, book -> {
            titleTextView.setText(book.getTitle());
            authorTextView.setText(book.getAuthor());
            descriptionTextView.setText(book.getDescription());
            bookStatus.setText(book.getBookStatus().toString());
        });

        // also getting the image
        requestViewModel.getImageUri().observe(this, uri -> {
            Glide.with(this).load(uri).into(bookImage);
        });
    }

    /**
     * display requesters of the book by getting them from FireStore based on bookId
     * @param bookId
     */
    public void displayRequest(String bookId) {
        requestViewModel.getRequesters(bookId).observe(this, requesters -> {
            if (requesters != null) {
                requestDataList.addAll(requesters);
                requestAdapter.notifyDataSetChanged();
            }
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
        Request request = requestDataList.get(position);
        requestViewModel.removeRequest(request.getId());
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
        String acceptedId = requestDataList.get(position).getId();
        List<String> idsToRemove = new ArrayList<>();
        for (int i = 0; i < requestDataList.size();i++) {
            idsToRemove.add(requestDataList.get(i).getId());
        }
        idsToRemove.remove(acceptedId);
        ConfirmDialog confirmDialog = new ConfirmDialog(acceptedId, idsToRemove);
        confirmDialog.show(getSupportFragmentManager(), "Confirm Dialog");
    }

    /**
     * function is called whenever the user confirms to accept a request
     * remove all other requests
     */
    @Override
    public void setConfirm(String id, List<String> idsToRemove) {
        requestViewModel.acceptRequest(id);
        int size = requestDataList.size();
        if (size > 0) {
            requestViewModel.removeAllRequest(idsToRemove);
            requestDataList.subList(0, size).clear();
            requestAdapter.notifyItemRangeRemoved(0, size);
        }
    }
}
