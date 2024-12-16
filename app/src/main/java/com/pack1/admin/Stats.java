package com.pack1.admin;

import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import com.pack1.dao.UserOrderDao;
import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Stats extends AppCompatActivity {

    BarChart barChart;
    int UselectedYear;
    TextView yearS,rev;
    float m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12;
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
        barChart = findViewById(R.id.barChart);
        yearS = findViewById(R.id.year);
        rev = findViewById(R.id.rev);

        int UselectedYear = LocalDate.now().getYear();
        Log.d("YEAR",""+UselectedYear);
        yearS.setText(""+UselectedYear);

        loadStats(UselectedYear);

        rev.setText("Tổng doanh thu : "+ String.format("%,d", ((int)(m1+m2+m3+m4+m5+m6+m7+m8+m9+m11+m12)) ) + "VNĐ");
        // Tạo danh sách các BarEntry cho dữ liệu
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, m1)); //Tháng và giá trị
        barEntries.add(new BarEntry(2, m2));
        barEntries.add(new BarEntry(3, m3));
        barEntries.add(new BarEntry(4, m4));
        barEntries.add(new BarEntry(5, m5));
        barEntries.add(new BarEntry(6, m6));
        barEntries.add(new BarEntry(7, m7));
        barEntries.add(new BarEntry(8, m8));
        barEntries.add(new BarEntry(9, m9));
        barEntries.add(new BarEntry(10, m10));
        barEntries.add(new BarEntry(11, m11));
        barEntries.add(new BarEntry(12, m12));

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


        yearS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearPickerDialog();
                yearS.setText(""+UselectedYear);
                rev.setText("Tổng doanh thu : "+ String.format("%,d", ((int)(m1+m2+m3+m4+m5+m6+m7+m8+m9+m11+m12)) ) + "VNĐ");
            }
        });
    }
    private float revenueCal(ArrayList<UserOrder> userOrderArrayList)
    {
        float revenue = 0;
        for(int i = 0;i < userOrderArrayList.size();i++)
        {
            revenue += userOrderArrayList.get(i).getTotalPrice();
        }
        return revenue;
    }
    private void showYearPickerDialog() {
        // Lấy năm hiện tại
        final Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // Tạo NumberPicker
        final NumberPicker yearPicker = new NumberPicker(this);
        yearPicker.setMinValue(1900); // Năm bắt đầu
        yearPicker.setMaxValue(currentYear); // Năm kết thúc (hiện tại)
        yearPicker.setValue(currentYear); // Năm mặc định (hiện tại)

        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Year");
        builder.setView(yearPicker); // Thêm NumberPicker vào AlertDialog

        // Nút "OK"
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedYear = yearPicker.getValue();
                UselectedYear = selectedYear;
                loadStats(UselectedYear);
                barChart.invalidate();
                // Xử lý sau khi chọn năm
            }
        });

        // Nút "Cancel"
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Đóng hộp thoại
            }
        });

        // Hiển thị AlertDialog
        builder.create().show();
    }
    private void loadStats(int selectedYear)
    {
        UserOrderDao userOrderDao = new UserOrderDao(this);
        m1 = revenueCal(userOrderDao.getOrdersByMonthAndYear(1,selectedYear));
        m2 = revenueCal(userOrderDao.getOrdersByMonthAndYear(2,selectedYear));
        m3 = revenueCal(userOrderDao.getOrdersByMonthAndYear(3,selectedYear));
        m4 = revenueCal(userOrderDao.getOrdersByMonthAndYear(4,selectedYear));
        m5 = revenueCal(userOrderDao.getOrdersByMonthAndYear(5,selectedYear));
        m6 = revenueCal(userOrderDao.getOrdersByMonthAndYear(6,selectedYear));
        m7 = revenueCal(userOrderDao.getOrdersByMonthAndYear(7,selectedYear));
        m8 = revenueCal(userOrderDao.getOrdersByMonthAndYear(8,selectedYear));
        m9 = revenueCal(userOrderDao.getOrdersByMonthAndYear(9,selectedYear));
        m10 = revenueCal(userOrderDao.getOrdersByMonthAndYear(10,selectedYear));
        m11 = revenueCal(userOrderDao.getOrdersByMonthAndYear(11,selectedYear));
        m12 = revenueCal(userOrderDao.getOrdersByMonthAndYear(12,selectedYear));
        Log.d("m12",""+selectedYear);

    }
}