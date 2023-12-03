package com.example.pad_proyecto.utils;

import android.content.Context;
import android.util.Pair;

import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.databases.DAOImp;
import com.example.pad_proyecto.databases.ExpenseDAO;
import com.example.pad_proyecto.databases.UserDAO;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;

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
    public LinkedList<Expense> searchExpense(String s, Context c, String et, String pm){
        LinkedList<Expense> expensesList = new LinkedList<>();
        for(Expense e:u.getExpensesList()){
            if((" " + e.getExpenseName()).toLowerCase().contains(" "+s.toLowerCase()) && (et.equals("Todos") || e.getCategory().toString().equals(et)) && (pm.equals("Todos") || e.getPayMethod().toString().equals(pm))){
                expensesList.add(e);
            }
        }
        return expensesList;
    }
    public User getUser(){
        return u;
    }

    public void deleteExpense(Context c,Expense e) {
        u.deleteExpense(e);
        ExpenseDAO dao = DAOImp.getInstance(c);
        dao.deleteExpense(e.getExpenseId());
    }
    public LinkedList<Expense> getAllExpenses(Context c) {
        ExpenseDAO dao = DAOImp.getInstance(c);
        return dao.getAllExpenses(u.getId());
    }
    public void editExpense(Context c,Expense e , Expense eActualizado) {
        u.editExpense(e, eActualizado);
        ExpenseDAO dao = DAOImp.getInstance(c);
        dao.updateExpense(e);
    }

    public List<Pair<String,Double>> showCategoryStatistics(Context c){
        List<Pair<String,Double>> list = new ArrayList<>();
        ExpenseDAO dao = DAOImp.getInstance(c);
        for(ExpenseType type: ExpenseType.values()){
            double suma = 0.0;
            for(Expense e : dao.getExpensesByExpenseType(type)){
                suma += e.getMoneySpent();
            }
            list.add(new Pair(type.toString(),suma));
        }
        return list;
    }

    public List<Pair<String,Double>> showPayMethodStatistics(Context c){
        List<Pair<String,Double>> list = new ArrayList<>();
        ExpenseDAO dao = DAOImp.getInstance(c);
        for(PayMethod type: PayMethod.values()){
            double suma = 0.0;
            for(Expense e : dao.getExpensesByPayMethod(type)){
                suma += e.getMoneySpent();
            }
            list.add(new Pair(type.toString(),suma));
        }
        return list;
    }
    public void deleteExpenses(Context context, LinkedList<Expense> expenses) {
        u.deleteExpenses(expenses);
        ExpenseDAO dao = DAOImp.getInstance(context);
        for(Expense e : expenses){
        dao.deleteExpense(e.getExpenseId());
        }
    }
}

