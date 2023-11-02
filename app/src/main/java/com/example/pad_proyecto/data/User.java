package com.example.pad_proyecto.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private long id;
    private String userName;
    private double totalMoneySpent = 0.0;
    private Double budget;
    private List<Expense> expenseList;

    public User(String userName) {
        this.userName = userName;
    }

    public void initInfo(List<Expense> el) {
        expenseList = el;
    }
    public long getId(){return id;}
    public void setId(long id){this.id= id;}
    public String getUserName() {
        return userName;
    }

    public double getTotalMoneySpent() {
        return totalMoneySpent;
    }

    public void setTotalMoneySpent(double tm){
        totalMoneySpent=tm;
    }

    public void setBudget(double b) {
        budget = b;
    }

    public Double getBudget() {
        return budget;
    }

    public List<Expense> getExpensesList() {
        return expenseList;
    }

    public void addExpense(Expense e) {
        if (expenseList == null) {
            expenseList = new ArrayList<>();
        }

        totalMoneySpent += e.getMoneySpent();
        expenseList.add(e);
        expenseList.sort((expense1, expense2) -> expense1.getTimeDate().compareTo(expense2.getTimeDate()));
    }

    public void deleteExpense(Expense e) {
        totalMoneySpent -= e.getMoneySpent();
        expenseList.remove(e);
    }

    public void reset() {
        totalMoneySpent = 0.0;
        budget = 0.0;
        expenseList = null;
    }
}

