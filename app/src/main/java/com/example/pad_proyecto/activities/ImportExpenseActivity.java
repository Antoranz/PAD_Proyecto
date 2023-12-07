package com.example.pad_proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.utils.Controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;



public class ImportExpenseActivity extends AppCompatActivity {

    Button ImportarDatos;

    private static final int PICK_JSON_FILE_REQUEST = 1;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        ImportarDatos = findViewById(R.id.jsonButtonImport);
        ImportarDatos.setOnClickListener(v -> {
            openJsonFilePicker();



        });

    }

    private void openJsonFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");

        Uri initialUri = Uri.parse("content://com.android.externalstorage.documents/document/primary:Download/SpendWise");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri);

        jsonFileLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> jsonFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            List<Expense> expensesImportados = readAndConvertJsonFile(uri);
                            for (Expense expense : expensesImportados) {
                                Controller.getInstance().addExpense(expense, this);
                                Log.d("EXPENSE", expense.toString());
                            }
                            Toast.makeText(this, getString(R.string.q_json_i), Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
    );

    private List<Expense> readAndConvertJsonFile(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Expense>>() {}.getType();
            return gson.fromJson(stringBuilder.toString(), listType);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }







}
