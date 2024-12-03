package com.example.ruletauwu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

public class settings extends MainActivity {
    private RadioGroup radioGroup;
    private CheckBox checkBox;
    private SeekBar seekBar;
    private EditText playerNameInput, moneyInput;
    TextView minbet;
    String moneyStr;

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu);
        initializeComponents();
        initializeListeners();
    }

    private void initializeComponents() {
        linearLayout = findViewById(R.id.layout_root);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.greenBackground);
        checkBox = findViewById(R.id.showLogoCheckbox);
        seekBar = findViewById(R.id.minBetSeekbar);
        minbet = findViewById(R.id.minbBet);
        playerNameInput = findViewById(R.id.name_input);
        moneyInput = findViewById(R.id.money_input);
        seekBar.setMax(1000);
        moneyInput.setText(String.valueOf(seekBar.getProgress()));
    }

    private void initializeListeners() {
      moneyInput.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              moneyStr = moneyInput.getText().toString();
          }

          @Override
          public void afterTextChanged(Editable editable) {

          }
      });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.greenBackground) {
                linearLayout.setBackgroundResource(R.drawable.background);
            } else if (checkedId == R.id.brownBackground) {
                linearLayout.setBackgroundResource(R.drawable.brown);
            }
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            TextView logo = findViewById(R.id.logo);
            logo.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moneyInput.setText(String.valueOf(progress));
                minbet.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button okButton = findViewById(R.id.okButton);
        Button cancelButton = findViewById(R.id.cancel_button);

        okButton.setOnClickListener(v -> {
            if (!validateInputs(moneyStr)) return;
            guardarSharedPreferences();
            goToMainActivity();
        });

        cancelButton.setOnClickListener(v -> goToMainActivity());
    }

    private boolean validateInputs(String moneyStr) {
        if (moneyStr.isEmpty()) {
            showMessage("Por favor, introduce una cantidad de dinero.");
            return false;
        }

        try {
            int money = Integer.parseInt(moneyStr);
            if (money > 1000) {
                showMessage("El dinero inicial no puede superar 1000.");
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Por favor, introduce un número válido.");
            return false;
        }

        if (playerNameInput.getText().toString().isEmpty()) {
            showMessage("Por favor, introduce un nombre de jugador.");
            return false;
        }

        return true;
    }

    private void guardarSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("menuPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("playerName", playerNameInput.getText().toString());
        editor.putInt("initialMoney", Integer.parseInt(moneyInput.getText().toString()));
        editor.putInt("minBet", seekBar.getProgress());
        editor.putBoolean("showLogo", checkBox.isChecked());
        editor.apply();

        showMessage("Configuración guardada.");
    }

    private void goToMainActivity() {
        Intent intent = new Intent(settings.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
