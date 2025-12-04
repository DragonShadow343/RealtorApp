package com.example.realtorapp.Activities;

import static java.lang.reflect.Array.getInt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.R;

public class FilterActivity extends AppCompatActivity {

    EditText minPrice, maxPrice, bedrooms, bathrooms;
    CheckBox petFriendlyCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }



        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);
        bedrooms = findViewById(R.id.bedrooms);
        bathrooms = findViewById(R.id.bathrooms);
        petFriendlyCheck = findViewById(R.id.petFriendlyCheck);

        findViewById(R.id.applyFiltersBtn).setOnClickListener(v -> applyFilters());

        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
          //  Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
           // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
           // return insets;
        //});
    }

    private void applyFilters() {
        Intent result = new Intent();

        result.putExtra("minPrice", getInt(minPrice, 800));
        result.putExtra("maxPrice", getInt(maxPrice, 5000));
        result.putExtra("bedrooms", getInt(bedrooms, 1));
        result.putExtra("bathrooms", getInt(bathrooms, 1));
        result.putExtra("petFriendly", petFriendlyCheck.isChecked());

        setResult(RESULT_OK, result);
        finish();
    }

    private int getInt(EditText e, int defaultValue) {
        String s = e.getText().toString().trim();
        return s.isEmpty() ? defaultValue : Integer.parseInt(s);
    }
}