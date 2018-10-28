package com.jeffreyromero.materialestimator.Item;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.BaseMaterial;

//todo - convert to EditSingleMaterialPropertyDialog if needed

public class EditQuantityDialog extends DialogFragment {
    public OnDialogSubmitListener mListener;

    private static final String POSITION = "position";
    private static final String ITEM = "item";
    private int position;
    private BaseMaterial material;

    /**
     * Displays a dialog for editing the quantity of a material.
     * @param position The position of the material in the MaterialList.
     * @param item The item that the materialList belongs to.
     * @return Returns the position and user input to the listener.
     */
    public static EditQuantityDialog newInstance(int position, BaseItem item) {
        EditQuantityDialog fragment = new EditQuantityDialog();
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putString(ITEM, new Gson().toJson(item));
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnDialogSubmitListener {
        void OnEditQuantityDialogSubmit(int position, double userInput);
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
                            "SingleTextInputDialog.OnDialogSubmitListener");
        }

        // Get arguments from the bundle
        position = getArguments().getInt(POSITION);
        material = Deserializer.toItem(getArguments().getString(ITEM))
                .getMaterialList()
                .get(position);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_quantity_dialog, null);

        // Setup views
        TextInputLayout quantityTIL = dialogView.findViewById(R.id.quantityTIL);
        quantityTIL.setHint(String.valueOf(material.getQuantity()));
        final EditText quantityET = dialogView.findViewById(R.id.quantityET);

        // Build dialog
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(dialogView);
        dialog.setTitle("Edit Quantity");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Pass user input to the listener if it's not empty.
                if (!quantityET.getText().toString().equals("")){
                    mListener.OnEditQuantityDialogSubmit(
                            position,
                            Double.valueOf(quantityET.getText().toString())
                    );
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Show keyboard.
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public EditQuantityDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
