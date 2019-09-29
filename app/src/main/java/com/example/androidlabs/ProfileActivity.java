package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    ImageButton mImageButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Intent dataFromPreviousPage = getIntent();
        String email = dataFromPreviousPage.getStringExtra("email");

        EditText email_edit = findViewById(R.id.profile_email);
        email_edit.setText(email);

        ImageButton mImageButton = findViewById(R.id.profile_img);

        if(mImageButton != null)
            mImageButton.setOnClickListener(v -> {
                Intent goProfile = new Intent(ProfileActivity.this, ProfileActivity.class);


            });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

}

