package me.tuong.chodinh.main.more.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import me.tuong.chodinh.R;

public class CustomDialog extends Dialog {

    interface FullNameListener {
        public void fullNameEntered(String fullName);
    }

    public Context context;

    private CustomDialog.FullNameListener listener;

    public CustomDialog(Context context, CustomDialog.FullNameListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_password);

    }
}
