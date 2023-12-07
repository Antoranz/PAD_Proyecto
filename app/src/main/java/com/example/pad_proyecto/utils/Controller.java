package com.example.pad_proyecto.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Pair;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.Manifest;

import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.databases.DAOImp;
import com.example.pad_proyecto.databases.ExpenseDAO;
import com.example.pad_proyecto.databases.UserDAO;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;
import com.github.mikephil.charting.data.BarEntry;

import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        checkearNotificacion(c);
    }
    private void checkearNotificacion(Context c) {
        if (u.getBudget() != null) {
            if(u.getBudget()<0) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);
                if (ActivityCompat.checkSelfPermission(c, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    // Crear la notificación
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(c, "mi_notificacion_id")
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentTitle("¡Te has calentado!")
                            .setContentText("Tu presupuesto es inferior a 0")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true); // Cierra la notificación cuando se toca
                    notificationManager.notify(1, builder.build());
                }
            }
            else if(u.getBudget()<((u.getTotalMoneySpent()*10)/100)) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);
                if (ActivityCompat.checkSelfPermission(c, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    // Crear la notificación
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(c, "mi_notificacion_id")
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentTitle("¡Ten cuidado con la Cartera!")
                            .setContentText("Te queda menos del 10% de tu presupuesto")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true); // Cierra la notificación cuando se toca
                    notificationManager.notify(1, builder.build());
                }
            }
        }
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

    public List<Pair<String, Double>> showCategoryStatistics(Context c,String añoEstablecido) {
        List<Pair<String, Double>> list = new ArrayList<>();
        ExpenseDAO dao = DAOImp.getInstance(c);
        for (ExpenseType type : ExpenseType.values()) {
            double suma = 0.0;
            for (Expense e : dao.getExpensesByExpenseType(type)) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                int expenseYear = Integer.parseInt(dateFormat.format(e.getTimeDate()));

                // Verifica si el gasto es del año 2023
                if (expenseYear == Integer.parseInt(añoEstablecido)) {
                    suma += e.getMoneySpent();
                }
            }
            list.add(new Pair(type.toString(), suma));
        }
        return list;
    }

    public List<Pair<String, Double>> showPayMethodStatistics(Context c,String añoEstablecido) {
        List<Pair<String, Double>> list = new ArrayList<>();
        ExpenseDAO dao = DAOImp.getInstance(c);
        for (PayMethod type : PayMethod.values()) {
            double suma = 0.0;
            for (Expense e : dao.getExpensesByPayMethod(type)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                int expenseYear = Integer.parseInt(dateFormat.format(e.getTimeDate()));

                // Verifica si el gasto es del año 2023
                if (expenseYear == Integer.parseInt(añoEstablecido)) {
                    suma += e.getMoneySpent();
                }
            }
            list.add(new Pair(type.toString(), suma));
        }
        return list;
    }


    public void deleteExpenses(Context context, LinkedList<Expense> expenses) {
        u.deleteExpenses(expenses);
        ExpenseDAO dao = DAOImp.getInstance(context);
        for (Expense e : expenses) {
            dao.deleteExpense(e.getExpenseId());
        }
    }

    public String getHighestExpense(Context c, String añoEstablecido) {

        Double maxSum = Double.MIN_VALUE;  // Inicializa con el valor mínimo posible
        String maxExpenseName = null;

        for (Expense e : getAllExpenses(c)) {
            Double currentSum = e.getMoneySpent();
            String currentExpenseName = e.getExpenseName();


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            int expenseYear = Integer.parseInt(dateFormat.format(e.getTimeDate()));

            // Verifica si el gasto es del año 2023
            if (expenseYear == Integer.parseInt(añoEstablecido)) {

                if (currentSum > maxSum) {
                    maxSum = currentSum;
                    maxExpenseName = currentExpenseName;
                }
            }
        }

        return maxExpenseName;
    }

    public List<String> getMostUsedPaymentMethod(Context c,String añoEstablecido) {
        ExpenseDAO dao = DAOImp.getInstance(c);
        int maxSum = 0;
        List<String> mostUsedPaymentMethod = new ArrayList<>();

        for (PayMethod type : PayMethod.values()) {
            int sum = 0;

            for (Expense e : dao.getExpensesByPayMethod(type)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                int expenseYear = Integer.parseInt(dateFormat.format(e.getTimeDate()));
                if (expenseYear == Integer.parseInt(añoEstablecido)) {

                    sum += 1;
                }
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

    // Función auxiliar para verificar si el gasto está en un año específico

    public List<String> getMostUsedCategories(Context c,String añoEstablecido) {

        ExpenseDAO dao = DAOImp.getInstance(c);
        int maxSum = 0;
        List<String> mostUsedCategories = new ArrayList<>();

        for (ExpenseType type : ExpenseType.values()) {
            int sum = 0;

            for (Expense e : dao.getExpensesByExpenseType(type)) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                int expenseYear = Integer.parseInt(dateFormat.format(e.getTimeDate()));
                // Verifica si el gasto es del año 2023
                if (expenseYear == Integer.parseInt(añoEstablecido)) {

                    sum += 1;
                }
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


    public String getMonthWithMostExpenses(Context c,String añoEstablecido) {

        Double maxSum = Double.MIN_VALUE;  // Inicializa con el valor mínimo posible

        String yearString = null;
        for (Expense e : getAllExpenses(c)) {
            Double currentSum = e.getMoneySpent();
            // Crea un nuevo formato solo para el año
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            int expenseYear = Integer.parseInt(dateFormat.format(e.getTimeDate()));

            // Verifica si el gasto es del año 2023
            if (expenseYear == Integer.parseInt(añoEstablecido)) {
                // Obtiene el mes del gasto
                dateFormat = new SimpleDateFormat("MM");
                String expenseMonth = String.valueOf(Integer.parseInt(dateFormat.format(e.getTimeDate())));

                if (currentSum > maxSum) {
                    maxSum = currentSum;

                    yearString = expenseMonth;
                }
            }
        }

        String monthName = obtenerNombreMes(Integer.parseInt(yearString));

        return monthName;
    }

    public List<String> getUniqueYearsOfExpenses(Context c) {
        Set<String> uniqueYears = new HashSet<>(); // Usamos un conjunto para evitar repeticiones

        for (Expense e : getAllExpenses(c)) {
            // Crea un nuevo formato solo para el año
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            int expenseYear = Integer.parseInt(dateFormat.format(e.getTimeDate()));

            // Agrega el año al conjunto
            uniqueYears.add(String.valueOf(expenseYear));
        }

        // Convierte el conjunto a una lista para devolverla
        return new ArrayList<>(uniqueYears);
    }

    private String obtenerNombreMes(int month) {
        // Implementa lógica para obtener el nombre del mes según su número
        // Puedes usar un array de strings, un switch, o cualquier otra lógica que prefieras
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return nombresMeses[month - 1];
    }

    /*public List<BarEntry> getExpensesByYear() {
        List<BarEntry> expensesInTargetYear = new ArrayList<>();

        for (Expense e : u.getExpensesList()) {
            // Suponiendo que la fecha está en el formato "yyyy-MM-dd HH:mm:ss"
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            int expenseYear = Integer.parseInt(dateFormat.format(e.getTimeDate()));

            if (expenseYear == 2023) {
                expensesInTargetYear.add(e);
            }
        }

        return expensesInTargetYear;
    }*/


    public List<BarEntry> getMonthlyBarChartData(Context c,String añoEstablecido) {
        List<BarEntry> entries = new ArrayList<>();

        // Supongamos que expensesList es tu lista de gastos
        Map<Integer, Double> monthlySums = new HashMap<>();

        for(int i = 1; i<=12;i++){
            monthlySums.put(i,0.0);
        }


        for (Expense e : getAllExpenses(c)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            int expenseYear = Integer.parseInt(dateFormat.format(e.getTimeDate()));

            // Verifica si el gasto es del año 2023
            if (expenseYear == Integer.parseInt(añoEstablecido)) {

                SimpleDateFormat dateFormato = new SimpleDateFormat("MM");
                int expenseMonth = Integer.parseInt(dateFormato.format(e.getTimeDate()));

                // Suma los gastos en el mismo mes
                double currentSum = monthlySums.getOrDefault(expenseMonth, 0.0);
                currentSum += e.getMoneySpent();
                monthlySums.put(expenseMonth, currentSum);
            }



        }

        // Convierte los datos consolidados al formato necesario para el gráfico de barras
        for (Map.Entry<Integer, Double> entry : monthlySums.entrySet()) {
            entries.add(new BarEntry(entry.getKey(), entry.getValue().floatValue()));
        }

        return entries;
    }


}
