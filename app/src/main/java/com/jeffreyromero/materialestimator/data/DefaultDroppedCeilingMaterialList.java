package com.jeffreyromero.materialestimator.data;

import android.content.Context;

import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.drywall.WallAngle;
import com.jeffreyromero.materialestimator.models.droppedCeiling.CeilingTile;
import com.jeffreyromero.materialestimator.models.droppedCeiling.CrossTeeLong;
import com.jeffreyromero.materialestimator.models.droppedCeiling.CrossTeeShort;
import com.jeffreyromero.materialestimator.models.droppedCeiling.MainTee;

import java.util.ArrayList;

public class DefaultDroppedCeilingMaterialList {

    public static MaterialList getList(Context context) {
        ArrayList<Material> l = new ArrayList<>();
        l.add(new CeilingTile("Ceiling Tiles 24x24", 1100, 24, 24));
        l.add(new MainTee("Main Tees", 2253, 144));
        l.add(new CrossTeeLong("4ft Cross Tees", 730, 48));
        l.add(new CrossTeeShort("2ft Cross Tees", 404, 24));
        l.add(new WallAngle("Wall Angles", 1374, 120));

        return new MaterialList(l, context.getString(R.string.default_dropped_ceiling_material_list));
    }
}
