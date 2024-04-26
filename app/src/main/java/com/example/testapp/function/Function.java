package com.example.testapp.function;

import android.text.TextUtils;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Function {
    public static String formatToVND(Integer amount){
        NumberFormat vnCurrencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return vnCurrencyFormat.format(amount);
    }

    public static String formatDateTimeToDate(String dateTime){
        String result = "";
        char[] chars = dateTime.toCharArray();
        for (int i = 0; i < 10; i++) {
                result += chars[i];
        }
        return  result;
    }

    public static boolean setRequired(List<EditText> listEt, String message) {
        boolean check = true;
        for (int i = 0; i < listEt.size(); i++) {
            if (TextUtils.isEmpty(listEt.get(i).getText())) {
                listEt.get(i).setError(message);
                check = false;
            }
        }
        return check;
    }

    public static boolean isValidPhoneNumber(EditText phoneNumber) {
        if (!phoneNumber.getText().toString().startsWith("+84")) {
            phoneNumber.setError("Số điện thoại bắt đầu bằng +84");
            return false;
        } else if (phoneNumber.getText().toString().length() != 12) {
            phoneNumber.setError("Độ dài của số điện thoại là 12 kí tự");
            return false;
        }
        return  true;
    }

    public static boolean isValidPassword(EditText password) {
        if (password.getText().toString().length() >= 6) {
            return false;
        }
        return true;
    }
}
