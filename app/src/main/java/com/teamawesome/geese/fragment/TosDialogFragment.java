package com.teamawesome.geese.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.widget.TextView;

import com.teamawesome.geese.R;

/**
 * Created by lcolam on 10/20/15.
 */
public class TosDialogFragment extends DialogFragment {

    public static TosDialogFragment newInstance() {
        TosDialogFragment frag = new TosDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.GeeseDialogBoxTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw)
                .setTitle(getString(R.string.terms_of_services))
                .setMessage(R.string.tos_legal_stuff)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}