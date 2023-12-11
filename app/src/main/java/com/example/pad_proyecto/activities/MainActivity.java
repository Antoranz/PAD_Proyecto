package com.example.pad_proyecto.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pad_proyecto.R;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.NavigationManager;

public class MainActivity extends AppCompatActivity {
    Button historyButton, addButton, exportButton, deleteButton, statisticButton, importButton , resetButton;
    TextView budgetText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            NavigationManager.getInstance().navigateToHistory(this);
        });
        addButton = findViewById(R.id.addExpenseButton);
        addButton.setOnClickListener(v -> {
            NavigationManager.getInstance().navigateToAddExpense(this);
        });
        deleteButton = findViewById(R.id.deleteExpenseButton);
        deleteButton.setOnClickListener(v -> {
            NavigationManager.getInstance().navigateToDeleteExpense(this);
        });
        exportButton = findViewById(R.id.buttonExport);
        exportButton.setOnClickListener(v -> {
            NavigationManager.getInstance().navigateToExportView(this);
        });
        statisticButton = findViewById(R.id.buttonStatistics);
        statisticButton.setOnClickListener(v -> {
            NavigationManager.getInstance().navigateToStatisticView(this);
        });
        importButton = findViewById(R.id.ImportButton);
        importButton.setOnClickListener(v -> {
            NavigationManager.getInstance().navigateToImportView(this);
        });
        resetButton = findViewById(R.id.buttonReset);
        resetButton.setOnClickListener(v -> {
            NavigationManager.getInstance().navigateToResetView(this);
        });
        budgetText = findViewById(R.id.textViewPresupuesto);
        Double budget = Controller.getInstance().getBudget();
        if(budget == null){
            budgetText.setText("-");
        }else{
            budgetText.setText(budget.toString());
        }
    }
}