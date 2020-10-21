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
    private TextView existEmail;
    private TextView existPhone;
    public ProfileEditListener listener;

    @NotNull
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_editprofile,container,false);
        editEmail = view.findViewById(R.id.edit_email);
        editPhone = view.findViewById(R.id.edit_phone);
        cancel = view.findViewById(R.id.text_cancel);
        confirm = view.findViewById(R.id.text_confirm);
        existEmail = view.findViewById(R.id.email);
        existPhone = view.findViewById(R.id.phone);

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

                if (isEmailValid(inputEmail) && (isPhoneValid(inputPhone))){
                    listener.editing(inputEmail, inputPhone);
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
            listener = (ProfileEditListener)getTargetFragment();
        }catch (ClassCastException e){
           throw new ClassCastException("Need Implement ProfileEditListener");
        }
    }

    public interface ProfileEditListener{
        void editing(String email, String phone);
    }

    // check if entered email address is valid or not
    // if not, show message
    boolean isEmailValid(CharSequence email) {
        boolean valid =  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!valid && email.length()!= 0){
            editEmail.setError("Email address is INVALID.");
            return false;
        }
        else{
            editEmail.setError(null);
            return true;
        }
    }
    /// check if entered phone number is valid or not
    // if not, show message
    boolean isPhoneValid(String phone) {
        if (phone.length()!=10 && phone.length()!=0){
            editPhone.setError("Phone number is INVALID.");
            return false;
        }
        else{
            editEmail.setError(null);
            return true;
        }
    }

}
