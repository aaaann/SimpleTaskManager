package com.annevonwolffen.androidschool.taskmanager.ui.view.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.Calendar;
import java.util.Date;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.stringToDate;

public class AddTaskDialogFragment extends DialogFragment  {

    private final static String TAG = "AddTaskDialogFragment";

    public interface AddDialogListener {

        public void onDialogPositiveClick(String title, Date dateTime, boolean isNotifEnabled);
        public void onDialogNegativeClick();
    }
    AddDialogListener addDialogListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            addDialogListener = (AddDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement AddDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View root = inflater.inflate(R.layout.dialog_add_task, null);
        final EditText etTitle  = root.findViewById(R.id.et_task_title);
        final StringBuilder date = new StringBuilder();
        final StringBuilder time = new StringBuilder();
        final DatePicker taskDate = root.findViewById(R.id.date_picker);
        Calendar calendar = Calendar.getInstance();
        taskDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d(TAG, "onDateChanged() called with: view = [" + view + "], year = [" + year + "], monthOfYear = [" + monthOfYear + "], dayOfMonth = [" + dayOfMonth + "]");
                date.setLength(0);
                date.append(defineDate(year, monthOfYear, dayOfMonth));
            }
        });
        date.append(defineDate(taskDate.getYear(), taskDate.getMonth(), taskDate.getDayOfMonth()));

        final TimePicker taskTime = root.findViewById(R.id.time_picker);
        taskTime.setIs24HourView(true);
        taskTime.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        time.append(defineTime(taskTime.getCurrentHour(), taskTime.getCurrentMinute()));
        taskTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "onTimeChanged() called with: view = [" + view + "], hourOfDay = [" + hourOfDay + "], minute = [" + minute + "]");
                time.setLength(0);
                time.append(defineTime(hourOfDay, minute));
            }
        });
        builder.setView(root)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String title = etTitle.getText().toString();
                        boolean isNotifEnabled = root.findViewById(R.id.chbx_add_notif).isEnabled();
                        String dateTime = date.toString() + " " + time.toString();
                        Log.d(TAG, "onPositiveButtonClick: " + dateTime);
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

    private String defineDate(int year, int monthOfYear, int dayOfMonth) {
        return dayOfMonth + "." +
                (monthOfYear + 1) + "." +
                year;
    };

    private String defineTime(int hourOfDay, int minute) {
        return hourOfDay + ":" + minute;
    }


}
