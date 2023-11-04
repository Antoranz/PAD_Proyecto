package com.example.pad_proyecto.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.SpendResultListAdapter;

import java.util.LinkedList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final int BOOK_LOADER_ID = 1;
    private SpendResultListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_expenses);

        RecyclerView rv = findViewById(R.id.recyclerView);

        SearchView sv = findViewById(R.id.searchView);

        sv.setOnQueryTextListener(this);

        adapter = new SpendResultListAdapter(new LinkedList<>(),this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);


    }

    public LinkedList<Expense> searchExpenses (String query){
        return Controller.getInstance().searchExpense(query, this);
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

