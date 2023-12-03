package com.example.pad_proyecto.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.pad_proyecto.R;
import com.example.pad_proyecto.utils.Controller;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {


    private PieChart pieChart;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_view);
        pieChart = findViewById(R.id.pieChart);
        spinner = findViewById(R.id.SpinnerStatistics);

        // Configura opciones del Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.chart_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        // Configura el evento de selección del Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lógica para cambiar el tipo de gráfico según la selección
                String chartType = parentView.getItemAtPosition(position).toString();
                cambiarTipoGrafico(chartType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Puedes manejar acciones adicionales si nada está seleccionado
            }
        });

        // Configura el gráfico inicial
        configurarGraficoPorCategorias();

    }

    private void cambiarTipoGrafico(String tipoGrafico) {
        // Lógica para cambiar el tipo de gráfico según la selección del Spinner
        // Aquí puedes agregar lógica para diferentes tipos de gráficos
        switch (tipoGrafico) {
            case "Por categorías":
                configurarGraficoPorCategorias();
                break;
            case "Por tipo de pago":
                configurarGraficoPorTipodepago();
                break;
            case "Informacion General":
                configurarInformacionGeneral();
                break;
            // Puedes agregar más casos según tus necesidades
        }
    }

    private void configurarInformacionGeneral() {

        List<String> mostUsedCategories = Controller.getInstance().getMostUsedCategories(this);
        List<String> mostUsedPaymentMethods = Controller.getInstance().getMostUsedPaymentMethod(this);
        String categoriesText = "";
        String paymethodsText = "";

        // Lógica para mostrar información adicional en lugar de un gráfico
        StringBuilder builder = new StringBuilder();
        builder.append("<font color='#000000'>Categoría/s más utilizada/s: </font> <br/>");

        for (String category : mostUsedCategories) {
            builder.append("<font color='#808080'>").append(category).append("</font>, ");
        }

        builder.append("<br/><font color='#000000'>Tipo de pago más utilizado: </font> <br/>");

        for (String paymethod : mostUsedPaymentMethods) {
            builder.append("<font color='#808080'>").append(paymethod).append("</font>, ");
        }

        // Elimina la coma adicional al final
        builder.delete(builder.length() - 2, builder.length());

        categoriesText = builder.toString();

        String mensaje = "<font color='#000000'>Mayor gasto: </font> <br/>" +
                "<font color='#808080'>" + Controller.getInstance().getHighestExpense() + "</font>" +
                "<br/>" +
                "<font color='#000000'>Año con más gastos: </font> <br/>" +
                "<font color='#808080'>" + Controller.getInstance().getYearWithMostExpenses() + "</font>" +
                "<br/>"+ categoriesText + "<br/>" + paymethodsText;


        // Muestra el mensaje en el TextView adicional
        TextView textView = findViewById(R.id.InformacionAdicional);
        textView.setText(Html.fromHtml(mensaje));

        textView.setTextSize(18); // Tamaño del texto más grande
        textView.setLineSpacing(20,2); // Ajuste la separación entre las líneas según sea necesario
        //textView.setTextColor(Color.BLACK); // Texto en color negro

        // Hacer visible el TextView
        textView.setVisibility(View.VISIBLE);

        // Ocultar el gráfico (si es necesario)
        pieChart.setVisibility(View.GONE);

    }



    private void configurarGraficoPorTipodepago() {

        List<PieEntry> entries = new ArrayList<>();
        int entryCount = entries.size();
        float textSize = entryCount > 5 ? 12f : 15f; // Tamaño más grande si hay menos entradas
        for(Pair<String, Double> p : Controller.getInstance().showPayMethodStatistics(this)){
            if(p.second > 0) {
                entries.add(new PieEntry(p.second.floatValue(), p.first));
            }
        }

        // ... Lógica para obtener los datos
        PieDataSet dataSet = new PieDataSet(entries, "Tipo de pago");
        String centerText = "Tipo de pago";
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.WHITE); // Establece el color del texto de los valores
        dataSet.setValueTextSize(20f); // Establece el tamaño del texto de los valores
        // ... Configuración adicional del gráfico
        actualizarGrafico(dataSet,centerText,textSize);
    }


    private void configurarGraficoPorCategorias() {
        // Configura el gráfico con datos de categorías

        List<PieEntry> entries = new ArrayList<>();
        int entryCount = entries.size();
        float textSize = entryCount > 5 ? 15f : 13f; // Tamaño más grande si hay menos entradas
        for(Pair<String, Double> p : Controller.getInstance().showCategoryStatistics(this)){
            if(p.second > 0) {
                entries.add(new PieEntry(p.second.floatValue(), p.first));
            }
        }

        // ... Lógica para obtener los datos
        PieDataSet dataSet = new PieDataSet(entries, "Categorias");
        String centerText = "Categoría";
        int[] colors = {
                Color.rgb(141, 184, 65),  // Verde suave
                Color.rgb(255, 184, 77),  // Amarillo suave
                Color.rgb(56, 166, 165),  // Turquesa suave
                Color.rgb(251, 118, 123), // Rosa suave
                Color.rgb(181, 136, 205), // Lila suave
                Color.rgb(116, 176, 191), // Azul verdoso suave
                Color.rgb(255, 214, 135), // Amarillo pálido
                Color.rgb(192, 142, 103), // Marrón claro
                Color.rgb(156, 188, 214), // Azul suave
                Color.rgb(255, 158, 128)  // Naranja suave
        };
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE); // Establece el color del texto de los valores
        dataSet.setValueTextSize(20f); // Establece el tamaño del texto de los valores
        // ... Configuración adicional del gráfico
        actualizarGrafico(dataSet,centerText,textSize);
    }

    private void actualizarGrafico(PieDataSet dataSet,String centerText,float textSize) {
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setCenterText(centerText);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.setDrawEntryLabels(false);

        // Configuración de la leyenda (Legend)
        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // Orientación vertical

        pieChart.setExtraBottomOffset(25f); // Ajusta el espacio adicional en la parte inferior del gráfico
        pieChart.setExtraLeftOffset(20f);

        //legend.setFormSize(15f); // Tamaño de las letras en la leyenda
        // Calcular el tamaño del texto proporcional al número de entradas
        legend.setTextSize(textSize); // Establecer el tamaño del texto de la leyenda

        pieChart.invalidate();
    }

}
