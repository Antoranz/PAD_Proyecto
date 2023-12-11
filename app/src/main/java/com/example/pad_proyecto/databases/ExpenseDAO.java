package com.example.pad_proyecto.databases;

import androidx.annotation.NonNull;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;
import java.util.LinkedList;
import java.util.List;

public interface ExpenseDAO {
    public void addExpense(@NonNull Expense expense);
    public void updateExpense(@NonNull Expense expense);
    public void deleteExpense(long expenseId);
    public LinkedList<Expense> getAllExpenses(long uId);
    public void clearExpenseTable();
    public List<Expense> getExpensesByExpenseType(@NonNull ExpenseType expenseType);
    public List<Expense> getExpensesByPayMethod(@NonNull PayMethod payMethod);

}
