package com.example.ruletauwu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView playerScoreText, dealerScoreText, moneyText;
    private EditText betValue;
    private Button buttonCard, buttonStand, buttonRestart;

    private ImageView[] playerCardSlots;
    private ImageView[] dealerCardSlots;

    private int money = 500;
    private int bet = 10;
    private boolean firstDraw;
    private List<int[]> deck;
    private List<int[]> playerHand, dealerHand;
    BroadcastReceiver br = new BaterryLow();
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        this.registerReceiver(br, filter);
        initializeComponents();
        initializeListeners();
        startNewGame();
        //View myView = findViewById(R.id.aboutme);
       // registerForContextMenu(myView);
        Toolbar mytoolbar= (Toolbar) findViewById(R.id.toolbar);
        //using toolbar as ActionBar
        setSupportActionBar(mytoolbar);
       // startService(new Intent()); TODO

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.GAME) {
           startNewGame();
        } else if (item.getItemId() == R.id.SETTINGS) {

        } else if (item.getItemId() == R.id.ABOUT) {
            showMyPhoto();
        } else if (item.getItemId() == R.id.QUIT) {
            showDialogConfirmation();
        } else if(item.getItemId() == R.id.HISTORIAL){
          mostrarUltimaPartida();
        }
        return super.onContextItemSelected(item);
    }

    private void mostrarUltimaPartida() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Historial ultima partida");
        //TODO
    }

    private void showMyPhoto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sobre el autor");
        builder.setMessage("Aplicación hecha por Nayra Rios");
        //builder.setIcon();
        //ImageView image = (ImageView)
            //    dialog.findViewById(R.id.image);
        //image.setImageResource(R.drawable.ic_launcher);
        AlertDialog alert= builder.create();
        alert.show();

    }
    private void showDialogConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir");
        builder.setMessage("Estas seguro que quieres salir");
        builder.setCancelable(false);
        builder.setPositiveButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    private void initializeComponents() {
        playerScoreText = findViewById(R.id.player_score_text);
        dealerScoreText = findViewById(R.id.dealer_score_text);
        moneyText = findViewById(R.id.money_value);
        betValue = findViewById(R.id.bet_value);
        buttonCard = findViewById(R.id.button_card);
        buttonStand = findViewById(R.id.button_stand);
        buttonRestart = findViewById(R.id.button_restart);


        playerCardSlots = new ImageView[]{
                findViewById(R.id.player_card1),
                findViewById(R.id.player_card2),
                findViewById(R.id.player_card3),
                findViewById(R.id.player_card4),
                findViewById(R.id.player_card5)
        };

        dealerCardSlots = new ImageView[]{
                findViewById(R.id.dealer_card1),
                findViewById(R.id.dealer_card2),
                findViewById(R.id.dealer_card3),
                findViewById(R.id.dealer_card4),
                findViewById(R.id.dealer_card5)
        };

        loadMoney();
    }

    private void initializeListeners() {
        buttonCard.setOnClickListener(v -> playerDrawCard());
        buttonStand.setOnClickListener(v -> dealerTurn());
        buttonRestart.setOnClickListener(view -> endGame("Reiniciaste partida"));
    }

    private void startNewGame() {
        clearCardSlots(playerCardSlots);
        clearCardSlots(dealerCardSlots);

        deck = createDeck();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        firstDraw = true;

        bet = betValue.getText().toString().isEmpty() ? 10 : Integer.parseInt(betValue.getText().toString());
        updateScores();
    }

    private List<int[]> createDeck() {
        List<int[]> deck = new ArrayList<>();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};
        int[] suits = {R.drawable.hearts, R.drawable.diamond, R.drawable.spades, R.drawable.clubs};
        for (int suit : suits) {
            for (int value : values) {
                deck.add(new int[]{value, suit});
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    private int[] drawCard(ImageView cardSlot) {
        if (deck.isEmpty()) return null;
        int[] card = deck.remove(0);
        Bitmap frontBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.front);

        Bitmap suitBitmap = BitmapFactory.decodeResource(getResources(), card[1]);
        Bitmap valueBitmap = BitmapFactory.decodeResource(getResources(), getDrawableForValue(card[0]));
        Bitmap combinedBitmap = combineImages(frontBitmap, suitBitmap, valueBitmap);
        cardSlot.setImageBitmap(combinedBitmap);
        return card;
    }

    private void playerDrawCard() {
        if (firstDraw) {
            playerHand.add(drawCard(playerCardSlots[0]));
            playerHand.add(drawCard(playerCardSlots[1]));
            dealerHand.add(drawCard(dealerCardSlots[0]));
            updateDealerVisibleScore();

            Bitmap backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back);
            dealerCardSlots[1].setImageBitmap(backBitmap);
            dealerHand.add(deck.remove(0)); // Añadir carta oculta a su mano

            firstDraw = false;
        } else {
            if (playerHand.size() < playerCardSlots.length) {
                playerHand.add(drawCard(playerCardSlots[playerHand.size()]));
            } else {
                Toast.makeText(this, "No puedes tomar más cartas", Toast.LENGTH_SHORT).show();
            }
        }
        updateScores();

        if (calculateScore(playerHand) > 21) {
            endGame("¡Te pasaste! Perdiste.");
            money -= bet;
            saveMoney();
        }
    }

    private void dealerTurn() {
        buttonCard.setEnabled(false); // Deshabilitar botón "Carta"
        buttonStand.setEnabled(false); // Deshabilitar botón "Plantarse"

        // Voltear la segunda carta del dealer
        int[] hiddenCard = dealerHand.get(1);
        dealerCardSlots[1].setImageBitmap(
                combineImages(
                        BitmapFactory.decodeResource(getResources(), R.drawable.front),
                        BitmapFactory.decodeResource(getResources(), hiddenCard[1]),
                        BitmapFactory.decodeResource(getResources(), getDrawableForValue(hiddenCard[0]))
                )
        );

        // Actualizar el puntaje del dealer con todas las cartas reveladas
        updateScores();

        // Dealer toma cartas si tiene 16 o menos
        while (calculateScore(dealerHand) <= 16 && dealerHand.size() < dealerCardSlots.length) {
            dealerHand.add(drawCard(dealerCardSlots[dealerHand.size()]));
            updateScores();
        }

        determineWinner();
    }

    private void updateDealerVisibleScore() {
        if (!dealerHand.isEmpty()) {
            // Mostrar solo el valor de la primera carta visible del dealer
            int visibleScore = dealerHand.get(0)[0];
            dealerScoreText.setText(String.valueOf((visibleScore > 10) ? 10 : visibleScore));
        }
    }

    private void updateScores() {
        // Actualizar puntaje del jugador
        String playerScoreTextValue = calculateScoreWithAces(playerHand);
        playerScoreText.setText(playerScoreTextValue);

        // Actualizar puntaje del dealer
        if (!firstDraw) {
            // Mostrar la puntuación completa del dealer cuando las cartas estén reveladas
            String dealerScoreTextValue = calculateScoreWithAces(dealerHand);
            dealerScoreText.setText(dealerScoreTextValue);
        } else {
            // Mostrar solo la puntuación de la primera carta del dealer
            updateDealerVisibleScore();
        }

        // Actualizar el dinero
        moneyText.setText(String.valueOf(money));
    }

    private String calculateScoreWithAces(List<int[]> hand) {
        int score = 0;
        int aces = 0;
        for (int[] card : hand) {
            int value = card[0];
            score += (value > 10) ? 10 : value;
            if (value == 1) aces++;
        }

        int scoreWithAceAs11 = score;
        while (scoreWithAceAs11 <= 11 && aces > 0) {
            scoreWithAceAs11 += 10;
            aces--;
        }

        if (scoreWithAceAs11 != score) {
            return score + "/" + scoreWithAceAs11;
        } else {
            return String.valueOf(score);
        }
    }

    private int calculateScore(List<int[]> hand) {
        int score = 0;
        int aces = 0;
        for (int[] card : hand) {
            int value = card[0];
            score += (value > 10) ? 10 : value;
            if (value == 1) aces++;
        }
        while (score <= 11 && aces > 0) {
            score += 10;
            aces--;
        }
        return score;
    }

    private Bitmap combineImages(Bitmap frontBitmap, Bitmap suitBitmap, Bitmap valueBitmap) {
        Bitmap overlay = Bitmap.createBitmap(frontBitmap.getWidth(), frontBitmap.getHeight(), frontBitmap.getConfig());
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(frontBitmap, new Matrix(), null);
        canvas.drawBitmap(suitBitmap, 250, 500, null);
        canvas.drawBitmap(valueBitmap, 365, 200, null);
        return overlay;
    }

    private int getDrawableForValue(int value) {
        switch (value) {
            case 1:
                return R.drawable.a;
            case 11:
                return R.drawable.j;
            case 12:
                return R.drawable.q;
            case 13:
                return R.drawable.k;
            default:
                return getResources().getIdentifier("c" + value, "drawable", getPackageName());
        }
    }

    private void loadMoney() {
        SharedPreferences prefs = getSharedPreferences("BlackjackPrefs", Context.MODE_PRIVATE);
        money = prefs.getInt("money", 500);
    }

    private void saveMoney() {
        SharedPreferences prefs = getSharedPreferences("BlackjackPrefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("money", money).apply();
    }

    private void clearCardSlots(ImageView[] cardSlots) {
        for (ImageView slot : cardSlots) {
            slot.setImageResource(0);
        }
    }

    private void endGame(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        buttonCard.setEnabled(true);
        buttonStand.setEnabled(true);
        new android.os.Handler().postDelayed(this::startNewGame, 2000);
    }

    private void determineWinner() {
        int playerFinalScore = calculateScore(playerHand);
        int dealerFinalScore = calculateScore(dealerHand);

        if (dealerFinalScore > 21 || playerFinalScore > dealerFinalScore) {
            endGame("¡Ganaste!");
            money += bet;
        } else if (playerFinalScore == dealerFinalScore) {
            endGame("Empate");
        } else {
            endGame("Perdiste");
            money -= bet;
        }
        saveMoney();
    }
}



