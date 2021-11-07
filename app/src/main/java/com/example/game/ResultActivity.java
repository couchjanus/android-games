package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    String userName, userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle arguments = getIntent().getExtras();
        if (arguments!=null){
            userName = arguments.get("name").toString();
            userEmail = arguments.get("email").toString();
        }
    }

    public void saveToDb(View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("games.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS players (name TEXT, email TEXT, rate INTEGER, UNIQUE(name))");
        db.execSQL("INSERT OR IGNORE INTO players VALUES('tom', 'tom@my.com', 5);");
        db.close();
    }

    public void showResult(View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("games.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM players", null);

        TextView showData = findViewById(R.id.showData);
        showData.setText("");
        while(query.moveToNext()){
            String name = query.getString(0);
            String email = query.getString(1);
            int rating = query.getInt(2);
            showData.append("Name: " + name + " Email: " + email + " Tating: " + rating);
        }
        query.close();
        db.close();
    }
}