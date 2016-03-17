package eda.eda;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialog {
    private AlertDialog.Builder dialog;

    public Dialog(Context context,String massage,String buttonText){
        dialog = new AlertDialog.Builder(context);
        dialog.setMessage(massage);
        dialog.setCancelable(false);
        dialog.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    public Dialog(Context context,String massage,String buttonText,String secButtonText){
        dialog = new AlertDialog.Builder(context);
        dialog.setMessage(massage);
        dialog.setCancelable(false);
        dialog.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.setNeutralButton(secButtonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        dialog.show();
    }
}
