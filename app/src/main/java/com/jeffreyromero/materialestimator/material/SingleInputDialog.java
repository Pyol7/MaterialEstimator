package com.jeffreyromero.materialestimator.material;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jeffreyromero.materialestimator.R;


public class SingleInputDialog extends DialogFragment {
    public OnDialogSubmitListener mListener;

    /**
     * Displays a single input dialog.
     * @param title The title of the dialog.
     * @param placeholder The placeholder text.
     * @return userInput to listener.
     */
    public static SingleInputDialog newInstance(String title, String placeholder) {
        SingleInputDialog fragment = new SingleInputDialog();
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("placeholder", placeholder);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnDialogSubmitListener {
        void OnSingleInputDialogSubmit(String userInput);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the hosting fragment to implement this callback interface.
        try {
            mListener = (OnDialogSubmitListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting Fragment must implement " +
                            "SingleInputDialog.OnDialogSubmitListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.single_input_dialog, null);
        // Get the title and placeholder from the bundle.
        String title = getArguments().getString("title");
        String placeholder = getArguments().getString("placeholder");
        // Set the placeholder to the inputET.
        final EditText inputET = dialogView.findViewById(R.id.inputET);
        inputET.setText(placeholder);
        //Build dialog
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(dialogView);
        dialog.setTitle(title);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Get user input
                String userInput = inputET.getText().toString();
                // Pass userInput to the listener if it's not empty.
                if (!userInput.isEmpty()) {
                    mListener.OnSingleInputDialogSubmit(userInput);
                }

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        return dialog.create();
    }

    public SingleInputDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
