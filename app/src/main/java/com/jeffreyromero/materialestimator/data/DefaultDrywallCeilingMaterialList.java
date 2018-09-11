package com.jeffreyromero.materialestimator.data;

import android.content.Context;

import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.models.drywall.Panel;
import com.jeffreyromero.materialestimator.models.drywall.CChannel;
import com.jeffreyromero.materialestimator.models.drywall.FurringChannel;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.drywall.WallAngle;

import java.util.ArrayList;

public class DefaultDrywallCeilingMaterialList {

    public static MaterialList getList(Context context) {
        ArrayList<Material> l = new ArrayList<>();
        l.add(new Panel("Ultra Light Boards", 7400, 96, 48));
        l.add(new FurringChannel("Furring Channels", 2000, 144));
        l.add(new CChannel("C Channels", 5150, 192));
        l.add(new WallAngle("Metal Wall Angles", 1525, 120));

        return new MaterialList(l, context.getString(R.string.default_drywall_ceiling_material_list));
    }
}
