package com.example.pad_proyecto.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;


import androidx.appcompat.app.AppCompatActivity;

import com.example.pad_proyecto.R;
import com.example.pad_proyecto.utils.Controller;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_view);
        PieChart pieChart = findViewById(R.id.pieChart);

        List<PieEntry> entries = new ArrayList<>();
        for(Pair<String, Double> p : Controller.getInstance().showCategoryStatistics(this)){
            if(p.second > 0) {
                entries.add(new PieEntry(p.second.floatValue(), p.first));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Categorias");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Usa colores predefinidos
        dataSet.setValueTextColor(Color.WHITE); // Establece el color del texto de los valores
        dataSet.setValueTextSize(20f); // Establece el tamaño del texto de los valores

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setCenterText("Dinero Gastado por categoría"); // Establece el texto del centro del gráfico
        pieChart.setHoleRadius(25f); // Establece el radio del agujero central
        pieChart.setTransparentCircleRadius(30f); // Establece el radio del círculo transparente alrededor del agujero
        pieChart.getDescription().setEnabled(false); // Deshabilita la descripción del gráfico
        pieChart.animateY(1000); // Añade una animación al gráfico
        pieChart.setDrawEntryLabels(false);
        pieChart.invalidate();



    }

}
