package com.example.pad_proyecto.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pad_proyecto.R;
import com.example.pad_proyecto.data.Expense;
import com.example.pad_proyecto.data.User;
import com.example.pad_proyecto.enums.ExpenseType;
import com.example.pad_proyecto.enums.PayMethod;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.NavigationManager;

import java.util.Calendar;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Controller.getInstance().initInfo(this);
        User u = Controller.getInstance().getUser();
        TextView b = findViewById(R.id.idBienvenida);
        createNotificationChannel();
        if(u!=null) b.setText(b.getText()+" "+u.getUserName());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(u!=null){
                    changeActivity();
                }else{
                    changeUserActivity();
                }
            }
        }, 3000);

    }
    public void changeActivity(){
        NavigationManager.getInstance().navigateToMenuView(this);
    }
    public void changeUserActivity(){NavigationManager.getInstance().navigateToAddUserView(this);}
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "mi_notificacion_id",
                    "Presupuesto",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}


