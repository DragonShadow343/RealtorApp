package com.example.realtorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.R;

public class MainActivity extends AppCompatActivity {

    Button bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void HomeScreen(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void goHome(View view){
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }

    public void goUpload(View view) {
        Intent intent = new Intent(this, UploadListingActivity.class);
        startActivity(intent);
    }
    public void loadAgents(View view) {
        // This command swaps the screen content
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SMS_Local_Agents()) // R.id.main matches the XML ID above
                .addToBackStack(null) // Allows pressing "Back" to return to buttons
                .commit();
    }
}