package com.example.kantorwalut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView txtLoginLogin;
    private TextView txtLoginPassword;
    private TextView txtRegisterLogin;
    private TextView txtRegisterPassword;
    private Button btnLogin;
    private Button btnRegister;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        initControls();

        btnLogin.setOnClickListener(view -> login());
        btnRegister.setOnClickListener(view -> register());

    }

    private void register() {
        if (txtRegisterLogin.getText().length() < 1) {
            message("Podaj login.");
            return;
        }

        if (txtRegisterPassword.getText().length() < 1) {
            message("Podaj hasło.");
            return;
        }

        if(!db.insertUser(txtRegisterLogin.getText().toString(), txtRegisterPassword.getText().toString())) {
            message("Nie można zarejestrować użytkownika.");
            return;
        }

        message("Zarejestrowano użytkownika: " + txtRegisterLogin.getText().toString() + ". Możesz się zalogować.");
    }

    private void login() {
        if (txtLoginLogin.getText().length() < 1) {
            message("Podaj login.");
            return;
        }

        if (txtLoginPassword.getText().length() < 1) {
            message("Podaj hasło.");
            return;
        }

        if (!db.login(txtLoginLogin.getText().toString(), txtLoginPassword.getText().toString())) {
            message("Niewłaściwy login lub hasło.");
            return;
        }

        message("Zalogowano jako: " + txtLoginLogin.getText());

        State.loggedIn = txtLoginLogin.getText().toString();
        State.isAdmin = db.getRole(txtLoginLogin.getText().toString()).equals("admin");

        if(State.isAdmin) {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        }
    }

    private void message(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    private void initControls() {
        txtLoginLogin = findViewById(R.id.txtLoginLogin);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);
        txtRegisterLogin = findViewById(R.id.txtRegisterLogin);
        txtRegisterPassword = findViewById(R.id.txtRegisterPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }
}