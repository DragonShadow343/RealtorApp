package com.example.realtorapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.R;
import com.example.realtorapp.auth.AuthRepo;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button LOGIN, GUEST;
    EditText EMAIL, PASSWORD;
    TextView SIGNUP;

    //creating auth repo object to connect the login page to the firebase auth
    AuthRepo authRepo;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Adding all the elements to the variables and on screen
        LOGIN = findViewById(R.id.login_btn);
        GUEST = findViewById(R.id.guest_btn);
        EMAIL = findViewById(R.id.email_input);
        PASSWORD = findViewById(R.id.password_input);
        SIGNUP = findViewById(R.id.textView);

        // auth repo object
        authRepo = new AuthRepo();




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // making the signup text clickable
        String signup_text = "Don't have an account? Sign Up";
        SpannableString ss = new SpannableString(signup_text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        };

        ss.setSpan(clickableSpan, 23, 30,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SIGNUP.setText(ss);
        SIGNUP.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());






    }


    public void goHomeWithLogin(View view){
        // adding email and pass to variable to check if they are correct and also to check if they are empty
        String email = EMAIL.getText().toString().trim();
        String password = PASSWORD.getText().toString().trim();

        // this checks if the fields are empty
        if(email.isEmpty() || password.isEmpty()){
            EMAIL.setError("Please enter your email");
            PASSWORD.setError("Please enter your password");
            return;
        }


        // to check if the email exists in the database
        authRepo.login(email, password, task -> {
            if(task.isSuccessful()){
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                PASSWORD.setError("Invalid email or password");
            }

        });

    }

    public void loginASguest(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
        finish();
    }
}