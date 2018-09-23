package com.jeffreyromero.materialestimator.project;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.jeffreyromero.materialestimator.R;


/**
 * Displays a dialog guiding the user on how to create a project item.
 */
public class AddProjectItemMessageDialog extends DialogFragment {

    public AddProjectItemMessageDialog() {
        // Required empty public constructor
    }

    public static AddProjectItemMessageDialog newInstance() {
        return new AddProjectItemMessageDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the view
        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.add_project_item_message_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
//        dialogBuilder.setTitle(getActivity().getString(R.string.projectItemCreatorFragment_title));
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return dialogBuilder.create();
    }
}
