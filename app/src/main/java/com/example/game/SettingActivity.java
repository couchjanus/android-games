package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    TextView name, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        Bundle arguments = getIntent().getExtras();


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        if (arguments!=null){
            name.setText(arguments.get("name").toString());
            email.setText(arguments.get("email").toString());

        }

    }

    public void runGame(){

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("email", email.getText().toString());
        startActivity(intent);
    }
}