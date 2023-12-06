package com.example.pad_proyecto.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pad_proyecto.R;

import com.example.pad_proyecto.activities.ExpenseViewActivity;
import com.example.pad_proyecto.data.Expense;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DeleteExpenseAdapter extends RecyclerView.Adapter<DeleteExpenseAdapter.ViewHolder> {

    private LinkedList<Expense> list;
    private Context context;
    private final LinkedList<Expense> selectedExpenses = new LinkedList<>();



    public DeleteExpenseAdapter(LinkedList<Expense> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public LinkedList<Expense> getSelectedExpenses() {
        return new LinkedList<>(selectedExpenses);
    }

    public void selectAllExpenses(LinkedList<Expense> lista){
        selectedExpenses.clear();
        selectedExpenses.addAll(lista);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_delete_expense_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView expenseNameTextView;
        private final TextView moneySpentTextView;
        private final CheckBox checkBox;
        private final ImageButton viewExpenseButton;

        public ViewHolder(View view) {
            super(view);
            expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
            moneySpentTextView = itemView.findViewById(R.id.moneySpentTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
            viewExpenseButton = itemView.findViewById(R.id.viewExpense);
        }

        public void bind(Expense expense) {
            expenseNameTextView.setText(expense.getExpenseName());
            moneySpentTextView.setText(String.valueOf(expense.getMoneySpent()));

            checkBox.setChecked(selectedExpenses.contains(expense));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedExpenses.add(expense);
                } else {
                    selectedExpenses.remove(expense);
                }
            });

            viewExpenseButton.setOnClickListener(v -> {
                NavigationManager.getInstance().navigateToExpenseView(context,expense);

            });
        }
    }
}
