package com.pack1.RegistersLogin;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.pack1.quanlybangiaythethao.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigInput {

    public static void blockSpaces(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    editText.setText(s.toString().replace(" ", ""));
                    editText.setSelection(editText.getText().length()); // Đặt con trỏ vào cuối chuỗi
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    public static void blockNumbers(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                // Loại bỏ các ký tự số
                if (!input.matches("[a-zA-Z]*")) {
                    editText.setText(input.replaceAll("[0-9]", ""));  // Loại bỏ số
                    editText.setSelection(editText.getText().length()); // Đưa con trỏ về cuối
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    public static void limitNumbers(EditText editText, AtomicBoolean flag,Context context) {
        InputFilter[] filters = new InputFilter[]{
                new InputFilter.LengthFilter(10)  // Giới hạn 10 ký tự
        };
        editText.setFilters(filters);
        editText.addTextChangedListener(new TextWatcher() {
            String input;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 input = s.toString();
                if (input.length() == 1 && !input.startsWith("0")) {
                    editText.setText("0" + input); // Thêm số 0 vào đầu
                    editText.setSelection(2); // Đặt con trỏ vào sau số 0
                }
                if(input.length()<10){
                    Toast.makeText(context.getApplicationContext(),"số điện thoại chưa đủ 10 số",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10){
                    flag.set(true);
                }
            }
        });
    }

    public static void isValidPassword(EditText editText, AtomicBoolean flag, TextView tv1, TextView tv2, TextView tv3, Context context) {

        editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String password = charSequence.toString();
                    if (password.isEmpty()) {
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                        tv3.setVisibility(View.GONE);
                    }
                    if (!password.isEmpty()) {
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                    }
                    // Check for uppercase letter
                    if (password.matches(".*[A-Z].*")) {
                        tv1.setTextColor(ContextCompat.getColor(context.getApplicationContext(),android.R.color.holo_green_dark));
                    } else {
                        tv1.setTextColor(ContextCompat.getColor(context.getApplicationContext(),android.R.color.holo_red_dark));
                    }

                    // Check for number
                    if (password.matches(".*\\d.*")) {
                        tv2.setTextColor(ContextCompat.getColor(context.getApplicationContext(),android.R.color.holo_green_dark));
                    } else {
                        tv2.setTextColor(ContextCompat.getColor(context.getApplicationContext(),android.R.color.holo_red_dark));
                    }

                    // Check for special character
                    if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                        tv3.setTextColor(ContextCompat.getColor(context.getApplicationContext(),android.R.color.holo_green_dark));
                    } else {
                        tv3.setTextColor(ContextCompat.getColor(context.getApplicationContext(),android.R.color.holo_red_dark));
                    }
                    int holo_green_dark = ContextCompat.getColor(context.getApplicationContext(), android.R.color.holo_green_dark);
                    if (tv1.getCurrentTextColor() == holo_green_dark &&
                            tv2.getCurrentTextColor() == holo_green_dark &&
                            tv3.getCurrentTextColor() == holo_green_dark){
                        flag.set(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
    }



}