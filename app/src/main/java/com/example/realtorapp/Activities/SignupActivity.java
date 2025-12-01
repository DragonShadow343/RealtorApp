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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.R;
import com.example.realtorapp.auth.AuthRepo;

public class SignupActivity extends AppCompatActivity {

    EditText NAME, EMAIL,PASSWORD;
    Spinner TYPE;
    Button SIGNUP, GUEST;
    TextView LOGIN;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);


        // creating all variables to store the values of the elements
        NAME = findViewById(R.id.name_input);
        EMAIL = findViewById(R.id.email_signup_input);
        PASSWORD = findViewById(R.id.password_signup_input);
        TYPE = findViewById(R.id.type_spinner);
        SIGNUP = findViewById(R.id.signup_btn);
        GUEST = findViewById(R.id.guest_signup_btn);



        // for creating login page link
        LOGIN = findViewById(R.id.login_signupActivity_text);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // creating the login page link
        String login_text = "Already have an account?? Login!";
        SpannableString ss = new SpannableString(login_text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };

        ss.setSpan(clickableSpan, 23, 30,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        LOGIN.setText(ss);
        LOGIN.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        // link done

    }

    public void clickSignup(View view){
        String name = NAME.getText().toString().trim();
        String email = EMAIL.getText().toString().trim();
        String password = PASSWORD.getText().toString().trim();
        String type = TYPE.getSelectedItem().toString().trim();

        // creating all the criteria for the signup
            if(name.isEmpty() || email.isEmpty() || password.isEmpty() || type.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // email criteria
            if(!email.contains("@")) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            // password criteria
            if(password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.matches(".*[A-Z].*")) {
                Toast.makeText(this, "Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.matches(".*[0-9].*")){
                Toast.makeText(this, "Password must contain at least one number", Toast.LENGTH_SHORT).show();
                return;
            }

        AuthRepo repo = new AuthRepo();
        repo.signup(name, email, password, type, task -> {
            if(task.isSuccessful()){
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();

            }
        });
        }

    }


