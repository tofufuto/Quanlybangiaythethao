package com.pack1.admin;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pack1.quanlybangiaythethao.R;

import java.util.ArrayList;

public class Stats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BarChart barChart = findViewById(R.id.barChart);

        // Tạo danh sách các BarEntry cho dữ liệu
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 10)); // Tháng 1: 10 triệu
        barEntries.add(new BarEntry(2, 20)); // Tháng 2: 20 triệu
        barEntries.add(new BarEntry(3, 30)); // Tháng 3: 30 triệu
        barEntries.add(new BarEntry(4, 40)); // Tháng 4: 40 triệu
        barEntries.add(new BarEntry(5, 50)); // Tháng 5: 50 triệu
        barEntries.add(new BarEntry(6, 60)); // Tháng 6: 60 triệu
        barEntries.add(new BarEntry(7, 70)); // Tháng 7: 70 triệu
        barEntries.add(new BarEntry(8, 80)); // Tháng 8: 80 triệu
        barEntries.add(new BarEntry(9, 90)); // Tháng 9: 90 triệu
        barEntries.add(new BarEntry(10, 100)); // Tháng 10: 100 triệu
        barEntries.add(new BarEntry(11, 110)); // Tháng 11: 110 triệu
        barEntries.add(new BarEntry(12, 120)); // Tháng 12: 120 triệu

        // Tạo BarDataSet từ dữ liệu
        BarDataSet barDataSet = new BarDataSet(barEntries, "Doanh Thu (Triệu VND)");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Đặt màu sắc tự động
        barDataSet.setValueTextColor(Color.BLACK); // Màu chữ số trên thanh
        barDataSet.setValueTextSize(12f); // Kích thước chữ số

        // Tạo BarData từ BarDataSet
        BarData barData = new BarData(barDataSet);

        // Gán BarData vào BarChart
        barChart.setData(barData);

        // Thiết lập các giá trị trên trục X (Tháng)
        String[] months = {"", "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
        barChart.getXAxis().setGranularity(1f); // Khoảng cách giữa các giá trị
        barChart.getXAxis().setGranularityEnabled(true);

        // Thiết lập hiệu ứng
        barChart.animateY(1000); // Hiệu ứng tăng dần theo trục Y

        // Thiết lập chú thích (Legend)
        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.BLACK); // Màu chữ legend
        legend.setTextSize(14f); // Kích thước chữ legend
        legend.setForm(Legend.LegendForm.CIRCLE); // Hình dạng legend

        // Thiết lập mô tả (Description)
        Description description = new Description();
        description.setText("Doanh thu theo tháng"); // Nội dung mô tả
        description.setTextColor(Color.RED); // Màu chữ mô tả
        description.setTextSize(12f); // Kích thước chữ mô tả
        barChart.setDescription(description);

        // Làm mới BarChart
        barChart.invalidate();
    }
}