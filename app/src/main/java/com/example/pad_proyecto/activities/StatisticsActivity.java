package com.example.pad_proyecto.activities;


import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.example.pad_proyecto.R;
import com.example.pad_proyecto.utils.Controller;
import com.example.pad_proyecto.utils.NotificacionSinGastos;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private PieChart pieChart;
    private Spinner spinner;
    private TextView textView;
    private BarChart barChart;
    private Spinner añoEstablecido;
    private int colorTexto;
    private TextView textInfo1, textInfo2,textInfo3,textInfo4,textInfoA1,textInfoA2,textInfoA3,textInfoA4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_view);
        pieChart = findViewById(R.id.pieChart);
        spinner = findViewById(R.id.SpinnerStatistics);
        textView = findViewById(R.id.InformacionAdicional);
        barChart = findViewById(R.id.barChart);
        añoEstablecido = findViewById(R.id.EscribirAño);
        textInfo1 = findViewById(R.id.TextInfo1);
        textInfo2 = findViewById(R.id.TextInfo2);
        textInfo3 = findViewById(R.id.TextInfo3);
        textInfo4 = findViewById(R.id.TextInfo4);
        textInfoA1 = findViewById(R.id.TextInfoA1);
        textInfoA2 = findViewById(R.id.TextInfoA2);
        textInfoA3 = findViewById(R.id.TextInfoA3);
        textInfoA4 = findViewById(R.id.TextInfoA4);

        //Eleccion de color para el texto de las leyendas de los graficos en funcion del tema
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                colorTexto = Color.WHITE;
                break;
            default:
                colorTexto = Color.BLACK;
                break;
        }
        if(!Controller.getInstance().getAllExpenses(this).isEmpty()) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.chart_types,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            List<String> uniqueYears = Controller.getInstance().getUniqueYearsOfExpenses(this);
            //Configura el arrayAdapter y el spinner usando únicamente los años de los gastos que tenemos
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, uniqueYears);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Aplica el ArrayAdapter al Spinner
            añoEstablecido.setAdapter(adapter2);

            //Establece el listener para manejar las selecciones del usuario en función de los años seleccionados
            añoEstablecido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    cambiarTipoGrafico(spinner.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    //No hace nada si no hay nada seleccionado
                }
            });
            //Establece un listener para manejar las selecciones del usuario en funcíon de las estadísticas elegidas
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    String chartType = parentView.getItemAtPosition(position).toString();
                    cambiarTipoGrafico(chartType);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    //No hace nada si no hay nada seleccionado
                }
            });
            // Configura el gráfico inicial
            configurarGraficoPorCategorias();
        }
        else{
            MostrarDialogo();
        }
    }
    public void MostrarDialogo() {
        DialogFragment newFragment = new NotificacionSinGastos();
        newFragment.show(getSupportFragmentManager(), "Singastos");
    }

    private void cambiarTipoGrafico(String tipoGrafico) {

        String[] chartTypes = getResources().getStringArray(R.array.chart_types);
        String porCategorias = chartTypes[0];
        String porTipoDePago = chartTypes[1];
        String dineroGastadoPorMes = chartTypes[2];
        String informacionGeneral = chartTypes[3];

        if (porCategorias.equals(tipoGrafico)) {
            configurarGraficoPorCategorias();
        } else if (porTipoDePago.equals(tipoGrafico)) {
            configurarGraficoPorTipodepago();
        } else if (dineroGastadoPorMes.equals(tipoGrafico)) {
            configurarGraficoPorMes();
        } else if (informacionGeneral.equals(tipoGrafico)) {
            configurarInformacionGeneral();
        } else {
            // Caso por defecto si no coincide con ningún caso
        }
    }

    private void configurarGraficoPorMes() {

        List<BarEntry> barEntries = Controller.getInstance().getMonthlyBarChartData(this,añoEstablecido.getSelectedItem().toString());
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dinero Gastado por Mes en 2023");
        BarData barData = new BarData(barDataSet);
        barDataSet.setValueTextSize(15f);
        barDataSet.setValueTextColor(colorTexto);
        barChart.setData(barData);
        // Configuración adicional del gráfico de barras
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);

        // Configura el eje X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter());  // Implementa esta clase según tus necesidades
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // Cambia la posición del eje X
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(colorTexto);
        xAxis.setLabelCount(12);
        xAxis.setAxisLineColor(colorTexto);


        // Configura el eje Y
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setTextSize(15f);
        yAxis.setTextColor(colorTexto);
        yAxis.setAxisLineColor(colorTexto);
        yAxis.setAxisMinimum(0f);

        barChart.setExtraBottomOffset(20f);
        barChart.setExtraLeftOffset(20f);
        barChart.getAxisLeft().setGridColor(Color.BLACK);
        barChart.setTouchEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setNoDataTextColor(colorTexto);
        barData.setBarWidth(0.5f);

        // Configurar la leyenda del gráfico de barras
        Legend legend = barChart.getLegend();
        legend.setTextSize(12f);
        legend.setTextColor(colorTexto);

        //Establezco la visibilidad de los gráficos en función de lo que el usuario elige

        textView.setVisibility(View.GONE);
        textInfo1.setVisibility(View.GONE);
        textInfo2.setVisibility(View.GONE);
        textInfo3.setVisibility(View.GONE);
        textInfo4.setVisibility(View.GONE);
        textInfoA1.setVisibility(View.GONE);
        textInfoA2.setVisibility(View.GONE);
        textInfoA3.setVisibility(View.GONE);
        textInfoA4.setVisibility(View.GONE);
        barChart.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.GONE);
    }

    private class MyXAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int month = (int) value;
            return obtenerNombreMes(month);
        }
        // Implementa esta función para obtener el nombre del mes según su número (1 para enero, 2 para febrero, etc.)
        private String obtenerNombreMes(int month) {
            String[] nombresMeses = {"E", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"};
            return nombresMeses[month - 1];
        }
    }
    private void configurarInformacionGeneral() {

        List<String> mostUsedCategories = Controller.getInstance().getMostUsedCategories(this,añoEstablecido.getSelectedItem().toString());
        List<String> mostUsedPaymentMethods = Controller.getInstance().getMostUsedPaymentMethod(this,añoEstablecido.getSelectedItem().toString());

        textInfoA1.setText(Controller.getInstance().getHighestExpense(this,añoEstablecido.getSelectedItem().toString()));
        textInfoA2.setText(Controller.getInstance().getMonthWithMostExpenses(this,añoEstablecido.getSelectedItem().toString()));

        String aux = "";
        for (String s : mostUsedCategories) {
            aux += s + ", ";
        }

        if (!aux.isEmpty()) {
            aux = aux.substring(0, aux.length() - 2);
        }
        textInfoA3.setText(aux);
        aux = "";
        for (String s : mostUsedPaymentMethods) {
            aux += s + ", ";
        }

        if (!aux.isEmpty()) {
            aux = aux.substring(0, aux.length() - 2);
        }

        textInfoA4.setText(aux);
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);
        textInfo1.setVisibility(View.VISIBLE);
        textInfo2.setVisibility(View.VISIBLE);
        textInfo3.setVisibility(View.VISIBLE);
        textInfo4.setVisibility(View.VISIBLE);
        textInfoA1.setVisibility(View.VISIBLE);
        textInfoA2.setVisibility(View.VISIBLE);
        textInfoA3.setVisibility(View.VISIBLE);
        textInfoA4.setVisibility(View.VISIBLE);
    }
    private void configurarGraficoPorTipodepago() {
        // Configura el gráfico de tipos de pago
        List<PieEntry> entries = new ArrayList<>();
        int entryCount = entries.size();
        float textSize = entryCount > 5 ? 12f : 15f;
        for(Pair<String, Double> p : Controller.getInstance().showPayMethodStatistics(this,añoEstablecido.getSelectedItem().toString())){
            if(p.second > 0) {
                entries.add(new PieEntry(p.second.floatValue(), p.first));
            }
        }
        PieDataSet dataSet = new PieDataSet(entries, "Tipo de pago");
        String centerText = "Tipo de pago";
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(20f);
        actualizarGrafico(dataSet,centerText,textSize);

        textView.setVisibility(View.GONE);
        textInfo1.setVisibility(View.GONE);
        textInfo2.setVisibility(View.GONE);
        textInfo3.setVisibility(View.GONE);
        textInfo4.setVisibility(View.GONE);
        textInfoA1.setVisibility(View.GONE);
        textInfoA2.setVisibility(View.GONE);
        textInfoA3.setVisibility(View.GONE);
        textInfoA4.setVisibility(View.GONE);
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);
    }


    private void configurarGraficoPorCategorias() {

        // Configura el gráfico de categorías
        List<PieEntry> entries = new ArrayList<>();
        int entryCount = entries.size();
        float textSize = entryCount > 5 ? 15f : 13f;
        for(Pair<String, Double> p : Controller.getInstance().showCategoryStatistics(this,añoEstablecido.getSelectedItem().toString())){
            if(p.second > 0) {
                entries.add(new PieEntry(p.second.floatValue(), p.first));
            }
        }
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
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(20f);
        actualizarGrafico(dataSet,centerText,textSize);

        textView.setVisibility(View.GONE);
        textInfo1.setVisibility(View.GONE);
        textInfo2.setVisibility(View.GONE);
        textInfo3.setVisibility(View.GONE);
        textInfo4.setVisibility(View.GONE);
        textInfoA1.setVisibility(View.GONE);
        textInfoA2.setVisibility(View.GONE);
        textInfoA3.setVisibility(View.GONE);
        textInfoA4.setVisibility(View.GONE);
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);
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
        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        pieChart.setExtraBottomOffset(25f);
        pieChart.setExtraLeftOffset(20f);
        legend.setTextSize(textSize);
        legend.setTextColor(colorTexto);
        pieChart.invalidate();
    }
}