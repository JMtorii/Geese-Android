package com.teamawesome.geese.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Cody on 2016-02-11.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int mId;
    private DatePickerDialogListener mListener;

    public static DatePickerFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("picker_id", id);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        mId = getArguments().getInt("picker_id");
        mListener = getActivity() instanceof DatePickerDialogListener ? (DatePickerDialogListener) getActivity() : null;

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mListener != null) mListener.onDateSet(mId, view, year, monthOfYear, dayOfMonth);
    }

    public interface DatePickerDialogListener {
        void onDateSet(int id, DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

}
