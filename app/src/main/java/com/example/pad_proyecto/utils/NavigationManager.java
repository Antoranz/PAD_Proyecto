package com.example.pad_proyecto.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.pad_proyecto.activities.AddUserActivity;
import com.example.pad_proyecto.activities.MainActivity;
import com.example.pad_proyecto.activities.OpenActivity;
import com.example.pad_proyecto.data.Expense;

public class NavigationManager {
    private static NavigationManager instance;

    private NavigationManager() {}

    public static synchronized NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        instance = new NavigationManager();
        return instance;
    }

    public void navigateToExpenseView(Context context, Expense expense) {
        /*if (context != null) {
            Intent intent = new Intent(context, ExpenseViewActivity.class);
            intent.putExtra("expense", expense);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla de ExpenseView");
        }*/
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
        /*if (context != null) {
            Intent intent = new Intent(context, HistoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.d("TAG","Error al cambiar a la pantalla del Historial");
        }*/
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

    public void navigateToEditExpenseView(Context context){

    }
}
