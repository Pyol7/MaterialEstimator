package com.jeffreyromero.materialestimator.utilities.dialogCreateNewItem;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.Item.EditItemTypeFragment;
import com.jeffreyromero.materialestimator.Item.EditQuantityDialog;
import com.jeffreyromero.materialestimator.MainActivity;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.projectFragments.ItemCreatorFragmentHelpDialog;
import com.jeffreyromero.materialestimator.utilities.Helper;
import com.jeffreyromero.materialestimator.utilities.SoftKeyboardVisibilityListener;

import java.util.Locale;

/**
 * This is the third and final step in creating a new item.
 * The created item is sent back to Project to be added
 * to it's list of items.
 */
public class DialogCreateItem extends Fragment implements
        EditQuantityDialog.OnDialogSubmitListener {

    private static final String SELECTED_ITEM_TYPE = "selected item type";
    private static final double FEET_TO_INCHES = 12;
    private MaterialListAdapter materialListAdapter;
    private MainActivity context;
    private BaseItem preparedItem;
    private View view;
    private OnFragmentInteractionListener mListener;

    private SoftKeyboardVisibilityListener keyboardListener;
    private RecyclerView recyclerView;

    public DialogCreateItem() {
        // Required empty public constructor
    }

    public static DialogCreateItem newInstance(BaseItem preparedItem) {
        DialogCreateItem fragment = new DialogCreateItem();
        Bundle args = new Bundle();
        args.putString(SELECTED_ITEM_TYPE, new Gson().toJson(preparedItem));
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void onDialogCreateItemSaveBtnClick(BaseItem newItem);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Enforce interface methods
        try {
            mListener = (DialogCreateItem.OnFragmentInteractionListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The host must implement this OnDialogSubmitListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use MainActivity as context
        context = (MainActivity) getActivity();

        // Get preparedItem from bundle
        if (getArguments() != null) {
            String json = getArguments().getString(SELECTED_ITEM_TYPE);
            preparedItem = Deserializer.toItemType(json);
        }

        // Instantiate the materialListAdapter.
        materialListAdapter = new MaterialListAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_create_item, container,false);

        // Show options menu.
        setHasOptionsMenu(true);

        // Get name and type
        TextView nameTV = view.findViewById(R.id.name_tv);
        nameTV.setText(preparedItem.getName());
        TextView typeTV = view.findViewById(R.id.type_tv);
        typeTV.setText(preparedItem.getSubType());

//        // Get the toolbar and set it as the action bar in MainActivity.
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        context.setSupportActionBar(toolbar);
//
//        // Set preparedItem name as toolbar title.
//        TextView title = toolbar.findViewById(R.id.toolbarTitle);
//        title.setText(preparedItem.getName());
//
//        // Set the current preparedItem type as toolbar subtitle.
//        TextView subtitle = toolbar.findViewById(R.id.toolbarSubtitle);
//        subtitle.setText(preparedItem.getMaterialList().getName());
//
//
//        // Enable up navigation for this fragment
//        context.enableDrawerNavigation(false);

        // Display the current preparedItem's material list
        recyclerView = view.findViewById(R.id.create_item_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(materialListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible preparedItem range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
//                    speedDialView.show();
                } else if (dy > 0) {
//                    speedDialView.hide();
                }
            }
        });

        calculateQuantities();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.item_creator_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                sendNewItemToListener();
                return true;
            case R.id.action_material_settings:
                editItemTypeMaterialSettings();
                return true;
            case R.id.action_help:
                showItemCreatorFragmentHelpDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendNewItemToListener() {
        mListener.onDialogCreateItemSaveBtnClick(preparedItem);
        //Clear the back stack up to Project.
        getFragmentManager().popBackStackImmediate("com.jeffreyromero.materialestimator.project.Project", 0);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SELECTED_ITEM_TYPE, new Gson().toJson(preparedItem));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //---------------------------- Callbacks --------------------------

    private void showInputGroup(final int duration) {
//        if (recyclerView.computeVerticalScrollRange() > recyclerView.getHeight()){
//            recyclerView.setPadding(0, 0, 0, inputGroup.getHeight());
//        }
//        inputGroup.animate()
//                .translationY(0)
//                .setDuration(duration);
    }

    private void hideInputGroup(final int delay, final int duration) {
        // Hide only if recycleView is scrollable and totalPrice > 0
//        if (recyclerView.computeVerticalScrollRange() > recyclerView.getHeight() && preparedItem.getTotalPrice() > 0) {
//            recyclerView.setPadding(0, 0, 0, 0);
//            inputGroup.animate()
//                    .translationY(inputGroup.getHeight())
//                    .setStartDelay(delay)
//                    .setDuration(duration);
//        }
    }

    // Edit quantity
    private void showEditQuantityDialog(int position) {
        EditQuantityDialog f = EditQuantityDialog.newInstance(
                position,
                preparedItem
        );
        f.setTargetFragment(DialogCreateItem.this, 0);
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    @Override
    public void OnEditQuantityDialogSubmit(int position, double userInput) {
        // Get material at position and update quantity
        BaseMaterial m = preparedItem.getMaterialList().get(position);
        m.setQuantity(userInput);
        materialListAdapter.notifyItemChanged(position);
    }

    // Item type material settings
    private void editItemTypeMaterialSettings() {
        Helper.replaceFragment(
                (AppCompatActivity) getActivity(),
                EditItemTypeFragment.newInstance(preparedItem),
                R.id.fragment_container,
                true
        );

        materialListAdapter.notifyDataSetChanged();
    }

    // Calculate quantities
    private void calculateQuantities() {

        // reload the materialList to catch any property changes
//        preparedItem.setMaterialList(itemsSP.get(selectedItemTypePosition).getMaterialList());
        preparedItem.calcQuantities();

        // Update materialListAdapter
        materialListAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(materialListAdapter.getItemCount()-1);

        // Close keyboard
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private boolean materialListHasQuantities(){
        MaterialList m = preparedItem.getMaterialList();
        for (int i = 0; i < m.size(); i++) {
            if (m.get(i).getQuantity() != 0){
                return true;
            }
        }
        return false;
    }

    private void showItemCreatorFragmentHelpDialog() {
        ItemCreatorFragmentHelpDialog f = ItemCreatorFragmentHelpDialog.newInstance();
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    //------------------------------- Classes -------------------------------//

    private class MaterialListAdapter extends RecyclerView.Adapter {
        //View types
        private final int ITEM_VIEW = 0;
        private final int TOTAL_VIEW = 1;

        @Override
        public int getItemCount() {
            return preparedItem.getMaterialList() == null ? 0 :
                    preparedItem.getMaterialList().size() + 1;
        }

        //Determine which layout to use for the row.
        @Override
        public int getItemViewType(int position) {
            if (position < getItemCount() - 1) {
                return ITEM_VIEW;
            } else {
                return TOTAL_VIEW;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == ITEM_VIEW) {
                // Inflate the ITEM_VIEW.
                return new ItemVH(LayoutInflater.from(context).inflate(
                        R.layout.dialog_create_item_list_item, parent, false));

            } else if (viewType == TOTAL_VIEW) {
                // Inflate the TOTAL_VIEW.
                return new TotalVH(LayoutInflater.from(context).inflate(
                        R.layout.total_list_item, parent, false));

            } else {
                //Throw exception if view type is not found.
                throw new RuntimeException("View type not found");
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {

                case ITEM_VIEW:
                    BaseMaterial material = preparedItem.getMaterialList().get(position);
                    // Set background color
                    ItemVH itemVH = (ItemVH) holder;
                    if (position % 2 == 0) {
                        itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    } else {
                        itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                    }
                    // Set values
                    itemVH.nameTV.setText(material.toString());
                    String q = String.format(
                            Locale.US,
                            "%.1f",
                            material.getQuantity());
                    itemVH.quantityTV.setText(q);
                    String u = String.format(
                            Locale.US,
                            "$%.2f",
                            material.getUnitPrice());
                    itemVH.unitPriceTV.setText(u);
                    itemVH.priceTV.setText(String.format(
                            Locale.US,
                            "$%.2f",
                            material.getPrice()));
                    break;

                case TOTAL_VIEW:
                    TotalVH totalVH = (TotalVH) holder;
                    totalVH.totalTV.setText(String.format(
                            Locale.US,
                            "$%.2f",
                            preparedItem.calcTotalPrice()));
                    break;

                default:
                    break;
            }
        }

        private class ItemVH extends RecyclerView.ViewHolder {
            TextView nameTV;
            TextView quantityTV;
            TextView unitPriceTV;
            TextView priceTV;

            ItemVH(View itemView) {
                super(itemView);
                nameTV = itemView.findViewById(R.id.toolbarTitle);
                quantityTV = itemView.findViewById(R.id.quantityTV);
                unitPriceTV = itemView.findViewById(R.id.unitPriceTV);
                priceTV = itemView.findViewById(R.id.priceTV);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditQuantityDialog(getAdapterPosition());
                    }
                });
            }
        }

        private class TotalVH extends RecyclerView.ViewHolder {
            TextView totalTV;

            TotalVH(View itemView) {
                super(itemView);
                totalTV = itemView.findViewById(R.id.totalTV);
            }
        }
    }
}
//Toast.makeText(context, "" + materialList + " " + position, Toast.LENGTH_SHORT).show();
