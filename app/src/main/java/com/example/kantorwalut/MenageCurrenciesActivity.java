package com.example.kantorwalut;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

public class MenageCurrenciesActivity extends AppCompatActivity {

    private LinearLayout layCurrencies;
    private Button btnCurrenciesBack;

    private DatabaseHelper db;

    private Button btnAddCurrency;
    private TextView txtAddCurrencyName;
    private TextView txtAddCurrencyFromValue;
    private TextView txtAddCurrencyToValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menage_currencies);

        initControls();

        db = new DatabaseHelper(this);

        btnAddCurrency.setOnClickListener(view -> {
            if (txtAddCurrencyName.getText().length() < 1) {
                message("Podaj nazwę waluty");
                return;
            }

            if (txtAddCurrencyFromValue.getText().length() < 1) {
                message("Podaj kwotę zamiany z");
                return;
            }

            if (txtAddCurrencyToValue.getText().length() < 1) {
                message("Podaj kwotę zamiany na");
                return;
            }

            db.insertCurrency(new Currency(0, txtAddCurrencyName.getText().toString(),
                    new BigDecimal(txtAddCurrencyFromValue.getText().toString()),
                    new BigDecimal(txtAddCurrencyToValue.getText().toString())));

            message("Dodano kwotę: " + txtAddCurrencyName.getText());

            init();
        });

        btnCurrenciesBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        });

        init();
    }

    private void init() {
        List<Currency> currencies = db.getAllCurrencies();

        layCurrencies.removeAllViews();

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);

        for (Currency currency : currencies) {
            TextView dat = new TextView(this);
            dat.setText(MessageFormat.
                    format("  {0}  {1}  {2}  {3}  ",
                            currency.getId(), currency.getName(), currency.getFromValue(), currency.getToValue()));
            row.addView(dat);

            Button remove = new Button(this);
            remove.setText(R.string.remove);
            remove.setOnClickListener(view -> {
                db.deleteCurrency(currency.getId());
                init();
            });
            row.addView(remove);
        }

        layCurrencies.addView(row);
    }

    private void message(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    private void initControls() {
        btnCurrenciesBack = findViewById(R.id.btnCurrenciesBack);
        layCurrencies = findViewById(R.id.layCurrencies);
        txtAddCurrencyName = findViewById(R.id.txtAddCurrencyName);
        txtAddCurrencyFromValue = findViewById(R.id.txtAddCurrencyFromValue);
        txtAddCurrencyToValue = findViewById(R.id.txtAddCurrencyToValue);
        btnAddCurrency = findViewById(R.id.btnAddCurrency);
    }
}