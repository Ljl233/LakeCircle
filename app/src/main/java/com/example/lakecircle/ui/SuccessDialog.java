package com.example.lakecircle.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.lakecircle.R;

public class SuccessDialog extends DialogFragment {

    private static final String ARG_DIALOG = "make sure";

    /**
     *
     * @param content dialog 所显示的内容
     * @return
     */
    public static SuccessDialog newInstance(String content) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DIALOG, content);

        SuccessDialog dialog = new SuccessDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());

        String content = (String) (getArguments() != null ? getArguments().getSerializable(ARG_DIALOG) : "");

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_success, null);

        TextView textView = v.findViewById(R.id.dialog_content);
        textView.setText(content);

        Button mOk = v.findViewById(R.id.dialog_bt_ok);
        mOk.setOnClickListener(a -> {
            dialog.dismiss();
        });
        dialog.setContentView(v);

        return dialog;
    }
}
