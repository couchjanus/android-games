package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.net.Uri;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private final static String SHAPEFILE = "game.txt";

    private TextView helpBox;

    int currentColor;
    int guessColor;
    int scoreYes = 0;
    int scoreNo = 0;
    int amount = 1;
    String name, email;
    int rating = 0;

    public void displayResult(String result){
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    public void guessYes(View view){
        if (guessColor == currentColor){
            scoreYes++;
        }
    }

    public void guessNo(View view){
        if (guessColor != currentColor){
            scoreNo++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        TextView textHeader = findViewById(R.id.header);
        textHeader.setText("Чи співпадає назва кольору зліва з кольором техта зправа?");
        Bundle arguments = getIntent().getExtras();
        if (arguments!=null){
            name = arguments.get("name").toString();
            email = arguments.get("email").toString();
            amount = Integer.parseInt(arguments.get("amount").toString());
        }
        run(amount);

    }

    public void run(int amount){
        TextView textViewRight = findViewById(R.id.textViewRight);
        TextView textViewLeft = findViewById(R.id.textViewLeft);

        for (int i = 0; i < amount; i++){
        Random rand = new Random();
        currentColor = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        textViewLeft.setBackgroundColor(currentColor);

        new CountDownTimer(60000,2000){
            public void onTick(long millisUntilFinished){
                guessColor = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                textViewRight.setBackgroundColor(guessColor);
            }
            public void onFinish(){
                displayResult("All done: ");
                rating = scoreYes*10 + scoreNo;
            }
        }.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.games_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_open_settings:
                openFile();
                return true;
            case R.id.action_save_settings:
                saveFile();
                return true;
            default:
                return true;
        }
    }

    private void openFile(){
        try {
            InputStream inputStream = openFileInput(SHAPEFILE);
            if (inputStream != null){
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null){
                    builder.append(line + "\n");
                }
                inputStream.close();
                //
                helpBox.setText(builder.toString());
            }

        } catch(Throwable t) {
            try {
                OutputStream outputStream = openFileOutput(SHAPEFILE, 0);
                OutputStreamWriter osw = new OutputStreamWriter((outputStream));
                osw.write(helpBox.getText().toString());
                osw.close();
            }catch (Throwable th){
                Toast.makeText(getApplicationContext(), "Exception: " + th.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void saveFile(){
        try {
            OutputStream outputStream = openFileOutput(SHAPEFILE, 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(helpBox.getText().toString());
            osw.close();
        }catch (Throwable t){
            Toast.makeText(getApplicationContext(), "Exception: " + t.toString(), Toast.LENGTH_LONG).show();

        }
    }




    public void toResult(View v){

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("rating", rating);
        startActivity(intent);
    }
}