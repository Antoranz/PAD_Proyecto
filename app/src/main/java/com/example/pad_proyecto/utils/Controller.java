package com.example.pad_proyecto.utils;

import android.content.Context;

import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.databases.DAOImp;
import com.example.pad_proyecto.databases.ExpenseDAO;
import com.example.pad_proyecto.databases.UserDAO;

public class Controller {
    private User u;
    static Controller instance;
    public static Controller getInstance(){
        if(instance!=null){
            return instance;
        }
        return new Controller();
    }

    public void setUser(User u,Context c){
        this.u = u;
        UserDAO dao = DAOImp.getInstance(c);
        dao.addUser(u);
    }

    public void addExpense(Expense e, Context c){
        u.addExpense(e);
        ExpenseDAO dao = DAOImp.getInstance(c);
        dao.addExpense(e);
    }
}
