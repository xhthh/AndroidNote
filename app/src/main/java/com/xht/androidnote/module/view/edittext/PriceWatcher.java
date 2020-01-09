package com.xht.androidnote.module.view.edittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by xht on 2020/1/9.
 */
public class PriceWatcher implements TextWatcher {
    private EditText pedt;

    public PriceWatcher(EditText edt) {
        this.pedt = edt;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String editable = pedt.getText().toString();
        String str = stringFilter(editable);
        if (!editable.equals(str)) {
            pedt.setText(str);
            pedt.setSelection(str.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        //^(([1-9]\\d{0,5}\\.{1}\\d{0,1})|([1-9]\\d{0,5})|([0]{1}\\.{1}[1-9]{0,1})|([0]))$


        String regEx = "^(([1-9]\\\\d{0,5}\\\\.{1}\\\\d{0,1})|([1-9]\\\\d{0,5})|([0]{1}\\\\" +
                ".{1}[1-9]{0,1})|([0]))$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
