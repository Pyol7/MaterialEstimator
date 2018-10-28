package com.jeffreyromero.materialestimator.project;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.MainActivity;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.BaseMaterial;

/**
 * Displays a BaseItem including it's list of material.
 */
public class ItemFragment extends Fragment {

    private static final String PROJECT_ITEM = "baseItem";
    private BaseItem baseItem;


    public ItemFragment() {
        // Required empty public constructor
    }

    public static ItemFragment newInstance(BaseItem baseItem) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        String json = new Gson().toJson(baseItem);
        args.putString(PROJECT_ITEM, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set all fields.
        if (getArguments() != null) {
            String json = getArguments().getString(PROJECT_ITEM);
            baseItem = Deserializer.toItem(json);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.item_fragment, container, false);

        // Enable up navigation for this fragment
//        ((MainActivity)getActivity()).enableUpNavigation();

        //Set a title to the toolbar.
        getActivity().setTitle(baseItem.getName());

        //Get views and set values.
        TextView nameTV = view.findViewById(R.id.toolbarTitle);
        TextView lengthTV = view.findViewById(R.id.unitPriceTV);
        TextView widthTV = view.findViewById(R.id.widthET);
        nameTV.setText(baseItem.getName());
        lengthTV.setText(String.valueOf(baseItem.getLength()));
        widthTV.setText(String.valueOf(baseItem.getWidth()));

        //Setup recyclerView
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        // Instantiate the adaptor
        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return baseItem.getMaterialList().size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // Inflate the baseItem view layout
            View itemView = inflater.inflate(R.layout.list_item_textview_textview, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            BaseMaterial material = baseItem.getMaterialList().get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.columnLeftTV.setText(material.getName());
            viewHolder.columnRightTV.setText(String.valueOf(material.getQuantity()));
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView columnLeftTV;
            TextView columnRightTV;

            ItemViewHolder(final View itemView) {
                super(itemView);
                columnLeftTV = itemView.findViewById(R.id.columnLeftTV);
                columnRightTV = itemView.findViewById(R.id.columnRightTV);
            }
        }
    }

}
