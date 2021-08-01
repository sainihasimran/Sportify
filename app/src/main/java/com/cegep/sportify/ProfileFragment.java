package com.cegep.sportify;

import android.os.Bundle;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;

   @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameEditText = view.findViewById(R.id.first_name_text);
        lastNameEditText = view.findViewById(R.id.last_name_text);
        emailEditText = view.findViewById(R.id.email_text);

        view.findViewById(R.id.update_profile_btn).setOnClickListener(v -> validateUpdateUser());

        firstNameEditText.setText(SportifyApp.user.firstname);
        lastNameEditText.setText(SportifyApp.user.lastname);
        emailEditText.setText(SportifyApp.user.email);

        setSelection(firstNameEditText);
        setSelection(lastNameEditText);
        setSelection(emailEditText);
    }

    private void validateUpdateUser() {
        User newUser = new User(SportifyApp.user);

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (SportifyApp.user.firstname.equals(firstName) && SportifyApp.user.lastname.equals(lastName) && SportifyApp.user.email.equals(email)) {
            endUpdateUser();
            return;
        } else {
            if (newUser.email.equals(email)) {
                newUser.firstname = firstName;
                newUser.lastname = lastName;

                updateUser(newUser);
            } else {
                FirebaseAuth.getInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Sensitive operation performed. Please re-login", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        newUser.firstname = firstName;
                        newUser.lastname = lastName;
                        newUser.email = email;

                        updateUser(newUser);
                    }
                });
            }
        }
    }

    private void updateUser(User user) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(SportifyApp.user.userId);
        userReference.setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SportifyApp.user = user;
                endUpdateUser();
            } else {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void endUpdateUser() {
        requireActivity().finish();
    }

    private void setSelection(EditText editText) {
        Selection.setSelection(editText.getText(), editText.length());
    }
}
