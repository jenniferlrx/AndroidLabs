package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    ImageButton mImageButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Intent dataFromPreviousPage = getIntent();
        String email = dataFromPreviousPage.getStringExtra("email");

        EditText email_edit = findViewById(R.id.profile_email);
        email_edit.setText(email);

        mImageButton = findViewById(R.id.profile_img);

        if(mImageButton != null)
            mImageButton.setOnClickListener(v -> {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            });
        Log.i(ACTIVITY_NAME, "In function onCreate" );

        Button chatBtn = findViewById(R.id.profile_chat);
        chatBtn.setOnClickListener(v->{
            Intent goChat = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            startActivity(goChat);
        });

        Button weatherBtn = findViewById(R.id.profile_weather);
        weatherBtn.setOnClickListener(v->{
            Intent goWeather = new Intent(ProfileActivity.this, WeatherForcast.class);
            startActivity(goWeather);
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In function onStart" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In function onResume" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In function onPause" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In function onStop" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In function onDestroy" );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        Log.i(ACTIVITY_NAME, "In function onActivityResult" );
    }

}

