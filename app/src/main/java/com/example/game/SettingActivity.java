package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class SettingActivity extends AppCompatActivity {
    TextView name, email;
    EditText amount;
    private final static String FILENAME = "settings.txt";
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        mEditText = (EditText) findViewById(R.id.rounds);

        Bundle arguments = getIntent().getExtras();


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        amount = findViewById(R.id.rounds);

        if (arguments!=null){
            name.setText(arguments.get("name").toString());
            email.setText(arguments.get("email").toString());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_open:
                openFile(FILENAME);
                return true;
            case R.id.action_save:
                saveFile(FILENAME);
                return true;
            default:
                return true;
        }
    }

    private void openFile(String fileName){
        try {
            InputStream inputStream = openFileInput(fileName);
            if (inputStream != null){
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuffer builder = new StringBuffer();
                while ((line = reader.readLine()) != null){
                    builder.append(line + "\n");
                }
                inputStream.close();
                //
                mEditText.setText(builder.toString());
            }

        } catch(Throwable t) {
            try {
                OutputStream outputStream = openFileOutput(fileName, 0);
                OutputStreamWriter osw = new OutputStreamWriter((outputStream));
                osw.write(mEditText.getText().toString());
                osw.close();
            }catch (Throwable th){
                Toast.makeText(getApplicationContext(), "Exception: " + th.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void saveFile(String fileName){
        try {
            OutputStream outputStream = openFileOutput(fileName, 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(mEditText.getText().toString());
            osw.close();
        }catch (Throwable t){
            Toast.makeText(getApplicationContext(), "Exception: " + t.toString(), Toast.LENGTH_LONG).show();

        }
    }

    public void runGame(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("email", email.getText().toString());
        intent.putExtra("amount", amount.getText().toString());
        startActivity(intent);
    }
}