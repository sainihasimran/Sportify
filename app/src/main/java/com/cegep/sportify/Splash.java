package com.cegep.sportify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.imageView);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                  /*  if ( !(fuser == null))
                    {
                        Intent i = new Intent(Splash.this, MainActivity.class);
                        finish();
                        startActivity(i);
                    }

                   */

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    Intent intent;
                    if (firebaseUser == null) {
                        intent = new Intent(Splash.this, LoginActivity.class);
                    } else {
                        intent = new Intent(Splash.this, MainActivity.class);
                    }
                    startActivity(intent);

                    finish();
                }
            }
        };
        timer.start();
    }
}