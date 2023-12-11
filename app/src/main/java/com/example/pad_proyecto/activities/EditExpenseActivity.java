package com.example.pad_proyecto.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.NavigationManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class EditExpenseActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String imageName;
    private String selectedImagePath;
    private boolean imageSelected;
    private Expense e;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        initUI();
    }
    private void initUI() {

        if (android.os.Build.VERSION.SDK_INT >= 31) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                e = (Expense) extras.getSerializable("expense");
            }
        } else {
            e = null;
        }

        // Obtener referencias a las vistas
        EditText expenseName = findViewById(R.id.expenseText);
        EditText moneySpent = findViewById(R.id.moneySpent);
        EditText expenseDate = findViewById(R.id.expenseDate);
        Button saveButton = findViewById(R.id.saveButton);
        ImageView galleryImage = findViewById(R.id.galleryImageView);
        Spinner categorySpinner = findViewById(R.id.expenseCategory);
        Spinner PayMethodSpinner = findViewById(R.id.expenseMethod);
        EditText noteText = findViewById(R.id.noteText);

        expenseName.setText(e.getExpenseName());
        expenseDate.setText(e.writeDate());
        moneySpent.setText(e.getMoneySpent().toString());
        noteText.setText(e.getNote());

        String imagen = e.getImagePath();
        String imagePath = this.getFilesDir() + "/" + imagen;
        File imgFile = new File(imagePath);
        if(imagen != null){
            if(imgFile.exists()){
                Uri imgUri = Uri.fromFile(imgFile);
                Glide.with(this)
                        .load(imgUri)
                        .into(galleryImage);
            }
        }
        // Manejador para el botón de seleccionar imagen
        galleryImage.setOnClickListener(v -> openImagePicker());
        // Manejador para el botón de guardar
        saveButton.setOnClickListener(v -> handleSaveExpense(
                expenseName.getText().toString(),
                expenseDate.getText().toString(),
                moneySpent.getText().toString(),
                (String) PayMethodSpinner.getSelectedItem(),
                (String) categorySpinner.getSelectedItem(),
                noteText.getText().toString(),
                imgFile
        )
        );

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            if (uri != null) {
                                String fileName = getImageFileNameFromUri(uri);
                                imageName = fileName;
                                selectedImagePath = uri.toString();
                                Bitmap imageBitmap = getBitmapFromUri(this, uri);
                                galleryImage.setImageBitmap(imageBitmap);
                                imageSelected = true;
                            }
                        }
                    }
                });

        // Inicializar el spinner con las categorías
        ExpenseType[] categoryValues = ExpenseType.values();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.stream(categoryValues)
                .map(Enum::name)
                .toArray(String[]::new));
        categorySpinner.setAdapter(adapter);

        int selectedIndex = Arrays.asList(categoryValues).indexOf(e.getCategory());

        if (selectedIndex != -1) {
            categorySpinner.setSelection(selectedIndex);
        }
        // Inicializar el spinner con los tipos de pago
        PayMethod[] PayMethodValues = PayMethod.values();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.stream(PayMethodValues)
                .map(Enum::name)
                .toArray(String[]::new));
        PayMethodSpinner.setAdapter(adapter2);

        selectedIndex = Arrays.asList(PayMethodValues).indexOf(e.getPayMethod());

        if (selectedIndex != -1) {
            PayMethodSpinner.setSelection(selectedIndex);
        }
    }

    private void handleSaveExpense(String expenseName,String expenseDate, String moneySpent, String selectedPayMethod, String selectedCategory, String note, File imgFile) {
        // Validar los datos del gasto antes de guardar
        if (expenseName.isEmpty()) {
            String warning = getString(R.string.q_gasto);
            showWarningDialog(warning);
        } else if (moneySpent.isEmpty() || Double.parseDouble(moneySpent) < 0) {
            String warning = getString(R.string.q_ms);
            showWarningDialog(warning);
        } else {
            // Crear el objeto Expense
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = null;
            try {
                fecha = formato.parse(expenseDate);
                String result;
                if (imageSelected) {
                    result = imageName;
                    if(imgFile.exists()){
                        imgFile.delete();
                    }
                } else {
                    result = e.getImagePath();
                }
                ExpenseType selectedCategoryEnum = ExpenseType.valueOf(selectedCategory);
                PayMethod selectedPaymentMethod =  PayMethod.valueOf(selectedPayMethod);
                Expense newExpense = new Expense(
                        Controller.getInstance().getUser().getId(),
                        expenseName,
                        Double.parseDouble(moneySpent),
                        fecha,
                        result,
                        selectedCategoryEnum,
                        selectedPaymentMethod,
                        note
                );
                if (imageSelected) {
                    if (imageName != null) {
                        if (selectedImagePath != null) {
                            copyImageToAppDataDirectory(Uri.parse(selectedImagePath), imageName);
                        }
                    }
                }
                // Guardar el gasto modificado y modificarlo en la lista
                Controller.getInstance().editExpense( this,e,newExpense);
                // Navegar a la actividad main activity
                NavigationManager.getInstance().navigateToMenuView(this);
            } catch (ParseException e) {
                String warning = getString(R.string.q_fecha);
                showWarningDialog(warning);
            }

        }
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void copyImageToAppDataDirectory(Uri sourceUri, String targetFileName) {
        try {
            InputStream sourceStream = getContentResolver().openInputStream(sourceUri);
            File targetPath = new File(getFilesDir(), targetFileName);
            FileOutputStream targetStream = new FileOutputStream(targetPath);

            try (InputStream input = sourceStream; OutputStream output = targetStream) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getImageFileNameFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (displayNameIndex != -1) {
                    String displayName = cursor.getString(displayNameIndex);
                    if (displayName != null) {
                        return displayName;
                    }
                }
            }
            cursor.close();
        }
        return "expense_image.jpg"; // Valor por defecto si no se puede obtener el nombre
    }
    private void showWarningDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.advertencia))
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
