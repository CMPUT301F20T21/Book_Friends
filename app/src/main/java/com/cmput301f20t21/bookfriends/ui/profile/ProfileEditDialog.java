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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;

import org.jetbrains.annotations.NotNull;

public class ProfileEditDialog extends DialogFragment {
    private static final String TAG = "UpDateTag";
    private EditText editEmail;
    private TextView cancel;
    private TextView confirm;
    private EditListener listener;

    @NotNull
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_editprofile,container,false);
        editEmail = view.findViewById(R.id.edit_email);
        cancel = view.findViewById(R.id.text_cancel);
        confirm = view.findViewById(R.id.text_confirm);

        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        //set value from the fragment
        Bundle argument  = getArguments();
        if (argument != null) {
            editEmail.setText(argument.getString("email"));
        }

        //if cancel is clicked on the dialog
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.button_round_corner);
        cancel.setOnClickListener(v -> getDialog().dismiss());

        //if confirm is clicked on the dialog, pass value
        confirm.setOnClickListener(v -> {
            String inputEmail = editEmail.getText().toString();
            isEmailValid(inputEmail);
            if (isEmailValid(inputEmail)){
                listener.onEdit(inputEmail);
                getDialog().dismiss();
            }

            //update email authentication
            profileViewModel.updateCurrentUserEmail(inputEmail, TAG);
            //update field "email"
            profileViewModel.updateFirestoreUserEmail(inputEmail, TAG);

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
        void onEdit(String email);
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
}
