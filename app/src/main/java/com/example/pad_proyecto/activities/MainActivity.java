package com.example.pad_proyecto.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.databases.ExpenseDAO;
import com.example.pad_proyecto.databases.ExpenseDAOImp;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExpenseDAO dao = ExpenseDAOImp.getInstance(this);
        User u = new User("Victor");
        dao.addUser(u);
        Expense e = new Expense(u.getId(),"Compra",15.2, Calendar.getInstance(), null, ExpenseType.Alimentacion, PayMethod.Efectivo, "");
        dao.addExpense(e);
    }

}