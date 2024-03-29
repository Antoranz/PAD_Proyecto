package com.example.pad_proyecto.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable {
    private long id;
    private String userName;
    private Double totalMoneySpent = 0.0;
    private Double budget;
    private List<Expense> expenseList;

    public User(String userName) {
        this.userName = userName;
        this.budget = null;
    }
    public void initInfo(List<Expense> el) {
        expenseList = el;
    }
    public long getId(){return id;}
    public void setId(long id){this.id= id;}
    public String getUserName() {
        return userName;
    }
    public Double getTotalMoneySpent() {
        return totalMoneySpent;
    }
    public void setTotalMoneySpent(Double tm){
        totalMoneySpent=tm;
    }
    public void setBudget(Double b) {
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
        if(budget!=null){
            budget-=e.getMoneySpent();
        }
        totalMoneySpent += e.getMoneySpent();
        expenseList.add(e);
        expenseList.sort((expense1, expense2) -> expense1.getTimeDate().compareTo(expense2.getTimeDate()));
    }
    public void deleteExpense(Expense e) {
        totalMoneySpent -= e.getMoneySpent();
        expenseList.remove(e);
        if(budget!=null){
            budget+=e.getMoneySpent();
        }
    }
    public void deleteExpenses(LinkedList<Expense> e) {
        for(int i=0;i<e.size();i++){
            totalMoneySpent -= e.get(i).getMoneySpent();
            expenseList.remove(e);
            if(budget!=null){
                budget+=e.get(i).getMoneySpent();
            }
        }
    }
    public void editExpense(Expense e, Expense eActualizado) {
        totalMoneySpent -= e.getMoneySpent();
        totalMoneySpent += eActualizado.getMoneySpent();
        for (Expense eAux:expenseList ) {
            if(eAux.getExpenseId() == e.getExpenseId()){
                eAux.update(eActualizado);
            }
        }
        if(budget!=null){
            budget-=(eActualizado.getMoneySpent()-e.getMoneySpent());
        }
        e.update(eActualizado);
    }
    public void reset() {
        totalMoneySpent = 0.0;
        budget = 0.0;
        expenseList = null;
    }
}

