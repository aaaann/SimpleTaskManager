package com.annevonwolffen.androidschool.taskmanager.ui.view.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.stringToDate;

public class AddTaskDialogFragment extends DialogFragment {

    public interface AddDialogListener {
        public void onDialogPositiveClick(String title, Date dateTime, boolean isNotifEnabled);
        public void onDialogNegativeClick();
    }

    AddDialogListener addDialogListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            addDialogListener = (AddDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View root = inflater.inflate(R.layout.dialog_add_task, null);
//        TextInputLayout tilTitle = root.findViewById(R.id.til_title);
        final EditText etTitle  = root.findViewById(R.id.et_task_title);
        final DatePicker taskDate = root.findViewById(R.id.date_picker);
        final TimePicker taskTime = root.findViewById(R.id.time_picker);
        builder.setView(root)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String title = etTitle.getText().toString();
                        boolean isNotifEnabled = root.findViewById(R.id.chbx_add_notif).isEnabled();
                        // todo: get date and time and convert to date
                        String date = taskDate.getDayOfMonth() + "." + (taskDate.getMonth() + 1) + "." + taskDate.getYear();
                        String time = taskTime.getCurrentHour() + " : " + taskTime.getCurrentMinute();
                        String dateTime = date + " " + time;
                        addDialogListener.onDialogPositiveClick(title, stringToDate(dateTime), isNotifEnabled);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addDialogListener.onDialogNegativeClick();
                        dialog.cancel();
                    }
                });

        AlertDialog addDialog = builder.create();
        addDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            TextInputLayout tilTitle = root.findViewById(R.id.til_title);
            @Override
            public void onShow(DialogInterface dialog) {
                final Button okButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                if (etTitle.getText().toString().isEmpty()) {
                    okButton.setEnabled(false);
                    tilTitle.setError("Введите задачу");
                }

                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            okButton.setEnabled(false);
                            tilTitle.setError("");
                        } else {
                            okButton.setEnabled(true);
                            tilTitle.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            }
        });

        return addDialog;
    }

}
