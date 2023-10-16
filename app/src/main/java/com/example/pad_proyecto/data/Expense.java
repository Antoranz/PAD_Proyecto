package com.example.pad_proyecto.data;

import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Expense {

    private String expenseName;
    private Double moneySpent;
    private Calendar timeDate;
    private String imagePath;
    private ExpenseType category;
    private PayMethod payMethod;
    private String note;

    public Expense(String e, Double m, Calendar d, String path, ExpenseType c, PayMethod p, String n) {
        expenseName = e;
        moneySpent = m;
        timeDate = d;
        imagePath = path;
        category = c;
        payMethod = p;
        note = n;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getNote() {
        return note;
    }

    public ExpenseType getCategory() {
        return category;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public Double getMoneySpent() {
        return moneySpent;
    }

    public Calendar getTimeDate() {
        return timeDate;
    }

    public String writeDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(timeDate.getTime());
    }
}

