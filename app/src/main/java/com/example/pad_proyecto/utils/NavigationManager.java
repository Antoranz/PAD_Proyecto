package com.example.pad_proyecto.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.pad_proyecto.activities.AddExpenseActivity;
import com.example.pad_proyecto.activities.AddUserActivity;
import com.example.pad_proyecto.activities.DeleteExpenseActivity;
import com.example.pad_proyecto.activities.EditExpenseActivity;
import com.example.pad_proyecto.activities.ExpenseViewActivity;
import com.example.pad_proyecto.activities.ExportExpenseActivity;
import com.example.pad_proyecto.activities.FullScreenActivity;
import com.example.pad_proyecto.activities.ImportExpenseActivity;
import com.example.pad_proyecto.activities.ListActivity;
import com.example.pad_proyecto.activities.MainActivity;
import com.example.pad_proyecto.activities.OpenActivity;
import com.example.pad_proyecto.activities.StatisticsActivity;
import com.example.pad_proyecto.data.Expense;

public class NavigationManager {
    private static NavigationManager instance;

    private NavigationManager() {}

    public static synchronized NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    public void navigateToExpenseView(Context context, Expense expense) {
        if (context != null) {
            Intent intent = new Intent(context, ExpenseViewActivity.class);
            intent.putExtra("expense", expense);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla de ExpenseView");
        }
    }
    public void navigateToMenuView(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla de ExpenseView");
        }
    }
    public void navigateToHistory(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, ListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla del Historial");
        }
    }

    public void navigateToExportView(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, ExportExpenseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla de Export");
        }
    }

    public void navigateToImportView(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, ImportExpenseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla de Import");
        }
    }
    public void navigateToAddExpense(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, AddExpenseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla del Añadir gasto");
        }
    }

    public void navigateToAddUserView(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, AddUserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla de AddUser");
        }
    }

    public void navigateToEditExpenseView(Context context, Expense e){

        if (context != null) {
            Intent intent = new Intent(context, EditExpenseActivity.class);
            intent.putExtra("expense", e);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla de AddUser");
        }

    }

    public void navigateToFullScreenView(Context context,String imgPath){

        if (context != null) {
            Intent intent = new Intent(context, FullScreenActivity.class);
            intent.putExtra("path", imgPath);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla de AddUser");
        }

    }
    public void navigateToDeleteExpense(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, DeleteExpenseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla del Historial");
        }

    }

    public void navigateToStatisticView(Context context) {

        if (context != null) {
            Intent intent = new Intent(context, StatisticsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla de AddUser");
        }

    }
}
