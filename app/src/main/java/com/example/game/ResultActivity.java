package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {
    String userName, userEmail;
    int userRating = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle arguments = getIntent().getExtras();
        if (arguments!=null){
            userName = arguments.get("name").toString();
            userEmail = arguments.get("email").toString();
            userRating = Integer.parseInt(arguments.get("rating").toString());
        }
    }

    public void saveToDb(View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("games.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS players (name TEXT, email TEXT, rate INTEGER, UNIQUE(name))");


        ContentValues contentValues = new ContentValues();
        contentValues.put("name", userName);
        contentValues.put("email", userEmail);
        contentValues.put("rate", userRating);
        db.insert("players", null, contentValues);
        db.close();
    }

    public void showResult(View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("games.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM players WHERE name='"+userName+"'", null);

        TextView showName = findViewById(R.id.showName);
        TextView showEmail = findViewById(R.id.showEmail);
        TextView showRating = findViewById(R.id.showRating);

        showName.setText("");
        showEmail.setText("");
        showRating.setText("");

        while(query.moveToNext()){
            String name = query.getString(0);
            String email = query.getString(1);
            int rating = query.getInt(2);
            showName.append(name);
            showEmail.append(email);
            showRating.append(""+rating);

        }
        query.close();
        db.close();
    }

    public void sendResult(View view){
        sendEmail();
    }

    @SuppressLint("IntentReset")
    protected void sendEmail() {
        Log.i("Send email", "Email message sended successfully");

        String[] TO = {"couchjanus@gmail.com"};
        String[] CC = {"janusnic@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "Finished sending email");
        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(ResultActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }
}