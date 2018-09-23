package com.jeffreyromero.materialestimator.project;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.jeffreyromero.materialestimator.R;


/**
 * Displays a dialog to create a new ic_project and passes
 * the name and description back to the hosting fragment.
 */
public class AddProjectDialog extends DialogFragment {

    private OnCreateListener mListener;

    public AddProjectDialog() {
        // Required empty public constructor
    }

    public static AddProjectDialog newInstance() {
        return new AddProjectDialog();
    }

    public interface OnCreateListener {
        void onAddProjectDialogSubmit(String name, String client, String location);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Ensure that the host implements the callbacks.
        try {
            mListener = (OnCreateListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting fragment must implement AddProjectDialog.OnCreateListener"
            );
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the view
        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.add_project_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(getActivity().getString(R.string.add_project_dialog_title));
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Get user input.
                EditText nameET = dialogView.findViewById(R.id.nameET);
                EditText clientET = dialogView.findViewById(R.id.clientET);
                EditText locationET = dialogView.findViewById(R.id.locationET);
                String name = nameET.getText().toString();
                String client = clientET.getText().toString();
                String location = locationET.getText().toString();

                //Inform the listener of the new ic_project created.
                mListener.onAddProjectDialogSubmit(name, client, location);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        return dialogBuilder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Show keyboard.
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
