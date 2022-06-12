package com.example.kantorwalut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    private View btnManageUsers;
    private View btnManageCurrencies;
    private View btnAdminLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initControls();

        btnManageUsers.setOnClickListener(view -> {
            Intent intent = new Intent(this, ManageUsersActivity.class);
            startActivity(intent);
        });

        btnManageCurrencies.setOnClickListener(view -> {
            Intent intent = new Intent(this, MenageCurrenciesActivity.class);
            startActivity(intent);
        });

        btnAdminLogout.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            message("Wylogowano.");
        });
    }

    private void message(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    private void initControls() {
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageCurrencies = findViewById(R.id.btnManageCurrencies);
        btnAdminLogout = findViewById(R.id.btnAdminLogout);
    }
}