package com.example.pad_proyecto.activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pad_proyecto.R;
import com.example.pad_proyecto.utils.Controller;

import com.example.pad_proyecto.data.Expense;

import com.example.pad_proyecto.utils.DeleteExpenseAdapter;
import com.example.pad_proyecto.utils.NavigationManager;

import java.util.LinkedList;

public class DeleteExpenseActivity extends AppCompatActivity {
    private DeleteExpenseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_view);
        initIGUI();
    }
    private void initIGUI() {
        RecyclerView rv = findViewById(R.id.recyclerViewDelete);

        LinkedList<Expense> expensesList = Controller.getInstance().getAllExpenses(this);
        // Inicializa el RecyclerView y su adaptador
        adapter = new DeleteExpenseAdapter(expensesList, this);

        // Configura el RecyclerView con un LinearLayoutManager y el adaptador
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        Button button = findViewById(R.id.deleteButton);
        button.setOnClickListener(v -> {
            LinkedList<Expense> selectedExpenses = adapter.getSelectedExpenses();

            Controller.getInstance().deleteExpenses(DeleteExpenseActivity.this, selectedExpenses);
            adapter.notifyDataSetChanged();
            NavigationManager.getInstance().navigateToDeleteExpense(v.getContext());
        });
        Button selectAllButton = findViewById(R.id.SelectAllbutton);
        selectAllButton.setOnClickListener(v -> {

            adapter.selectAllExpenses(expensesList);
            adapter.notifyDataSetChanged();
        });
    }
}
