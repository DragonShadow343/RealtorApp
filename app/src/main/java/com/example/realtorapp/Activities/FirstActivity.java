package com.example.realtorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.R;

public class FirstActivity extends AppCompatActivity {

    Button CONTINUE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first);

        ImageView Background = findViewById(R.id.bg_image);
        TextView Welcome = findViewById(R.id.welcome_txt);
        TextView Realtor = findViewById(R.id.realtor_txt);
        TextView Virtual = findViewById(R.id.virtual_txt);
        CONTINUE = findViewById(R.id.continue_btn);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    // takes to the login page when the continue button is pressed
    public void continueTOlogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

