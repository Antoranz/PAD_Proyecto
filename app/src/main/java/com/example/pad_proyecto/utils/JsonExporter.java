package com.example.pad_proyecto.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.pad_proyecto.data.Expense;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonExporter {

    public static void exportExpensesToJson(Context context, List<Expense> expenses, String fileName) {
        // Convertir la lista de gastos a formato JSON
        String jsonExpenses = convertExpensesListToJson(expenses);

        // Guardar el JSON en un archivo
        saveJsonToExternalStorage(context, jsonExpenses, fileName);
    }

    private static String convertExpensesListToJson(List<Expense> expenses) {
        Gson gson = new Gson();
        return gson.toJson(expenses);
    }

    private static void saveJsonToExternalStorage(Context context, String jsonExpenses, String fileName) {
        String filePath = null;

        try {
            // Crear un directorio para almacenar archivos si no existe
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SpendWise");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Crear el archivo JSON
            File file = new File(directory, fileName);


            try (FileWriter writer = new FileWriter(file)) {
                writer.write(jsonExpenses);
                filePath = file.getAbsolutePath();
            }

            // Escanea el archivo para que aparezca en el explorador de archivos
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);



        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar el archivo JSON", Toast.LENGTH_SHORT).show();
        }



        if (filePath != null) {
            Toast.makeText(context, "Archivo JSON guardado con éxito", Toast.LENGTH_SHORT).show();
        }

    }
}
