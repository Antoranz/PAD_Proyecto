package com.example.pad_proyecto.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.ExcelExporter;

import java.util.List;


public class ExportExpenseActivity extends AppCompatActivity {

    Button jsonButton;
    Button excelButton;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        jsonButton = findViewById(R.id.jsonButton);
        jsonButton.setOnClickListener(v -> {

        });
        excelButton = findViewById(R.id.xlsxButton);
        excelButton.setOnClickListener(v -> {
            List<Expense> expenses = Controller.getInstance().getUser().getExpensesList();
            String filename = "expenses.xlsx";
            ExcelExporter.exportExpensesToExcel(this, expenses, filename);
        });
    }
}