package com.example.pad_proyecto.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.example.pad_proyecto.data.Expense;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter {

    public static void exportExpensesToExcel(Context context, List<Expense> expenses, String fileName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expenses");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Fecha");
        headerRow.createCell(2).setCellValue("Nombre del Gasto");
        headerRow.createCell(3).setCellValue("Dinero gastado");
        headerRow.createCell(4).setCellValue("Categoría");
        headerRow.createCell(5).setCellValue("Metodo de Pago");
        headerRow.createCell(6).setCellValue("Nota");


        int rowNum = 1;
        for (Expense expense : expenses) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(expense.getExpenseId());
            row.createCell(1).setCellValue(expense.writeDate());
            row.createCell(2).setCellValue(expense.getExpenseName());
            row.createCell(3).setCellValue(expense.getMoneySpent());
            row.createCell(4).setCellValue(expense.getCategory().toString());
            row.createCell(5).setCellValue(expense.getPayMethod().toString());
            row.createCell(6).setCellValue(expense.getNote());
        }

        // Guarda el libro de trabajo en un archivo
        String filePath = saveWorkbookToExternalStorage(context, workbook, fileName);

        if (filePath != null) {
            openExcelFile(context, filePath);
        } else {
            Toast.makeText(context, "Error al guardar el archivo Excel", Toast.LENGTH_SHORT).show();
        }
        // Cierra el libro de trabajo
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String saveWorkbookToExternalStorage(Context context, Workbook workbook, String fileName) {
        String filePath = null;

        try {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SpendWise");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, fileName);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                filePath = file.getAbsolutePath();
            }

            // Escanea el archivo para que aparezca en el explorador de archivos
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    private static void openExcelFile(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + filePath);
        intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "Open with"));
        } else {
            Toast.makeText(context, "No hay aplicaciones instaladas para abrir archivos Excel", Toast.LENGTH_SHORT).show();
        }
    }
}

