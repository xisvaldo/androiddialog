package br.com.xisvaldo.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import br.com.xisvaldo.android.dialog.masks.CPFMask;

/**
 * Created by leonardo.borges on 27/09/2016.
 */
public class AndroidInputDialog {

    private static AlertDialog.Builder dialogBuilder;
    private static Handler responseHandler;
    private static AlertDialog dialog;
    private final static int MAX_TITLE_LENGTH = 20;
    private final static int MAX_MESSAGE_LENGTH = 140;
    private static boolean isShowingPassword = false;

    public enum InputType {
        TEXT,
        NUMBER,
        PASSWORD
    }

    public static void show(Activity activity, InputType type, String title,
                            String message, String placeholder, Handler handler) throws IOException {

        show(activity, type, title, message, placeholder, handler, false);
    }


    public static void show(Activity activity, InputType type, String title,
                            String message, String placeholder, Handler handler, final boolean useCPFMask) throws IOException {

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IOException("Max allowed title length is " + MAX_TITLE_LENGTH + ".");
        }
        else if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new IOException("Max allowed message length is " + MAX_MESSAGE_LENGTH + ".");
        }

        responseHandler = handler;
        dialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.android_inputdialog, null);

        TextView titleTV = (TextView) dialogView.findViewById(R.id.title);
        titleTV.setText(title);

        TextView messageTV = (TextView) dialogView.findViewById(R.id.dialog_info);
        messageTV.setText(message);

        final EditText input = (EditText) dialogView.findViewById(R.id.input);
        input.getBackground().mutate().setColorFilter(activity.getResources().getColor(R.color.lightBlue), PorterDuff.Mode.SRC_ATOP);

        if (useCPFMask) {
            input.addTextChangedListener(CPFMask.insert(input));
        }

        Button cancelButton = (Button) dialogView.findViewById(R.id.cancel);
        Button okButton = (Button) dialogView.findViewById(R.id.ok);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (input.getText().toString().isEmpty()) {
                    return;
                }

                if (useCPFMask) {
                    String cpf = CPFMask.unmask(input.getText().toString());

                    if (!CPFMask.validateCPF(cpf)) {
                        input.setText("");
                        input.requestFocus();
                        input.setHint(R.string.invalidCPF);
                        return;
                    }
                }

                Bundle args = new Bundle();
                args.putString("INPUT", input.getText().toString().trim());
                args.putInt("RESULT", AndroidDialog.Result.OK.ordinal());

                Message msg = new Message();
                msg.setData(args);
                responseHandler.sendMessage(msg);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseHandler.sendEmptyMessage(AndroidDialog.Result.CANCEL.ordinal());
                dialog.dismiss();
            }
        });

        ImageView passwordVisibility = (ImageView) dialogView.findViewById(R.id.login_password_visibility);

        if (type  == InputType.PASSWORD) {

            passwordVisibility.setVisibility(View.VISIBLE);
            configurePasswordField(activity, input, passwordVisibility);
        }
        else {

            passwordVisibility.setVisibility(View.GONE);

            if (type == InputType.TEXT) {
                input.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            }
            else if (type == InputType.NUMBER) {
                input.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            }
        }

        input.setHint(placeholder);
        input.setHintTextColor(activity.getResources().getColor(R.color.gray));

        dialogBuilder.setCancelable(true);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.show();
    }

    private static void configurePasswordField(final Activity activity, final EditText passwordEdit,
                                        final ImageView passwordVisibility) {

        // Just show password visibility (eye) if password length greater than zero
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordVisibility.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });

        // Set password visibility (eye) behavior
        passwordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passwordEdit.getSelectionStart() != 0) {
                    // Change input type will reset cursor position, so we want to save it
                    final Integer cursor = passwordEdit.getSelectionStart();

                    if (!isShowingPassword) {
                        passwordEdit.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                        isShowingPassword = true;

                        passwordVisibility.setImageResource(R.drawable.ic_visibility_on);

                    } else {
                        passwordEdit.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        isShowingPassword = false;

                        passwordVisibility.setImageResource(R.drawable.ic_visibility_off);
                    }

                    passwordVisibility.setColorFilter(activity.getResources().getColor(R.color.darkGray),
                            PorterDuff.Mode.SRC_ATOP);

                    if (cursor != null) {
                        passwordEdit.setSelection(cursor);
                    } else {
                        passwordEdit.setSelection(0);
                    }
                }
            }
        });
    }
}
