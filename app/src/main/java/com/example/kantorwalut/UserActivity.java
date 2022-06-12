package com.example.kantorwalut;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

public class UserActivity extends AppCompatActivity {

    private Spinner spnFromCurrency;
    private Spinner spnToCurrency;
    private TextView txtValue;
    private TextView txtResult;
    private Button btnCalculate;

    private DatabaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initControls();

        db = new DatabaseHelper(this);

        init();

        btnCalculate.setOnClickListener(view -> {
            String fromCurrencyStr = String.valueOf(spnFromCurrency.getSelectedItem());
            String toCurrencyStr = String.valueOf(spnToCurrency.getSelectedItem());

            Currency fromCurrency = db.getCurrency(fromCurrencyStr);
            Currency toCurrency = db.getCurrency(toCurrencyStr);

            String valStr = txtValue.getText().toString();
            if(valStr.length() < 1) {
                message("Wpisz wartość do konwersji");
                return;
            }

            float val = Float.parseFloat(valStr);
            float result = val * fromCurrency.getFromValue().floatValue() * 1.0f/toCurrency.getToValue().floatValue();

            txtResult.setText(String.valueOf(result));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        List<Currency> allCurrencies = db.getAllCurrencies();
        String[] arraySpinner = allCurrencies.stream().map(Currency::getName).toArray(String[]::new);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFromCurrency.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnToCurrency.setAdapter(adapter2);
    }

    private void message(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    private void initControls() {
        spnFromCurrency = findViewById(R.id.spnFromCurrency);
        spnToCurrency = findViewById(R.id.spnToCurrency);
        txtValue = findViewById(R.id.txtValue);
        txtResult = findViewById(R.id.txtResult);
        btnCalculate = findViewById(R.id.btnCalculate);
    }
}