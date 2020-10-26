package com.cmput301f20t21.bookfriends.ui.request;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestHolder> {
    private String[] dataSet;
    public static class RequestHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RequestHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    public RequestAdapter(String[] myData) {
        dataSet = myData;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);

        RequestHolder r = new RequestHolder(v);
        return r;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        holder.textView.setText(dataSet[position]);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
