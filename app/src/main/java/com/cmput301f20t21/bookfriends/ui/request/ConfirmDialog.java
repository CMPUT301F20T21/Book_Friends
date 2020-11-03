package com.cmput301f20t21.bookfriends.ui.request;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;

import java.util.List;

public class ConfirmDialog extends AppCompatDialogFragment {
    private Integer position;

    public ConfirmDialog(Integer position) {
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.confirm_dialog, null);

        RequestViewModel vm = new ViewModelProvider(this).get(RequestViewModel.class);

        builder.setView(v)
                .setTitle(getString(R.string.accept_this_request))
                .setNegativeButton(getString(R.string.edit_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // does not do anything
                    }
                })
                .setPositiveButton(getString(R.string.edit_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        vm.acceptRequest(position);
                    }
                });
        return builder.create();
    }

}
