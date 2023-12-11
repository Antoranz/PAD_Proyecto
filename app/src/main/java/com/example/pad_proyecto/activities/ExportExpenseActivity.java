package com.example.pad_proyecto.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.ExcelExporter;
import com.example.pad_proyecto.utils.JsonExporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class ExportExpenseActivity extends AppCompatActivity {

    Button jsonButton;
    Button excelButton;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        jsonButton = findViewById(R.id.jsonButton);
        jsonButton.setOnClickListener(v -> {
            List<Expense> expenses = Controller.getInstance().getUser().getExpensesList();
            JsonExporter.exportExpensesToJson(this, expenses, "expenses.json");

            Toast.makeText(this, getString(R.string.q_json), Toast.LENGTH_SHORT).show();

        });
        excelButton = findViewById(R.id.xlsxButton);
        excelButton.setOnClickListener(v -> {
            try{
                List<Expense> expenses = Controller.getInstance().getAllExpenses(this);
                String filename = "expenses.xlsx";
                ExcelExporter.exportExpensesToExcel(this, expenses, filename);
                Toast.makeText(this, getString(R.string.q_excel), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this, getString(R.string.q_error_excel), Toast.LENGTH_SHORT).show();
            }
        });
    }
}