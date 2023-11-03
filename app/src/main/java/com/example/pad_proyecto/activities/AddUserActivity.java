package com.example.pad_proyecto.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.NavigationManager;

public class AddUserActivity extends AppCompatActivity {

    private EditText name;
    private EditText budget;
    private Button iniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        name = findViewById(R.id.editTextName);
        budget = findViewById(R.id.editTextNumberDecimal);
        iniciar =findViewById(R.id.buttonIniciar);

        iniciar.setOnClickListener(v -> {
            if(!name.getText().toString().equals("")) {
                User u = new User(name.getText().toString());
                if(!budget.getText().toString().equals("")) u.setBudget(Double.parseDouble(budget.getText().toString()));
                Controller.getInstance().addUser(u, this);
                NavigationManager.getInstance().navigateToMenuView(this);
            }else {
                Toast.makeText(this,"El nombre no puede ser un campo no vacio", Toast.LENGTH_LONG).show();
            }
        });
    }

}