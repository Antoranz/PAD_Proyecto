package com.example.pad_proyecto.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pad_proyecto.R;

import java.io.File;

public class FullScreenActivity extends AppCompatActivity {


    private ImageView img;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreenimageview);
        String imagen = getIntent().getStringExtra("path");


        img = findViewById(R.id.imageView2);
        if(imagen != null){

            String imagePath = this.getFilesDir() + "/" + imagen;
            File imgFile = new File(imagePath);

            if(imgFile.exists()){
                Uri imgUri = Uri.fromFile(imgFile);

                Glide.with(this)
                        .load(imgUri)
                        .into(img);




            }

        }
    }
}
