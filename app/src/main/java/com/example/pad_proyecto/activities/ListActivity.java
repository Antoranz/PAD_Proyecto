package com.example.pad_proyecto.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.SpendResultListAdapter;
import java.util.ArrayList;
import java.util.LinkedList;

public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SpendResultListAdapter adapter;
    private Spinner s1Categoria;
    private Spinner s2MetodoPago;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_expenses);

        RecyclerView rv = findViewById(R.id.recyclerView);

        s1Categoria = findViewById(R.id.spinner1);
        s2MetodoPago = findViewById(R.id.spinner2);

        ArrayList<String> listaValoresCategoria = new ArrayList<>();
        listaValoresCategoria.add("Todos");
        for(ExpenseType e : ExpenseType.values())
            listaValoresCategoria.add(e.toString());

        ArrayList<String> listaValoresMetodoPago = new ArrayList<>();
        listaValoresMetodoPago.add("Todos");
        for(PayMethod p : PayMethod.values())
            listaValoresMetodoPago.add(p.toString());

        s1Categoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaValoresCategoria));
        s2MetodoPago.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaValoresMetodoPago));
        SearchView sv = findViewById(R.id.searchView);
        sv.setOnQueryTextListener(this);
        adapter = new SpendResultListAdapter(new LinkedList<>(),this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        adapter.setExpenseList(Controller.getInstance().getAllExpenses(this));
        adapter.notifyDataSetChanged();
    }
    public LinkedList<Expense> searchExpenses (String query){
        return Controller.getInstance().searchExpense(query, this, s1Categoria.getSelectedItem().toString(), s2MetodoPago.getSelectedItem().toString());
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.setExpenseList(searchExpenses(query));
        adapter.notifyDataSetChanged();
        return true;
    }
    @Override
    public boolean onQueryTextChange(String query) {
        adapter.setExpenseList(searchExpenses(query));
        adapter.notifyDataSetChanged();
        return true;
    }
}

