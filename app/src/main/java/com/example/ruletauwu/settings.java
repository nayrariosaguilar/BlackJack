package com.example.ruletauwu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class settings extends AppCompatActivity {
    private RadioGroup radioGroup;
    private CheckBox checkBox;
    private SeekBar seekBar;
    private EditText playerNameInput, moneyInput;
    private TextView minbet;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);
        initializeComponents();
        initializeListeners();
        Toolbar mytoolbar= findViewById(R.id.toolbar2);
        setSupportActionBar(mytoolbar);
    }


    private void initializeComponents() {
        SharedPreferences sharedPref = getSharedPreferences("menuPrefs", Context.MODE_PRIVATE);

        linearLayout = findViewById(R.id.layout_root);
        radioGroup = findViewById(R.id.radioGroup);


        checkBox = findViewById(R.id.showLogoCheckbox);
        checkBox.setChecked(sharedPref.getBoolean("showLogo", true));

        seekBar = findViewById(R.id.minBetSeekbar);
        seekBar.setMax(1000);
        seekBar.setProgress(sharedPref.getInt("minBet", 50));

        minbet = findViewById(R.id.minbBet);
        minbet.setText(String.valueOf(seekBar.getProgress()));

        playerNameInput = findViewById(R.id.name_input);
        playerNameInput.setText(sharedPref.getString("playerName", ""));

        moneyInput = findViewById(R.id.money_input);
        moneyInput.setText(String.valueOf(sharedPref.getInt("initialMoney", 500)));
    }

    private void initializeListeners() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = getSharedPreferences("menuPrefs", Context.MODE_PRIVATE).edit();
            editor.putInt("background", checkedId);
            editor.apply();

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
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        moneyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!text.isEmpty()) {
                    int value = Integer.parseInt(text);
                    if (value <= seekBar.getMax()) {
                        seekBar.setProgress(value);
                    } else {
                        moneyInput.setError("Introduce un número válido.");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        findViewById(R.id.okButton).setOnClickListener(v -> {
            if (!validateInputs()) return;
            guardarSharedPreferences();
            goToMainActivity();
        });

        findViewById(R.id.cancel_button).setOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.GAME) {
            goToMainActivity();
        }
        else if (item.getItemId() == R.id.SETTINGS) {
            irASettings();
        }
        else if (item.getItemId() == R.id.ABOUT) {
            showMyPhoto();
        }
        else if (item.getItemId() == R.id.QUIT) {
            showDialogConfirmation();
        }
        else if (item.getItemId() == R.id.HISTORIAL) {
            mostrarUltimaPartida();
        }
        return true;
    }

    public void irASettings(){
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
    }

    public String showLastResult() {
        StringBuilder lastGame = new StringBuilder();
        String archivo = "lastResults.txt";

        try (FileInputStream readlastResults = openFileInput(archivo);
             BufferedReader reader = new BufferedReader(new InputStreamReader(readlastResults))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                lastGame.append(linea).append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lastGame.toString().trim();
    }

    private void mostrarUltimaPartida() {
        String ultimaPartida = showLastResult();
        if (ultimaPartida.isEmpty()) {
            ultimaPartida = "No hay partidas registradas.";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Historial última partida");
        builder.setMessage(ultimaPartida);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    private void showMyPhoto()
    {
        //
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.sobremi_layout, null);
        // Dialog dialog = new Dialog(this);
        //builder.setContentView(R.layout.sobremi_layout);
        builder.setTitle("Sobre el autor");
        TextView text = (TextView)
                customView.findViewById(R.id.texto_sobremi);
        builder.setView(customView);
        text.setText("Aplicación hecha por Nayra Rios");
        ImageView image = (ImageView)
                customView.findViewById(R.id.foto);
        image.setImageResource(R.drawable.gato);
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showDialogConfirmation() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Títol del Quadre");
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity(); // Tanca totes les Activities

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu); // Asegúrate de que el archivo "menu.xml" existe en "res/menu"
        return true;
    }


    private boolean validateInputs() {
        String moneyStr = moneyInput.getText().toString();
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
        editor.putInt("background", radioGroup.getCheckedRadioButtonId());
        editor.apply();

        showMessage("Configuración guardada.");
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, GameView.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

