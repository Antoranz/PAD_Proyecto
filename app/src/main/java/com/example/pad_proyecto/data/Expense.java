package com.example.pad_proyecto.data;

import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense implements Serializable {
    private long id;
    private long userId;
    private String expenseName;
    private Double moneySpent;
    private Date timeDate;
    private String imagePath;
    private ExpenseType category;
    private PayMethod payMethod;
    private String note;
    public Expense(long ui, String e, Double m, Date d, String path, ExpenseType c, PayMethod p, String n) {
        expenseName = e;
        moneySpent = m;
        timeDate = d;
        imagePath = path;
        category = c;
        payMethod = p;
        note = n;
        userId = ui;
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

    public Date getTimeDate() {
        return timeDate;
    }

    public String writeDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(timeDate.getTime());
    }
    public long getExpenseId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserId() {return userId;}
    public void update(Expense eActualizado) {
        this.expenseName = eActualizado.getExpenseName();
        this.moneySpent = eActualizado.getMoneySpent();
        this.timeDate = eActualizado.getTimeDate();
        this.imagePath = eActualizado.getImagePath();
        this.category = eActualizado.getCategory();
        this.payMethod = eActualizado.getPayMethod();
        this.note = eActualizado.getNote();
    }
}

