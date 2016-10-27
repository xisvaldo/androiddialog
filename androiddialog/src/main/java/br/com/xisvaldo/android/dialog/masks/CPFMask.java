package br.com.xisvaldo.android.dialog.masks;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by leonardo.borges on 29/08/2016.
 */
public abstract class CPFMask {

    private static final String cpfMask = "###.###.###-##";

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    public static TextWatcher insert(final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = CPFMask.unmask(s.toString());
                String mask = "";

                if (isUpdating) {
                    old = input;
                    isUpdating = false;
                    return;
                }

                int i = 0;
                for (char m : cpfMask.toCharArray()) {
                    if (m != '#' && input.length() > old.length() ||
                            (m != '#' && input.length() < old.length() && input.length() != i)) {
                        mask += m;
                        continue;
                    }
                    try {
                        mask += input.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mask);
                ediTxt.setSelection(mask.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    public static boolean validateCPF(String cpf) {

        if ((cpf == null) || (cpf.length() != 11)) return false;

        int[] weight = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        Integer firstDigit = calculateDigit(cpf.substring(0, 9), weight);
        Integer secondDigit = calculateDigit(cpf.substring(0, 9) + firstDigit, weight);
        return cpf.equals(cpf.substring(0, 9) + firstDigit.toString() + secondDigit.toString());
    }

    private static int calculateDigit(String str, int[] weight) {
        int sum = 0;
        int digit;

        for (int index = str.length() - 1; index >= 0; index--) {
            digit = Integer.parseInt(str.substring(index, index + 1));
            sum += digit * weight[weight.length - str.length() + index];
        }

        sum = 11 - sum % 11;
        return sum > 9 ? 0 : sum;
    }
}