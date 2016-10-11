package br.com.xisvaldo.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by leonardo.borges on 26/09/2016.
 */
public class AndroidProgressDialog {

    private static AlertDialog.Builder dialogBuilder;
    private static AlertDialog dialog;
    private final static int MAX_TITLE_LENGTH = 20;
    private final static int MAX_MESSAGE_LENGTH = 140;

    public static void show(Activity activity, String title,
                            String message) throws IOException {

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IOException("Max allowed title length is " + MAX_TITLE_LENGTH + ".");
        }
        else if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new IOException("Max allowed message length is " + MAX_MESSAGE_LENGTH + ".");
        }

        dialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.android_progressdialog, null);

        TextView titleTV = (TextView) dialogView.findViewById(R.id.title);
        titleTV.setText(title);

        TextView messageTV = (TextView) dialogView.findViewById(R.id.dialog_info);
        messageTV.setText(message);

        ProgressBar progress = (ProgressBar) dialogView.findViewById(R.id.progress);
        progress.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.lightBlue), PorterDuff.Mode.SRC_ATOP);

        dialogBuilder.setCancelable(true);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.show();
    }

    public static void dismiss() {
        dialog.dismiss();
    }
}
