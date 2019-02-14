package com.jeffreyromero.materialestimator.utilities.dialogCreateNewItem;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.MainActivity;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.models.BaseItem;

import java.util.ArrayList;

/**
 * This is the First menu that shows when creating a new item.
 * Current nested menus are:
 *      DialogSelectItemType
 *      DialogDroppedCeilingDetails
 *      DialogDrywallCeilingDetails
 *      DialogDrywallPartitionDetails
 *      DialogCreateItem
 * DialogCreateItem finally passes the newly created item back to Project.
 */
public class DialogSelectItemType extends Fragment {

    private static final String ITEM_TYPES = "itemTypes";
    private ArrayList<BaseItem> itemTypes;
    private int selectedItemTypeIndex;
    private MainActivity mainActivity;
    private OnFragmentInteractionListener mListener;

    public DialogSelectItemType() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener {
        void onDialogSelectItemTypeNextBtnClick(BaseItem selectedItemType);
    }

    public static DialogSelectItemType newInstance(ArrayList<BaseItem> itemTypes) {
        DialogSelectItemType fragment = new DialogSelectItemType();
        Bundle args = new Bundle();
        args.putString(ITEM_TYPES, new Gson().toJson(itemTypes));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Enforce interface methods
        try {
            mListener = (DialogSelectItemType.OnFragmentInteractionListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The host must implement this OnDialogSubmitListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        if (getArguments() != null) {
            String json = getArguments().getString(ITEM_TYPES);
            itemTypes = Deserializer.toItemTypes(json);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_select_item_type, container, false);

        // Make this fragment capable of modifying the options menu.
        setHasOptionsMenu(true);

        RecyclerView itemTypesRV = view.findViewById(R.id.itemTypes_rv);
        itemTypesRV.setAdapter(new itemTypesAdaptor());

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
                if (mListener != null) {
                    mListener.onDialogSelectItemTypeNextBtnClick(itemTypes.get(selectedItemTypeIndex));
                }
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

    private class itemTypesAdaptor extends RecyclerView.Adapter<itemTypesAdaptor.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            // Inflate the item view and pass into view holder.
            return new ViewHolder(LayoutInflater.from(mainActivity).inflate(
                    R.layout.radio_textview_rv_list_item, viewGroup, false));
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            // Bind data to views in view holder
            viewHolder.radioBtnLabel.setText(itemTypes.get(i).getSubType());
            viewHolder.radioButton.setChecked(selectedItemTypeIndex == i);
        }
        @Override
        public int getItemCount() {
            return itemTypes.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView radioBtnLabel;
            RadioButton radioButton;
            ViewHolder(View view) {
                super(view);
                radioButton = view.findViewById(R.id.radio_btn);
                radioBtnLabel = view.findViewById(R.id.radio_btn_label_tv);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedItemTypeIndex = getAdapterPosition();
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
//Toast.makeText(mainActivity,"Item type: " + itemTypes[selectedItemTypeIndex],Toast.LENGTH_LONG).show();
