package br.com.xisvaldo.android.dialog.masks;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Created by leonardo.borges on 03/02/2017.
 */
public class CurrencyMask implements TextWatcher {

    private final EditText editText;

    private boolean isUpdating = false;

    public CurrencyMask(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isUpdating) {
            isUpdating = false;
            return;
        }

        isUpdating = true;
        String str = s.toString();
        boolean hasMask = (str.contains("R$") || str.contains("$"))
                && (str.contains(".") || str.contains(","));

        if (hasMask) {
            str = str.replaceAll("[$,.]", "").replaceAll("[a-zA-Z]","");
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        str = nf.format(Double.parseDouble(str)/100);

        editText.setText(str);
        editText.setSelection(editText.getText().length());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
