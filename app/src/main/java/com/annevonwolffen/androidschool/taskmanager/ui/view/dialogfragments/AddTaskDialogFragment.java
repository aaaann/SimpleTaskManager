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
import android.widget.CheckBox;
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

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.intDateFromStringDate;
import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.intTimeFromStringDate;
import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.stringToDate;

public class AddTaskDialogFragment extends DialogFragment {

    private final static String TAG = "AddTaskDialogFragment";
    private static final String ARG_ID = "argId";
    private static final String ARG_TITLE = "argTitle";
    private static final String ARG_DATE = "argDate";
    private static final String ARG_IS_NOTIF = "argNotif";

    public interface AddDialogListener {

        void onDialogPositiveClick(long id, String title, Date dateTime, boolean isNotifEnabled);

        void onDialogNegativeClick();
    }

    AddDialogListener addDialogListener;

    public static AddTaskDialogFragment newInstance(long id, String title, String date, boolean isNotifEnabled) {

        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DATE, date);
        args.putBoolean(ARG_IS_NOTIF, isNotifEnabled);

        AddTaskDialogFragment fragment = new AddTaskDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

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

        final EditText etTitle = root.findViewById(R.id.et_task_title);
        etTitle.setText(getArguments() != null ? getArguments().getString(ARG_TITLE) : null);

        final CheckBox chbxAddNotification = root.findViewById(R.id.chbx_add_notif);
        chbxAddNotification.setChecked(getArguments() != null && getArguments().getBoolean(ARG_IS_NOTIF));

        final StringBuilder date = new StringBuilder();
        final StringBuilder time = new StringBuilder();
        final DatePicker taskDate = root.findViewById(R.id.date_picker);
        taskDate.init(getInitDataForDatePicker()[2], getInitDataForDatePicker()[1], getInitDataForDatePicker()[0], (view, year, monthOfYear, dayOfMonth) -> {
            date.setLength(0);
            date.append(defineDate(year, monthOfYear, dayOfMonth));
        });
        date.append(defineDate(taskDate.getYear(), taskDate.getMonth(), taskDate.getDayOfMonth()));

        final TimePicker taskTime = root.findViewById(R.id.time_picker);
        taskTime.setIs24HourView(true);
        taskTime.setCurrentHour(getInitDataForTimePicker()[0]);
        taskTime.setCurrentMinute(getInitDataForTimePicker()[1]);
        time.append(defineTime(taskTime.getCurrentHour(), taskTime.getCurrentMinute()));
        taskTime.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            time.setLength(0);
            time.append(defineTime(hourOfDay, minute));
        });

        builder.setView(root)
                .setPositiveButton("ОК", (dialog, which) -> {

                    String title = etTitle.getText().toString();
                    boolean isNotifEnabled = chbxAddNotification.isChecked();
                    String dateTime = date.toString() + " " + time.toString();
                    addDialogListener.onDialogPositiveClick(getArguments() != null ? getArguments().getLong(ARG_ID) : -1, title, stringToDate(dateTime), isNotifEnabled);
                    dialog.dismiss();
                })
                .setNegativeButton("Отмена", (dialog, which) -> {

                    addDialogListener.onDialogNegativeClick();
                    dialog.cancel();
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
    }


    private String defineTime(int hourOfDay, int minute) {
        return hourOfDay + ":" + minute;
    }

    private int[] getInitDataForDatePicker() {
        if (getArguments() != null) {
            int[] aDate = intDateFromStringDate(getArguments().getString(ARG_DATE));
            return new int[] {aDate[0], aDate[1] - 1, aDate[2]};
        } else {
            Calendar calendar = Calendar.getInstance();
            return new int[] {calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)};
        }
    }

    private int[] getInitDataForTimePicker() {
        if (getArguments() != null) {
            return intTimeFromStringDate(getArguments().getString(ARG_DATE));
        } else {
            Calendar calendar = Calendar.getInstance();
            return new int[] {calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)};
        }
    }


}
