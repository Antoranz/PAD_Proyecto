package com.example.pad_proyecto.utils;

import android.content.Context;

import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.databases.DAOImp;
import com.example.pad_proyecto.databases.ExpenseDAO;
import com.example.pad_proyecto.databases.UserDAO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    private User u;
    static Controller instance;
    public static Controller getInstance(){
        if(instance!=null){
            return instance;
        }
        instance = new Controller();
        return instance;
    }

    public void addUser(User u,Context c){
        this.u = u;
        UserDAO dao = DAOImp.getInstance(c);
        dao.addUser(u);
    }

    public void addExpense(Expense e, Context c){
        u.addExpense(e);
        ExpenseDAO dao = DAOImp.getInstance(c);
        dao.addExpense(e);
    }
    public void initInfo(Context c){
        UserDAO dao = DAOImp.getInstance(c);
        List<User> userList = dao.getAllUser();
        if(userList.size()>0){
            u = userList.get(0);
            ExpenseDAO daoE = DAOImp.getInstance(c);
            u.initInfo(daoE.getAllExpenses(u.getId()));
        }

    }
    public LinkedList<Expense> searchExpense(String s, Context c){
        LinkedList<Expense> expensesList = new LinkedList<>();
        for(Expense e:u.getExpensesList()){
            if(e.getExpenseName().toLowerCase().contains(s.toLowerCase())){
                expensesList.add(e);
            }
        }
        return expensesList;
    }
    public User getUser(){
        return u;
    }
}
