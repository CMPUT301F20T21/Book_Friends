package com.cmput301f20t21.bookfriends.ui.request;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Request;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private ArrayList<Request> requestItems;
    private OnItemClickListener listener;
    public interface OnItemClickListener{
        void onRejectClick(int position);
        void onAcceptClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button acceptButton;
        public Button rejectButton;
        public ViewHolder(View v, final OnItemClickListener listener) {
            super(v);
            textView = v.findViewById(R.id.request_text_view);
            acceptButton = v.findViewById(R.id.accept_button);
            rejectButton = v.findViewById(R.id.reject_button);

            acceptButton.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAcceptClick(position);
                    }
                }
            });

            rejectButton.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRejectClick(position);
                    }
                }
            });
        }
    }

    public RequestAdapter(ArrayList<Request> requestItems) {
        this.requestItems = requestItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);

        ViewHolder r = new ViewHolder(v, listener);
        return r;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request currentItem = requestItems.get(position);
        holder.textView.setText(holder.itemView.getContext().getResources().getString(R.string.ask_to_borrow, currentItem.getRequester()));
    }

    @Override
    public int getItemCount() {
        return requestItems.size();
    }
}
