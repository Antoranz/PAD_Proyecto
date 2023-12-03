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

import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Controller {
    private User u;
    static Controller instance;

    public static Controller getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new Controller();
        return instance;
    }

    public void addUser(User u, Context c) {
        this.u = u;
        UserDAO dao = DAOImp.getInstance(c);
        dao.addUser(u);
    }

    public void addExpense(Expense e, Context c) {
        u.addExpense(e);
        ExpenseDAO dao = DAOImp.getInstance(c);
        dao.addExpense(e);
    }

    public void initInfo(Context c) {
        UserDAO dao = DAOImp.getInstance(c);
        List<User> userList = dao.getAllUser();
        if (userList.size() > 0) {
            u = userList.get(0);
            ExpenseDAO daoE = DAOImp.getInstance(c);
            u.initInfo(daoE.getAllExpenses(u.getId()));
        }

    }

    public LinkedList<Expense> searchExpense(String s, Context c, String et, String pm) {
        LinkedList<Expense> expensesList = new LinkedList<>();
        for (Expense e : u.getExpensesList()) {
            if ((" " + e.getExpenseName()).toLowerCase().contains(" " + s.toLowerCase()) && (et.equals("Todos") || e.getCategory().toString().equals(et)) && (pm.equals("Todos") || e.getPayMethod().toString().equals(pm))) {
                expensesList.add(e);
            }
        }
        return expensesList;
    }

    public User getUser() {
        return u;
    }

    public void deleteExpense(Context c, Expense e) {
        u.deleteExpense(e);
        ExpenseDAO dao = DAOImp.getInstance(c);
        dao.deleteExpense(e.getExpenseId());
    }

    public LinkedList<Expense> getAllExpenses(Context c) {
        ExpenseDAO dao = DAOImp.getInstance(c);
        return dao.getAllExpenses(u.getId());
    }

    public void editExpense(Context c, Expense e, Expense eActualizado) {
        u.editExpense(e, eActualizado);
        ExpenseDAO dao = DAOImp.getInstance(c);
        dao.updateExpense(e);
    }

    public List<Pair<String, Double>> showCategoryStatistics(Context c) {
        List<Pair<String, Double>> list = new ArrayList<>();
        ExpenseDAO dao = DAOImp.getInstance(c);
        for (ExpenseType type : ExpenseType.values()) {
            double suma = 0.0;
            for (Expense e : dao.getExpensesByExpenseType(type)) {
                suma += e.getMoneySpent();
            }
            list.add(new Pair(type.toString(), suma));
        }
        return list;
    }

    public List<Pair<String, Double>> showPayMethodStatistics(Context c) {
        List<Pair<String, Double>> list = new ArrayList<>();
        ExpenseDAO dao = DAOImp.getInstance(c);
        for (PayMethod type : PayMethod.values()) {
            double suma = 0.0;
            for (Expense e : dao.getExpensesByPayMethod(type)) {
                suma += e.getMoneySpent();
            }
            list.add(new Pair(type.toString(), suma));
        }
        return list;
    }

    public List<Pair<String, Double>> showTimeStatistics(Context c) {
        List<Pair<String, Double>> list = new ArrayList<>();
        //Se encarga Oscar
        return list;
    }

    public void deleteExpenses(Context context, LinkedList<Expense> expenses) {
        u.deleteExpenses(expenses);
        ExpenseDAO dao = DAOImp.getInstance(context);
        for (Expense e : expenses) {
            dao.deleteExpense(e.getExpenseId());
        }
    }

    public String getHighestExpense() {

        Double maxSum = Double.MIN_VALUE;  // Inicializa con el valor mínimo posible
        String maxExpenseName = null;

        for (Expense e : u.getExpensesList()) {
            Double currentSum = e.getMoneySpent();
            String currentExpenseName = e.getExpenseName();

            if (currentSum > maxSum) {
                maxSum = currentSum;
                maxExpenseName = currentExpenseName;
            }

        }

        return maxExpenseName;
    }

    public List<String> getMostUsedPaymentMethod(Context c) {

        ExpenseDAO dao = DAOImp.getInstance(c);
        int maxSum = 0;
        List<String> mostUsedPaymentMethod = new ArrayList<>();

        for (PayMethod type : PayMethod.values()) {
            int sum = 0;

            for (Expense e : dao.getExpensesByPayMethod(type)) {
                sum += 1;
            }

            if (sum > maxSum) {
                maxSum = sum;
                mostUsedPaymentMethod.clear();  // Reinicia la lista si encuentras una nueva máxima
                mostUsedPaymentMethod.add(type.toString());
            } else if (sum == maxSum) {
                mostUsedPaymentMethod.add(type.toString());  // Agrega la categoría a la lista si tiene la misma suma máxima
            }
        }

        return mostUsedPaymentMethod;
    }

    public List<String> getMostUsedCategories(Context c) {

        ExpenseDAO dao = DAOImp.getInstance(c);
        int maxSum = 0;
        List<String> mostUsedCategories = new ArrayList<>();

        for (ExpenseType type : ExpenseType.values()) {
            int sum = 0;

            for (Expense e : dao.getExpensesByExpenseType(type)) {
                sum += 1;
            }

            if (sum > maxSum) {
                maxSum = sum;
                mostUsedCategories.clear();  // Reinicia la lista si encuentras una nueva máxima
                mostUsedCategories.add(type.toString());
            } else if (sum == maxSum) {
                mostUsedCategories.add(type.toString());  // Agrega la categoría a la lista si tiene la misma suma máxima
            }
        }

        return mostUsedCategories;
    }


    public String getYearWithMostExpenses() {

        Double maxSum = Double.MIN_VALUE;  // Inicializa con el valor mínimo posible

        String yearString = null;
        for (Expense e : u.getExpensesList()) {
            Double currentSum = e.getMoneySpent();
            // Crea un nuevo formato solo para el año
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

            // Convierte la fecha al formato deseado


            if (currentSum > maxSum) {
                maxSum = currentSum;

                yearString = yearFormat.format(e.getTimeDate());
            }

        }

        return yearString;
    }
}
