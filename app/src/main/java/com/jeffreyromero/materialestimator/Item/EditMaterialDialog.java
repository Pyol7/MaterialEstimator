package com.jeffreyromero.materialestimator.Item;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.quantifiables.JointCompound;

import java.util.Locale;


/**
 * Receives an ItemType for editing.
 * After editing, sends it back to the listener.
 */
public class EditMaterialDialog extends DialogFragment {

    private static final String MATERIAL = "material";
    private static final String POSITION = "position";
    private OnItemChangeListener onItemChangeListener;
    private BaseMaterial material;
    private View dialogView;
    private Context context;
    private int position;

    EditText nameET;
    EditText unitPriceET;
    EditText lengthET;
    EditText widthET;
    EditText spacingET;
    EditText coverageET;
    EditText coefficientET;
    SeekBar seekBar;

    public EditMaterialDialog() {
        // Required empty public constructor
    }

    public interface OnItemChangeListener {
        void onEditMaterialDialogSubmit(BaseMaterial material, int position);
    }

    // Receives a material from a listener for editing
    public static EditMaterialDialog newInstance(BaseMaterial material, int position) {
        EditMaterialDialog fragment = new EditMaterialDialog();
        Bundle args = new Bundle();
        args.putString(MATERIAL, new Gson().toJson(material));
        args.putInt(POSITION, position);
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
                    "The hosting Fragment must implement EditMaterialDialog.OnItemChangeListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String json = getArguments().getString(MATERIAL);
            //Make the input material the selected material.
            this.material = Deserializer.toMaterial(json);
            this.position = getArguments().getInt(POSITION);
        } catch (Exception e) {
            Log.e("EditMaterialDialog", e.getMessage());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate the view
        dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.edit_material_dialog, null);

        //todo - input validation for all
        // Get individual views
        nameET = dialogView.findViewById(R.id.projectNameET);
        unitPriceET = dialogView.findViewById(R.id.unitPriceET);
        // Add a text change listener to detect when a value of -1 (or -1.0) is set.
        // Which indicates that a field is not used therefore should be hidden.
        lengthET = dialogView.findViewById(R.id.lengthET);
        lengthET.addTextChangedListener(
                new inputValidator(dialogView.findViewById(R.id.lengthLabelTV), lengthET)
        );
        widthET = dialogView.findViewById(R.id.widthET);
        widthET.addTextChangedListener(
                new inputValidator(dialogView.findViewById(R.id.widthLabelTV), widthET)
        );
        spacingET = dialogView.findViewById(R.id.spacingET);
        spacingET.addTextChangedListener(
                new inputValidator(dialogView.findViewById(R.id.spacingLabelTV), spacingET)
        );
        coefficientET = dialogView.findViewById(R.id.coefficientET);
        coefficientET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //todo - sanitize user input
                // This controls the seek bar via user input
                if (!(s.toString().equals("") || s.toString().equals("."))){
                    seekBar.setProgress(Integer.valueOf(s.toString()));
                }
            }
        });

        if (material instanceof JointCompound){
            coverageET = dialogView.findViewById(R.id.coverageET);
            coverageET.addTextChangedListener(
                    new inputValidator(dialogView.findViewById(R.id.coverageLabelTV), coverageET)
            );
        }

        // Build the dialog view
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Edit Material");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // todo - check for empty submission
                // Update material from user input
                material.setName(nameET.getText().toString());
                material.setUnitPrice(Double.valueOf(unitPriceET.getText().toString()));
                material.setLength(Double.valueOf(lengthET.getText().toString()));
                material.setWidth(Double.valueOf(widthET.getText().toString()));
                material.setSpacing(Integer.valueOf(spacingET.getText().toString()));
                material.setCoefficient(toCoefficient(seekBar.getProgress()));
                if (material instanceof JointCompound){
                    ((JointCompound) material).setCoverage(
                            Integer.valueOf(coverageET.getText().toString())
                    );
                }
                // Send the edited material back to the listener.
                onItemChangeListener.onEditMaterialDialogSubmit(material, position);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        seekBar = dialogView.findViewById(R.id.coefficientSB);
        seekBar.setOnSeekBarChangeListener(new CoefficientSeekBarChangeListener());

        setDataToViews(material);

        return dialogBuilder.create();
    }

    private class CoefficientSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser && material.getCoefficient() != toCoefficient(seekBar.getProgress())){
                coefficientET.setText(String.valueOf(seekBar.getProgress()));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }

    }

    // Actual progress:                 Min=0, Max=50
    // As Coefficient ((p/100) + 1):    Min=1, Max=1.5
    private double toCoefficient(int progress){
        return ((double)progress / 100) + 1;
    }

    private void setDataToViews(BaseMaterial material) {
        nameET.setText(
                material.getName()
        );
        unitPriceET.setText(
                String.format(Locale.US,"%.2f",material.getUnitPrice())
        );
        lengthET.setText(
                String.format(Locale.US,"%.1f",material.getLength())
        );
        widthET.setText(
                String.format(Locale.US,"%.1f",material.getWidth())
        );
        spacingET.setText(
                String.valueOf(material.getSpacing())
        );
        // This textChangeListener is activated which updates the seekBar
        coefficientET.setText(
                String.format(Locale.US,"%.0f", (material.getCoefficient() - 1) * 100)
        );
        if (material instanceof JointCompound){
            coverageET.setText(
                 String.valueOf(((JointCompound) material).getCoverage())
            );
        }
    }

    private class inputValidator implements TextWatcher {

        private String beforeText;
        private View labelTV;
        private EditText inputET;

        public inputValidator(View labelTV, EditText inputET) {
            this.labelTV = labelTV;
            this.inputET = inputET;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Don't store an empty string
            if (!s.toString().equals("")) {
                this.beforeText = s.toString();
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Hide the view that has a value of -1 or -1.0
            String text = s.toString();
            if (text.equals(String.valueOf(-1)) ||
                    String.valueOf(s).equals(String.valueOf(-1.0))){
                labelTV.setVisibility(View.GONE);
                inputET.setVisibility(View.GONE);
            }
            // Prohibit zero input
            if (text.equals(String.valueOf(0)) || text.equals(".")){
                Toast.makeText(
                        context,
                        "Enter a value greater than zero",
                        Toast.LENGTH_LONG
                ).show();
                inputET.setText(beforeText);
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onItemChangeListener = null;
    }
}
