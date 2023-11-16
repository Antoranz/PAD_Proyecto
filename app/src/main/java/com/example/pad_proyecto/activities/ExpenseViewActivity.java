package com.example.pad_proyecto.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.NavigationManager;

import java.io.File;

public class ExpenseViewActivity extends AppCompatActivity {

    private TextView gasto;
    private TextView fecha;
    private TextView dineroGastado;
    private TextView tipoPago;
    private TextView categoria;
    private TextView nota;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_view);
        Expense e = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            e = getIntent().getSerializableExtra("expense",Expense.class);
        }

        gasto = findViewById(R.id.expenseName);
        fecha = findViewById(R.id.timeDate);
        dineroGastado =findViewById(R.id.spentMoney);
        tipoPago =findViewById(R.id.paymentMethod);
        categoria =findViewById(R.id.expenseType);
        nota =findViewById(R.id.noteExpense);
        img = findViewById(R.id.galleryImageView);


        gasto.setText(e.getExpenseName());
        fecha.setText(e.writeDate());
        dineroGastado.setText(e.getMoneySpent().toString());
        tipoPago.setText(e.getPayMethod().toString());
        categoria.setText(e.getCategory().toString());
        nota.setText(e.getNote());

        String imagen = e.getImagePath();

        if(imagen != null){

            String imagePath = this.getFilesDir() + "/" + imagen;
            File imgFile = new File(imagePath);

            if(imgFile.exists()){
                Uri imgUri = Uri.fromFile(imgFile);

                Glide.with(this)
                        .load(imgUri)
                        .into(img);




            }

        }

        img.setOnClickListener(v -> {
            if(imagen!=null){
                NavigationManager.getInstance().navigateToFullScreenView(this,imagen);
            }

        });




    }

}
