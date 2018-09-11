package com.jeffreyromero.materialestimator.material;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.MaterialListsDataSource;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;


/**
 * Displays the dialog for adding a Material.
 */
public class AddMaterialDialog extends DialogFragment {

    private OnItemChangeListener onItemChangeListener;
    private Material selectedMaterial;
    private View dialogView;
    private Context context;

    public interface OnItemChangeListener {
        void onAddMaterialDialogSubmit(Material material);
    }

    /**
     * Used for adding a material.
     *
     * @return the added material.
     */
    public static AddMaterialDialog newInstance() {
        return new AddMaterialDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Set the context.
        this.context = context;
        //Get the hosting fragment and ensure that it implements onItemChangeListener.
        try {
            onItemChangeListener = (OnItemChangeListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting Fragment must implement AddMaterialDialog.onItemChangeListener"
            );
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Inflate the view
        dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.add_material_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Add Material");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Update the selected material with user input values.
                EditText priceET = dialogView.findViewById(R.id.priceET);
                selectedMaterial.setUnitPrice(Integer.valueOf(priceET.getText().toString()));
                //Send the new selected material to the listener.
                onItemChangeListener.onAddMaterialDialogSubmit(selectedMaterial);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        initSpinner();

        setDataToViews(dialogView, selectedMaterial);

        return dialogBuilder.create();
    }

    private void initSpinner() {
        //Get the default material list from App Shared Preferences.
        MaterialListsDataSource appSPHelper = new MaterialListsDataSource(
                getString(R.string.default_material_lists),
                context
        );
        MaterialList spinnerList = appSPHelper
                .get(context.getString(R.string.default_material_list));
        Spinner spinner = dialogView.findViewById(R.id.spinner);

        //Make selected material equal to the first item in the list.
        selectedMaterial = spinnerList.getList().get(0);

        //Create an ArrayAdapter
        ArrayAdapter<Material> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                spinnerList.getList()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Get the selected material from the spinner
                AddMaterialDialog.this.selectedMaterial =
                        (Material) parent.getItemAtPosition(position);

                setDataToViews(dialogView, selectedMaterial);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    private void setDataToViews(View view, Material selectedMaterial) {
        //Get views.
        TextView nameTV = view.findViewById(R.id.nameTV);
        EditText priceET = view.findViewById(R.id.priceET);
        TextView lengthTV = view.findViewById(R.id.lengthTV);
        TextView widthTV = view.findViewById(R.id.widthTV);

        //Set values.
        nameTV.setText(selectedMaterial.getName());
        priceET.setText(String.valueOf(selectedMaterial.getPrice()));
        lengthTV.setText(String.valueOf(selectedMaterial.getLength()));
        widthTV.setText(String.valueOf(selectedMaterial.getWidth()));
    }

    public AddMaterialDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onItemChangeListener = null;
    }
}
