package com.example.pad_proyecto.databases;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;

import java.util.List;

public interface ExpenseDAO {
    public void addExpense(@NonNull Expense expense);
    public void updateExpense(@NonNull Expense expense);
    public void deleteExpense(long expenseId);
    public List<Expense> getAllExpenses();
    public void clearExpenseTable();
    public List<Expense> getExpensesByExpenseType(@NonNull ExpenseType expenseType);
    public List<Expense> getExpensesByPayMethod(@NonNull PayMethod payMethod);

}
