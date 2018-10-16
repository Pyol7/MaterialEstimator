package com.jeffreyromero.materialestimator.material;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.models.BaseMaterial;


/**
 * Displays the dialog for editing a BaseMaterial.
 */
public class EditMaterialDialog extends DialogFragment {

    private OnItemChangeListener onItemChangeListener;
    private BaseMaterial selectedMaterial;
    private View dialogView;
    private Context context;

    public interface OnItemChangeListener {
        void onEditMaterialDialogSubmit(BaseMaterial material);
    }

    /**
     * Used for editing a material.
     *
     * @param inputMaterial the clicked material.
     * @return the edited material.
     */
    public static EditMaterialDialog newInstance(BaseMaterial inputMaterial) {
        EditMaterialDialog fragment = new EditMaterialDialog();
        //Add input data to the bundle.
        Bundle args = new Bundle();
        String inputMaterialJson = new Gson().toJson(inputMaterial);
        args.putString("inputMaterial", inputMaterialJson);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Set the context.
        this.context = context;
        // Get the hosting fragment and ensure that it implements onItemChangeListener.
        try {
            onItemChangeListener = (OnItemChangeListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting Fragment must implement EditMaterialDialog.onItemChangeListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String inputMaterialJson = null;
        try {
            inputMaterialJson = getArguments().getString("inputMaterial");
            //Make the input material the selected material.
            this.selectedMaterial = Deserializer.toMaterial(inputMaterialJson);
        } catch (NullPointerException e) {
            Log.e("EditMaterialDialog", e.getMessage());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate the view
        dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.edit_material_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Edit BaseMaterial");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Update the selected material with user input values.
                EditText priceET = dialogView.findViewById(R.id.priceET);
                selectedMaterial.setUnitPrice(Integer.valueOf(priceET.getText().toString()));
                //Send the new selected material to the listener.
                onItemChangeListener.onEditMaterialDialogSubmit(selectedMaterial);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        setDataToViews(dialogView, selectedMaterial);

        return dialogBuilder.create();
    }

    private void setDataToViews(View view, BaseMaterial selectedMaterial) {
        //Get views.
        TextView nameTV = view.findViewById(R.id.nameLabelTV);
        EditText priceET = view.findViewById(R.id.priceET);
        priceET.clearFocus();
        TextView lengthTV = view.findViewById(R.id.lengthET);
        TextView widthTV = view.findViewById(R.id.widthET);

        //Set values.
        nameTV.setText(selectedMaterial.getName());
        priceET.setText(String.valueOf(selectedMaterial.getPrice()));
        lengthTV.setText(String.valueOf(selectedMaterial.getLength()));
        widthTV.setText(String.valueOf(selectedMaterial.getWidth()));
    }

    public EditMaterialDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onItemChangeListener = null;
    }
}
