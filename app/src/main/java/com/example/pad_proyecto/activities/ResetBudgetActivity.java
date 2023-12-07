package com.example.pad_proyecto.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pad_proyecto.R;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.NavigationManager;


public class ResetBudgetActivity extends AppCompatActivity {


    Button resetButton;
    EditText editText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_budget);

        editText = findViewById(R.id.editTextPresupuesto);
        editText.setText(Controller.getInstance().getBudget().toString());
        resetButton = findViewById(R.id.buttonResetBudget);
        resetButton.setOnClickListener(v -> {

            Controller.getInstance().setBudget(this,Double.parseDouble(String.valueOf(editText.getText())));
            NavigationManager.getInstance().navigateToMenuView(this);

        });


    }
}
