package com.pack1.RegistersLogin;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class BlockSpaceChar {

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
}