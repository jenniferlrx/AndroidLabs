package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {
    Toolbar tbar;
    String msg = "You clicked on the overflow menu ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        tbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(tbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "This is the initial message ", Toast.LENGTH_LONG).show();
                break;
            case R.id.item2:
                dialogFun();
                break;
            case R.id.item3:
                Snackbar sb = Snackbar.make(tbar, "Go back?", Snackbar.LENGTH_LONG);
                sb.setAction("YES", v -> finish());
                sb.show();
                break;
            case R.id.item4:
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                break;

        }
        return true;
    }

    public void dialogFun() {

        View middle = getLayoutInflater().inflate(R.layout.dialog_extra,null);
        EditText message = middle.findViewById(R.id.dialog_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setPositiveButton("save", (dialog, id)-> {
            msg = message.getText().toString();

        })
                .setNegativeButton("cancel",(dialog, id)-> msg.toString())
                .setView(middle);
        builder.create().show();
    }

}

