package com.jeffreyromero.materialestimator.utilities.dialogCreateNewItem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.ItemTypes.DrywallPartition;

import java.util.Locale;


public class DialogDrywallPartitionDetails extends Fragment implements CreateNewItemDialogDetails{

    private static final String ITEM_TYPE = "item type";
    private DrywallPartition itemType;
    private View view;
    private OnFragmentInteractionListener mListener;

    public DialogDrywallPartitionDetails() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener {
        void loadDialogCreateItem(BaseItem itemType);
    }

    public static DialogDrywallPartitionDetails newInstance(BaseItem itemType) {
        DialogDrywallPartitionDetails fragment = new DialogDrywallPartitionDetails();
        Bundle args = new Bundle();
        args.putString(ITEM_TYPE, new Gson().toJson(itemType));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Enforce interface methods
        try {
            mListener = (DialogDrywallPartitionDetails.OnFragmentInteractionListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The host must implement this OnDialogSubmitListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(ITEM_TYPE);
            // Cast to item type after deserialization.
            itemType = (DrywallPartition) Deserializer.toItemType(json);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dialog_drywall_partition_details, container, false);

        // Make this fragment capable of modifying the options menu.
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Empty the option menu and add the next button.
        menu.clear();
        MenuItem nextItem = menu.add(Menu.NONE, R.id.action_next, 10, R.string.action_next);
        nextItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        nextItem.setIcon(R.drawable.ic_arrow_next);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                addUserInputToItemType();
                mListener.loadDialogCreateItem(itemType);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void addUserInputToItemType() {
        // todo - input validation.
        EditText nameET = view.findViewById(R.id.name_et);
        EditText lengthET = view.findViewById(R.id.length_et);
        EditText heightET = view.findViewById(R.id.height_et);

        String name = nameET.getText().toString();
        double length = Double.parseDouble(lengthET.getText().toString());
        double height = Double.parseDouble(heightET.getText().toString());

        String dims = String.format(
                Locale.US,
                "%.1fft x %.1fft  (%.0fSF)",
                length,
                height,
                length * height);

        itemType.setName(name + " " + dims);
        itemType.setLength(feetToInches(length));
        itemType.setHeight(feetToInches(height));

        itemType.setLayersOfBoards(determineBoardLayers());
        itemType.setTotalOpeningArea(calcTotalOpeningArea());
    }

    @Override
    public double feetToInches(double feet) {
        return feet * 12;
    }

    private int determineBoardLayers(){
        RadioButton doubleLayerBoardRadio = view.findViewById(R.id.double_Layer_board_radio);
        int layer = 1;
        if (doubleLayerBoardRadio.isChecked()) {
            layer = 2;
        }
        return layer;

    }

    private double calcTotalOpeningArea(){
        EditText length1ET = view.findViewById(R.id.opening_1_length_et);
        EditText height1ET = view.findViewById(R.id.opening_1_height_et);
        EditText length2ET = view.findViewById(R.id.opening_2_length_et);
        EditText height2ET = view.findViewById(R.id.opening_2_height_et);
        double length1 = Double.parseDouble(length1ET.getText().toString());
        double height1 = Double.parseDouble(height1ET.getText().toString());
        double length2 = Double.parseDouble(length2ET.getText().toString());
        double height2 = Double.parseDouble(height2ET.getText().toString());
        return  (length1*height1) + (length2*height2);
    }
}
