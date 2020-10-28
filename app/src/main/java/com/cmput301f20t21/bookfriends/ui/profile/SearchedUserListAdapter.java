package com.cmput301f20t21.bookfriends.ui.profile;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.ui.library.BaseBookListAdapter;

import java.util.ArrayList;

public class SearchedUserListAdapter extends RecyclerView.Adapter<SearchedUserListAdapter.ViewHolder> {
    ArrayList<User> users;
    public SearchedUserListAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public SearchedUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searched_user, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = this.users.get(position);
        holder.onBind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView usernameView;

        public ViewHolder(View v) {
            super(v);
            usernameView = v.findViewById(R.id.searched_user_username);
            v.setOnClickListener(itemView -> {

            });
        }

        public void onBind(User user) {
            this.usernameView.setText(user.getUsername());
        }
    }
}
