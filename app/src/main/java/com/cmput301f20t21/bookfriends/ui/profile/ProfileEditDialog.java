package com.cmput301f20t21.bookfriends.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.cmput301f20t21.bookfriends.R;

import org.jetbrains.annotations.NotNull;

public class ProfileEditDialog extends DialogFragment {
    private EditText editEmail;
    private EditText editPhone;
    private TextView cancel;
    private TextView confirm;
    private EditListener listener;

    @NotNull
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_editprofile,container,false);
        editEmail = view.findViewById(R.id.edit_email);
        editPhone = view.findViewById(R.id.edit_phone);
        cancel = view.findViewById(R.id.text_cancel);
        confirm = view.findViewById(R.id.text_confirm);
        //set value from the fragment
        editEmail.setText(getArguments().getString("email"));
        editPhone.setText(getArguments().getString("phone"));


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
                String inputEmail = editEmail.getText().toString();
                String inputPhone = editPhone.getText().toString();
                isEmailValid(inputEmail);
                isPhoneValid(inputPhone);
                if (isEmailValid(inputEmail) && isPhoneValid(inputPhone)){
                    listener.onEdit(inputEmail, inputPhone);
                    getDialog().dismiss();
                }
            }
        });
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (EditListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException("Need Implement ProfileEditListener");
        }
    }

    public interface EditListener{
        void onEdit(String email, String phone);
    }

    // check if entered email address is valid or not
    // if not, show message
    boolean isEmailValid(CharSequence email) {
        if (email.length() == 0){
            editEmail.setError(getString(R.string.empty_email));
            return false;
        }
        boolean valid =  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!valid){
            editEmail.setError(getString(R.string.m_not_valid_email));
            return false;
        }
        return true;
    }
    /// check if entered phone number is valid or not
    // if not, show message
    boolean isPhoneValid(String phone) {
        if (phone.length() == 0){
            return true;
        }
        boolean valid =  android.util.Patterns.PHONE.matcher(phone).matches();
        if (!valid){
            editPhone.setError(getString(R.string.m_not_valid_phone));
            return false;
        }
        return true;
    }

}
