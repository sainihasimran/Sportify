package com.cegep.sportify;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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


public class UserSignupFragment extends Fragment {


    DatabaseReference DatabaseReference ;
    public UserSignupFragment() {
        // Required empty public constructor
    }

    Button btnsign;
    TextInputLayout txtmail, txtpswd,firstname, lastname,txtcpswd;
    TextView tvlogin;
    ProgressBar bar;

    FirebaseAuth fauth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_signupragment, container, false);

        btnsign = view.findViewById(R.id.btnsign);
        tvlogin = view.findViewById(R.id.btnlog);

        firstname = (TextInputLayout) view.findViewById(R.id.fname);
        lastname = (TextInputLayout) view.findViewById(R.id.lname);
        txtmail = (TextInputLayout) view.findViewById(R.id.email);
        txtpswd = (TextInputLayout) view.findViewById(R.id.pwd);
        txtcpswd = (TextInputLayout) view.findViewById(R.id.cnfpwd);
        bar = (ProgressBar) view.findViewById(R.id.progressBar3);

        fauth = FirebaseAuth.getInstance();
        DatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Calendar calendar =Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bar.setVisibility(View.VISIBLE);

                String email = txtmail.getEditText().getText().toString();
                String password = txtpswd.getEditText().getText().toString();
                String cpassword = txtcpswd.getEditText().getText().toString();
                String fname = firstname.getEditText().getText().toString();
                String lname = lastname.getEditText().getText().toString();

                firstname.setError(null);
                lastname.setError(null);
                txtmail.setError(null);
                txtpswd.setError(null);
                txtcpswd.setError(null);

                if (fname.isEmpty() == true) {
                    bar.setVisibility(View.INVISIBLE);
                    firstname.setError("First name field is empty!");
                }
               else if (lname.isEmpty() == true) {
                    bar.setVisibility(View.INVISIBLE);
                    lastname.setError("Last name field is empty!");
                }
                else if (TextUtils.isEmpty(email)) {
                    bar.setVisibility(View.INVISIBLE);
                    txtmail.setError("Email is Required.");
                }

                else if (TextUtils.isEmpty(password)) {
                    bar.setVisibility(View.INVISIBLE);
                    txtpswd.setError("Password is Required.");
                }

               else if (password.length() < 9) {
                    bar.setVisibility(View.INVISIBLE);
                    txtpswd.setError("Min password length is 9");
                }
               else if (!password.equals(cpassword)) {
                    bar.setVisibility(View.INVISIBLE);
                    txtcpswd.setError("Password not matched!");
                }
               else {

                    fauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                bar.setVisibility(View.INVISIBLE);

                                String email = txtmail.getEditText().getText().toString();
                                User user = new User(email, fname, lname, 0);
                                user.firstname = fname;

                                DatabaseReference userDatabaseReference = DatabaseReference.push();
                                user.userId = userDatabaseReference.getKey();
                                userDatabaseReference.setValue(user);

                                SportifyApp.user = user;

                                Intent intent = new Intent(requireActivity(), MainActivity.class);
                                startActivity(intent);
                                requireActivity().finish();
                            } else {
                                bar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity().getApplicationContext()
                                        , "Registeration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            }
        });


        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        return view;
    }
}