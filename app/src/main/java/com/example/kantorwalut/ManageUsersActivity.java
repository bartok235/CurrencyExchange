package com.example.kantorwalut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    private LinearLayout layUsers;
    private Button btnUsersBack;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        initControls();

        db = new DatabaseHelper(this);

        btnUsersBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        });

        init();
    }

    private void init() {
        List<User> users = db.getAllUsers();

        layUsers.removeAllViews();

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);

        for (User user : users) {
            TextView dat = new TextView(this);
            dat.setText(MessageFormat.
                    format("  {0}  {1}  {2}  ",
                            user.getId(), user.getUsername(), user.getRole()));
            row.addView(dat);

            if (!user.getRole().equals("admin")) {
                Button promote = new Button(this);
                promote.setText(R.string.promote);
                promote.setOnClickListener(view -> {
                    db.makeAdmin(user.getId());
                    init();
                });
                row.addView(promote);
            }
            Button remove = new Button(this);
            remove.setText(R.string.remove);
            remove.setOnClickListener(view -> {
                db.deleteUser(user.getUsername());
                init();
            });
            row.addView(remove);
        }

        layUsers.addView(row);
    }

    private void message(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    private void initControls() {
        btnUsersBack = findViewById(R.id.btnCurrenciesBack);
        layUsers = findViewById(R.id.layCurrencies);
    }
}