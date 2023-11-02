package com.example.pad_proyecto.activities;

import android.database.ContentObservable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;
import com.example.pad_proyecto.utils.Controller;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}