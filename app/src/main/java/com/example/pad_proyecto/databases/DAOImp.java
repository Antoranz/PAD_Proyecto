package com.example.pad_proyecto.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DAOImp extends SQLiteOpenHelper implements ExpenseDAO, UserDAO{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "spendwise.db";

    private static final String TABLE_EXPENSE = "Expense";
    private static final String COLUMN_ID = "expenseId";
    private static final String COLUMN_NAME = "expenseName";
    private static final String COLUMN_MONEY_SPENT = "moneySpent";
    private static final String COLUMN_TIME_DATE = "timeDate";
    private static final String COLUMN_IMAGE_PATH = "imagePath";
    private static final String COLUMN_SPENT_TYPE = "spentType";
    private static final String COLUMN_PAY_METHOD = "payMethod";
    private static final String COLUMN_NOTE = "note";
    private static final String TABLE_USER = "User";
    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_USER_NAME = "userName";
    private static final String COLUMN_BUDGET = "budget";
    private static final String COLUMN_USER_TOTALMONEY = "totalMonaySpent";

    private static DAOImp instance;
    public static synchronized DAOImp getInstance(Context context) {
        if (instance == null) {
            instance = new DAOImp(context);
        }
        instance = new DAOImp(context);
        return instance;
    }
    public DAOImp(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (!checkDatabaseExists(context)) {
            getWritableDatabase();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if (!isTableExists(sqLiteDatabase,TABLE_USER)) {
            String CREATE_TABLE_USER =
                    "CREATE TABLE " + TABLE_USER + " (" +
                            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                            COLUMN_USER_NAME + " TEXT NOT NULL," +
                            COLUMN_BUDGET + " REAL," +
                            COLUMN_USER_TOTALMONEY + " REAL DEFAULT 0" + ");";
            sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        }
        if(!isTableExists(sqLiteDatabase,TABLE_EXPENSE)){
            String CREATE_TABLE_EXPENSE =
                    "CREATE TABLE " + TABLE_EXPENSE + " (" +
                            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                            COLUMN_USER_ID + " INTEGER," +  // Clave foránea
                            COLUMN_NAME + " TEXT NOT NULL," +
                            COLUMN_MONEY_SPENT + " REAL NOT NULL," +
                            COLUMN_TIME_DATE + " DATETIME NOT NULL," +
                            COLUMN_IMAGE_PATH + " TEXT," +
                            COLUMN_SPENT_TYPE + " TEXT NOT NULL," +
                            COLUMN_PAY_METHOD + " TEXT NOT NULL," +
                            COLUMN_NOTE + " TEXT," +
                            "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")" +
                            ");";

            sqLiteDatabase.execSQL(CREATE_TABLE_EXPENSE);
            String insertTrigger =
                    "CREATE TRIGGER insert_amount " +
                            "AFTER INSERT ON " + TABLE_EXPENSE + " " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "    UPDATE " + TABLE_USER + " " +
                            "    SET "+COLUMN_USER_TOTALMONEY+" = "+ COLUMN_USER_TOTALMONEY + " + NEW." + COLUMN_MONEY_SPENT + ", " +
                            "        "+COLUMN_BUDGET+" = CASE WHEN "+COLUMN_BUDGET+" IS NOT NULL THEN "+COLUMN_BUDGET+" - NEW." + COLUMN_MONEY_SPENT +" ELSE "+COLUMN_BUDGET+" END "+
                            "    WHERE "+COLUMN_USER_ID+" = NEW." + COLUMN_USER_ID + "; "+
                            "END;";

            sqLiteDatabase.execSQL(insertTrigger);

            String updateTrigger =
                    "CREATE TRIGGER update_amount " +
                            "AFTER UPDATE ON " + TABLE_EXPENSE + " " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "    UPDATE " + TABLE_USER + " " +
                            "    SET "+COLUMN_USER_TOTALMONEY+" = "+ COLUMN_USER_TOTALMONEY + " - OLD." + COLUMN_MONEY_SPENT + " + NEW." + COLUMN_MONEY_SPENT + ", " +
                            "        "+COLUMN_BUDGET+" = CASE WHEN "+COLUMN_BUDGET+" IS NOT NULL THEN "+COLUMN_BUDGET+" + OLD." + COLUMN_MONEY_SPENT +" - NEW." + COLUMN_MONEY_SPENT +" ELSE "+COLUMN_BUDGET+" END "+
                            "    WHERE "+COLUMN_USER_ID+" = NEW." + COLUMN_USER_ID + "; "+
                            "END;";

            sqLiteDatabase.execSQL(updateTrigger);


            String deleteTrigger =
                    "CREATE TRIGGER delete_amount " +
                            "AFTER DELETE ON " + TABLE_EXPENSE + " " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "    UPDATE " + TABLE_USER + " " +
                            "    SET "+COLUMN_USER_TOTALMONEY+" = "+ COLUMN_USER_TOTALMONEY + " - OLD." + COLUMN_MONEY_SPENT + ", " +
                            "        "+COLUMN_BUDGET+" = CASE WHEN "+COLUMN_BUDGET+" IS NOT NULL THEN "+COLUMN_BUDGET+" + OLD." + COLUMN_MONEY_SPENT +" ELSE "+COLUMN_BUDGET+" END "+
                            "    WHERE "+COLUMN_USER_ID+" = OLD." + COLUMN_USER_ID + "; "+
                            "END;";

            sqLiteDatabase.execSQL(deleteTrigger);


        }
    }
    private boolean checkDatabaseExists(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }
    private boolean isTableExists(@NonNull SQLiteDatabase db,String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName});
        try {
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
        }
    }
    @Override
    public void onUpgrade(@NonNull SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_EXPENSE);
        onCreate(sqLiteDatabase);
    }
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_BUDGET, user.getBudget());

        long id = db.insert(TABLE_USER, null, values);
        user.setId(id);

        db.close();
    }
    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_USER;

        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
            Double budget = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BUDGET));
            Double tmoney = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_USER_TOTALMONEY));
            User u = new User(userName);
            u.setBudget(budget);
            u.setId(userId);
            u.setTotalMoneySpent(tmoney);
            userList.add(u);
        }

        cursor.close();
        db.close();

        return userList;
    }
    public void updateUserBudget(long id, double budget){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BUDGET, budget);

        db.update(TABLE_USER, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }


    public void addExpense(@NonNull Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, expense.getUserId());
        values.put(COLUMN_NAME, expense.getExpenseName());
        values.put(COLUMN_MONEY_SPENT, expense.getMoneySpent());
        values.put(COLUMN_TIME_DATE, expense.getTimeDate().getTime());
        values.put(COLUMN_IMAGE_PATH, expense.getImagePath());
        values.put(COLUMN_SPENT_TYPE, expense.getCategory().toString());
        values.put(COLUMN_PAY_METHOD, expense.getPayMethod().toString());
        values.put(COLUMN_NOTE, expense.getNote());

        long id = db.insert(TABLE_EXPENSE, null, values);
        expense.setId(id);

        db.close();
    }
    public void updateExpense(@NonNull Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, expense.getUserId());
        values.put(COLUMN_NAME, expense.getExpenseName());
        values.put(COLUMN_MONEY_SPENT, expense.getMoneySpent());
        values.put(COLUMN_TIME_DATE, expense.getTimeDate().getTime());
        values.put(COLUMN_IMAGE_PATH, expense.getImagePath());
        values.put(COLUMN_SPENT_TYPE, expense.getCategory().toString());
        values.put(COLUMN_PAY_METHOD, expense.getPayMethod().toString());
        values.put(COLUMN_NOTE, expense.getNote());

        db.update(TABLE_EXPENSE, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(expense.getExpenseId())});

        db.close();
    }
    public void deleteExpense(long expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EXPENSE, COLUMN_ID + " = ?",
                new String[]{String.valueOf(expenseId)});

        db.close();
    }
    public LinkedList<Expense> getAllExpenses(long uId) {
        LinkedList<Expense> expenseList = new LinkedList<Expense>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE +" WHERE (" + COLUMN_USER_ID+" = "+uId+");";

        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String expenseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            double moneySpent = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MONEY_SPENT));

            long timeDateInMillis = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIME_DATE));
            // Crea un objeto Instant utilizando el valor long
            Instant instant = null;
            Date fecha = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                instant = Instant.ofEpochMilli(timeDateInMillis);
                // Convierte el Instant a un objeto Date
               fecha = Date.from(instant);

            }

            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH));
            String expenseType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPENT_TYPE));
            String payMethod = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAY_METHOD));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE));

            Expense expense = new Expense(userId,expenseName, moneySpent, fecha, imagePath, ExpenseType.valueOf(expenseType), PayMethod.valueOf(payMethod), note);
            expense.setId(id);

            expenseList.add(expense);
        }
        cursor.close();
        db.close();

        return expenseList;
    }
    public void clearExpenseTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EXPENSE, null, null);

        db.close();
    }
    public List<Expense> getExpensesByExpenseType(@NonNull ExpenseType expenseType) {
        String et = expenseType.toString();

        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE +
                " WHERE " + COLUMN_SPENT_TYPE + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{et});

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String expenseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            double moneySpent = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MONEY_SPENT));

            long timeDateInMillis = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIME_DATE));
            // Crea un objeto Instant utilizando el valor long
            Instant instant = null;
            Date fecha = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                instant = Instant.ofEpochMilli(timeDateInMillis);
                // Convierte el Instant a un objeto Date
                fecha = Date.from(instant);

            }

            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH));
            String eType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPENT_TYPE));
            String payMethod = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAY_METHOD));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE));

            Expense expense = new Expense(userId, expenseName, moneySpent, fecha, imagePath, ExpenseType.valueOf(eType), PayMethod.valueOf(payMethod), note);
            expense.setId(id);

            expenseList.add(expense);
        }

        cursor.close();
        db.close();

        return expenseList;
    }

    public List<Expense> getExpensesByPayMethod(@NonNull PayMethod payMethod) {
        String pm = payMethod.toString();

        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE +
                " WHERE " + COLUMN_PAY_METHOD + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{pm});

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String expenseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            double moneySpent = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MONEY_SPENT));

            long timeDateInMillis = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIME_DATE));
            // Crea un objeto Instant utilizando el valor long
            Instant instant = null;
            Date fecha = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                instant = Instant.ofEpochMilli(timeDateInMillis);
                // Convierte el Instant a un objeto Date
                fecha = Date.from(instant);

            }
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH));
            String expenseType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPENT_TYPE));
            String pMethod = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAY_METHOD));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE));

            Expense expense = new Expense(userId, expenseName, moneySpent, fecha, imagePath, ExpenseType.valueOf(expenseType), PayMethod.valueOf(pMethod), note);
            expense.setId(id);

            expenseList.add(expense);
        }

        cursor.close();
        db.close();

        return expenseList;
    }

}
