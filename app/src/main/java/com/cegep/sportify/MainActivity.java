package com.cegep.sportify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextInputLayout txtmail, txtpswd, brandname,txtcpswd;
    ProgressBar bar;
    FirebaseAuth mAuth;
    String fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        brandname = (TextInputLayout) findViewById(R.id.bname);
        txtmail = (TextInputLayout) findViewById(R.id.email);
        txtpswd = (TextInputLayout) findViewById(R.id.pwd);
        txtcpswd = (TextInputLayout) findViewById(R.id.cnfpwd);
        bar = (ProgressBar) findViewById(R.id.progressBar3);
        mAuth = FirebaseAuth.getInstance();

    }

    public void gotologin(View view) {
        startActivity(new Intent(MainActivity.this, loginActivity.class));
    }

    public void adminsingup(View view) {
        bar.setVisibility(View.VISIBLE);

        String brname = brandname.getEditText().getText().toString();
        String email = txtmail.getEditText().getText().toString();
        String password = txtpswd.getEditText().getText().toString();
        String cpassword = txtcpswd.getEditText().getText().toString();

        if (brname.isEmpty() == true)
        {
            brandname.setError("Brand name field is empty!");
        }
        if (email.isEmpty() == true)
        {
            bar.setVisibility(View.INVISIBLE);
            txtmail.setError("Email field is empty!");
        }
        else if(password.length() < 9) {

            bar.setVisibility(View.INVISIBLE);
            txtpswd.setError("Password length must be greater than 9 !");
        }
        else if(!password.equals(cpassword)==true) {

            bar.setVisibility(View.INVISIBLE);
            txtcpswd.setError("Password not matched!");
        }
        else{

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                bar.setVisibility(View.INVISIBLE);
                                txtmail.getEditText().setText("");
                                txtpswd.getEditText().setText("");

                                fuser = mAuth.getCurrentUser().getUid();

                                startActivity(new Intent(MainActivity.this, loginActivity.class));

                                Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                bar.setVisibility(View.INVISIBLE);
                                txtmail.getEditText().setText("");
                                txtpswd.getEditText().setText("");
                                Toast.makeText(MainActivity.this, "Error !!.." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}