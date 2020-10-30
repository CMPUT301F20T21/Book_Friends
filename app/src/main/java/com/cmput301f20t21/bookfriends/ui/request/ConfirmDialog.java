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

import com.cmput301f20t21.bookfriends.R;

import java.util.List;

public class ConfirmDialog extends AppCompatDialogFragment {
    private ConfirmDialogListener confirmDialogListener;
    private String requesterId;
    private List<String> idsToRemove;

    public ConfirmDialog(String id, List<String> idsToRemove) {
        this.requesterId = id;
        this.idsToRemove = idsToRemove;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.confirm_dialog, null);
        builder.setView(v)
                .setTitle("Accept this request?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // does not do anything
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        confirmDialogListener.setConfirm(requesterId, idsToRemove);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            confirmDialogListener = (ConfirmDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement ConfirmDialogListener");
        }
    }

    public interface ConfirmDialogListener {
        void setConfirm(String id, List<String> idsToRemove);
    }
}
