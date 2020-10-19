package com.cmput301f20t21.bookfriends.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.cmput301f20t21.bookfriends.R;

import org.jetbrains.annotations.NotNull;

public class ProfileEditDialog extends DialogFragment {
    private EditText editEmail,editPhone;
    private TextView cancel, confirm;
    public ProfileEditListener listener;

    @NotNull
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_editprofile,container,false);
        editEmail = view.findViewById(R.id.editEmail);
        editPhone = view.findViewById(R.id.editPhone);
        cancel = view.findViewById(R.id.textCancel);
        confirm = view.findViewById(R.id.textConfirm);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.button_round_corner);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO check edittext's text is email address or not
                String input_email = editEmail.getText().toString();
                String input_phone = editPhone.getText().toString();
                listener.editing(input_email,input_phone);
                getDialog().dismiss();
            }
        });
        return view;

    }

   @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (ProfileEditListener)getTargetFragment();
        }catch (ClassCastException e){
           throw new ClassCastException("Need Implement ProfileEditListener");
        }
    }

    public interface ProfileEditListener{
        void editing(String email, String phone);
    }
}
