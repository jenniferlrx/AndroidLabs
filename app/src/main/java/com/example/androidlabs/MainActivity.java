package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText password = findViewById(R.id.passwordInput);
        EditText email = findViewById(R.id.emailInput);

        Button logButton = findViewById(R.id.login);

        if(logButton != null)
            logButton.setOnClickListener(v -> {
                Intent goProfile = new Intent(MainActivity.this, ProfileActivity.class);

                goProfile.putExtra("email", email.getText().toString());
                startActivity(goProfile);
            });



        SharedPreferences prefs = getSharedPreferences("FileName", MODE_PRIVATE);
        String previous = prefs.getString("ReserveName", "");

        email.setText(previous);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText email = findViewById(R.id.emailInput);

        SharedPreferences prefs = getSharedPreferences("FileName", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName", email.getText().toString());

        editor.commit();
    }
}
