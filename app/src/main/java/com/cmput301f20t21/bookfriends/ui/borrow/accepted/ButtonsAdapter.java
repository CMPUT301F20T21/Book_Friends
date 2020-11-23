package com.cmput301f20t21.bookfriends.ui.borrow.accepted;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.ui.component.DetailButtonModel;

import java.util.List;

public class ButtonsAdapter extends RecyclerView.Adapter<ButtonsAdapter.ViewHolder> {
    private final List<DetailButtonModel> buttonModels;

    public ButtonsAdapter(List<DetailButtonModel> buttonModels) {
        this.buttonModels = buttonModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_button, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(buttonModels.get(position));
    }

    @Override
    public int getItemCount() {
        return buttonModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView body;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.detail_button_component_title);
            body = itemView.findViewById(R.id.detail_button_component_body);
        }

        public void onBind(DetailButtonModel buttonModel) {
            title.setText(buttonModel.getTitle());
            body.setText(buttonModel.getBody());
            itemView.setOnClickListener(buttonModel.getOnClick()::run);
        }
    }
}
