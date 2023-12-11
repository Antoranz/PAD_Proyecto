package com.example.pad_proyecto.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import com.example.pad_proyecto.R;
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
        headerRow.createCell(1).setCellValue(context.getString(R.string.view_fecha).replace(":",""));
        headerRow.createCell(2).setCellValue(context.getString(R.string.view_gasto).replace(":",""));
        headerRow.createCell(3).setCellValue(context.getString(R.string.view_dinero_gastado).replace(":",""));
        headerRow.createCell(4).setCellValue(context.getString(R.string.view_categoria).replace(":",""));
        headerRow.createCell(5).setCellValue(context.getString(R.string.view_tipo_de_pago).replace(":",""));
        headerRow.createCell(6).setCellValue(context.getString(R.string.view_nota).replace(":",""));


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

        String filePath = saveWorkbookToExternalStorage(context, workbook, fileName);

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
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

}

