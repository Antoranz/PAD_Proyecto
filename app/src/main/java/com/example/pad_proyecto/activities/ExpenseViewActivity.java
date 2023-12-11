package com.example.pad_proyecto.activities;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_view);
        Expense e = null;
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                e = (Expense) extras.getSerializable("expense");
            }
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
