package com.cmput301f20t21.bookfriends.ui.profile;

import android.content.Context;
import android.content.Intent;
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
    private ArrayList<User> users;
    private Context context;

    public SearchedUserListAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchedUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searched_user, parent, false);
        return new ViewHolder(itemView, parent);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView usernameView;
        final ViewGroup parent;
        private User user;

        public ViewHolder(View v, ViewGroup parent) {
            super(v);
            this.parent = parent;
            this.usernameView = v.findViewById(R.id.searched_user_username);
            v.setOnClickListener(itemView -> {
                Intent intent = new Intent(parent.getContext(), ProfileViewUserActivity.class);
                intent.putExtra(ProfileViewUserActivity.UID_KEY, user.getUid());
                context.startActivity(intent);
            });
        }

        public void onBind(User user) {
            this.user = user;
            this.usernameView.setText(user.getUsername());
        }
    }
}
