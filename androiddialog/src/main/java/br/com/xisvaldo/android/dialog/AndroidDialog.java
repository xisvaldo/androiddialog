package br.com.xisvaldo.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by leonardo.borges on 13/09/2016.
 */
public class AndroidDialog {

    private static AlertDialog.Builder dialogBuilder;
    private static Handler responseHandler;
    private static AlertDialog dialog;
    private final static int MAX_TITLE_LENGTH = 20;
    private final static int MAX_MESSAGE_LENGTH = 140;

    public enum Type {
        INFO,
        SUCCESS,
        ERROR,
        QUESTION
    }

    public enum Result {
        OK,
        YES,
        NO,
        CANCEL
    }

    public static void show(Activity activity, Type type, String title,
                             String message, Handler handler) throws IOException {

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IOException("Max allowed title length is " + MAX_TITLE_LENGTH + ".");
        }
        else if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new IOException("Max allowed message length is " + MAX_MESSAGE_LENGTH + ".");
        }

        responseHandler = handler;
        dialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.android_dialog, null);

        TextView titleTV = (TextView) dialogView.findViewById(R.id.title);
        titleTV.setText(title);

        TextView messageTV = (TextView) dialogView.findViewById(R.id.dialog_info);
        messageTV.setText(message);

        RelativeLayout header = (RelativeLayout) dialogView.findViewById(R.id.header);
        Button yesButton = (Button) dialogView.findViewById(R.id.yes);
        Button noButton = (Button) dialogView.findViewById(R.id.no);
        Button okButton = (Button) dialogView.findViewById(R.id.ok);

        if (type  == Type.QUESTION) {

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    responseHandler.sendEmptyMessage(Result.YES.ordinal());
                    dialog.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    responseHandler.sendEmptyMessage(Result.NO.ordinal());
                    dialog.dismiss();
                }
            });

            header.setBackgroundColor(activity.getColor(R.color.yellow));

            okButton.setVisibility(View.GONE);

        }
        else {

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    responseHandler.sendEmptyMessage(Result.OK.ordinal());
                    dialog.dismiss();
                }
            });

            if (type == Type.INFO) {
                header.setBackgroundColor(activity.getColor(R.color.lightBlue));
            }
            else if (type == Type.SUCCESS) {
                header.setBackgroundColor(activity.getColor(R.color.green));
            }
            else {
                header.setBackgroundColor(activity.getColor(R.color.red));
            }

            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
        }

        dialogBuilder.setCancelable(true);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.show();
    }

}
