package com.example.pad_proyecto.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;

import java.util.LinkedList;
import java.util.List;

public class SpendResultListAdapter extends RecyclerView.Adapter<SpendResultListAdapter.ViewHolder> {

    protected LinkedList<Expense> expenseList;

    protected Context context;
    protected View.OnClickListener onClickListener;
    public SpendResultListAdapter(LinkedList<Expense> list, Context c){
        context = c;
        expenseList = list;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public SpendResultListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendResultListAdapter.ViewHolder holder, int position) {
        holder.bind(expenseList.get(position));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void setExpenseList(List<Expense> data) {
        expenseList = (LinkedList<Expense>) data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView expenseName;
        public TextView moneySpent;
        public ImageButton viewExpense;
        public ImageButton editExpense;
        public ImageButton deleteExpense;
        public View view;
        public Context context;
        public Expense e;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            view = itemView;
            this.context=context;
            expenseName = itemView.findViewById(R.id.expenseNameTextView);
            moneySpent = itemView.findViewById(R.id.moneySpentTextView);
            viewExpense = itemView.findViewById(R.id.viewExpense);
            viewExpense.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    NavigationManager.getInstance().navigateToExpenseView(context,e);
                }
            });
            editExpense = itemView.findViewById(R.id.editExpense);
            editExpense.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    NavigationManager.getInstance().navigateToEditExpenseView(context,e);
                }
            });;
            deleteExpense = itemView.findViewById(R.id.deleteExpense);
            deleteExpense.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Controller.getInstance().deleteExpense(context,e);
                }
            });;
        }
        public void bind(Expense b){
            e=b;
            if(b.getExpenseName().length()>35){
                expenseName.setText(b.getExpenseName().substring(0,34)+"...");
            }else{
                expenseName.setText(b.getExpenseName());
            }
        }
    }
}