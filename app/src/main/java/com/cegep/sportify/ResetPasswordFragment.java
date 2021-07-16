package com.cegep.sportify;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static com.cegep.sportify.SportifyApp.user;


public class ResetPasswordFragment extends Fragment {

    DatabaseReference DatabaseReference;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    Button reset_password_btn;

    FirebaseAuth fauth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resetpassword, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        reset_password_btn = view.findViewById(R.id.reset_password_btn);


        fauth = FirebaseAuth.getInstance();
        DatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        reset_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetmail = new EditText(view.getContext());
                AlertDialog.Builder forgotpassword = new AlertDialog.Builder(view.getContext());
                forgotpassword.setTitle("Reset Password?");
                forgotpassword.setMessage("Enter Your Email To Receive Reset Link.");
                forgotpassword.setView(resetmail);

                forgotpassword.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String mail = resetmail.getText().toString();
                        fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(requireContext(), "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireContext(), "Error: Reset Link is Not Sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                forgotpassword.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
                forgotpassword.create().show();
            }
        });
    }
}
