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
        if(Controller.getInstance().getBudget()!=null){
            editText.setText(Controller.getInstance().getBudget().toString());
        }
        resetButton = findViewById(R.id.buttonResetBudget);
        resetButton.setOnClickListener(v -> {
            if(!editText.getText().toString().isEmpty()){
                Controller.getInstance().setBudget(this,Double.parseDouble(editText.getText().toString()));
            }else{
                Controller.getInstance().setBudget(this,null);
            }
            NavigationManager.getInstance().navigateToMenuView(this);
        });
    }
}
