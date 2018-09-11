package com.jeffreyromero.materialestimator.material;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeffreyromero.materialestimator.R;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class SingleSelectDialog extends DialogFragment {
    public OnDialogSubmitListener mListener;
    private ArrayList<String> itemList;
    private int selectedPosition;
    private Adapter adapter;
    private String title;

    /**
     * Displays a dialog with a list of option to select from.
     * Only one of which can be selected.
     *
     * @param title <String> The title of the dialog.
     * @param itemList Set<String> An array of items.
     * @param selectedPosition <String> The currently selected item position.
     * @return <int> The selected item position.
     */
    public static SingleSelectDialog newInstance(String title, ArrayList<String> itemList, int selectedPosition) {
        SingleSelectDialog fragment = new SingleSelectDialog();
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("itemList", new Gson().toJson(itemList));
        args.putInt("selectedPosition", selectedPosition);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnDialogSubmitListener {
        void OnSingleSelectDialogSubmit(int selectedPosition);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the hosting fragment and ensure that it implements Listener.
        try {
            mListener = (OnDialogSubmitListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting Fragment must implement " +
                            "SingleSelectDialog.OnDialogSubmitListener");
        }
        // Get data from the bundle.
        title = getArguments().getString("title");
        String listJson = getArguments().getString("itemList");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        itemList = new Gson().fromJson(listJson, type);
        selectedPosition = getArguments().getInt("selectedPosition");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.single_select_dialog, null);

        //Display the selected list.
        RecyclerView rv = dialogView.findViewById(R.id.recyclerView);
        adapter = new Adapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Build dialog
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(dialogView);
        dialog.setTitle(title);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mListener.OnSingleSelectDialogSubmit(selectedPosition);
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

    public SingleSelectDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //------------------------------- Adapter -------------------------------//

    public class Adapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // Inflate the item view layout
            View itemView = inflater.inflate(R.layout.list_item_radio_textview, parent, false);
            return new Adapter.ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            String item = itemList.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            if (position == selectedPosition){
                viewHolder.radioBTN.setChecked(true);
            } else {
                viewHolder.radioBTN.setChecked(false);
            }
            viewHolder.nameTV.setText(item);
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            RadioButton radioBTN;
            TextView nameTV;

            ItemViewHolder(final View itemView) {
                super(itemView);
                radioBTN = itemView.findViewById(R.id.radioBTN);
                nameTV = itemView.findViewById(R.id.projectItemNameTV);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Set selected position.
                        selectedPosition = getAdapterPosition();
                        //Update adapter.
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
//Toast.makeText(getActivity(), "" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
