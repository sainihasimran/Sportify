package com.cegep.sportify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;


public class LoginFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText client_email, client_pass;
    Button signin_btn;
    TextView joinus;

    public LoginFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client_email = view.findViewById(R.id.email_et);
        client_pass = view.findViewById(R.id.password_et);
        signin_btn = view.findViewById(R.id.Signin_btn);
        joinus = view.findViewById(R.id.signup);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: Login clicked");
                if (!checkEmptyFields()) {
                    String a_email = client_email.getText().toString();
                    String a_pass = client_pass.getText().toString();
                    doSignin(a_email, a_pass);
                }
            }
        });

        joinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SignUpActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    private void doSignin(String email, String pass)
    {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful())
                    {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference  = firebaseDatabase.getReference("Users");
                        Query query = databaseReference.orderByChild("email").equalTo(currentUser.getEmail());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DataSnapshot userSnapshot = null;
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    userSnapshot = child;
                                }

                                User user = userSnapshot.getValue(User.class);
                                SportifyApp.user = user;

                                if (user == null) {
                                    Toast.makeText(requireContext(), "Failed to login", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Login Success!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(requireContext(), MainActivity.class);
                                    startActivity(intent);
                                    requireActivity().finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Authenticate Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean checkEmptyFields()
    {
        if(TextUtils.isEmpty(client_email.getText().toString()))
        {
            client_email.setError("Email cannot be empty!");
            client_email.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(client_pass.getText().toString()))
        {
            client_pass.setError("Password cannot be empty!");
            client_pass.requestFocus();
            return true;
        }
        return false;
    }
}