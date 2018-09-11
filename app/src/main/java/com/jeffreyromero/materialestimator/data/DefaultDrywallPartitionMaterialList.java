package com.jeffreyromero.materialestimator.data;

import android.content.Context;

import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.models.drywall.FramingScrew;
import com.jeffreyromero.materialestimator.models.drywall.Panel;
import com.jeffreyromero.materialestimator.models.drywall.JointCompoundAllPurpose;
import com.jeffreyromero.materialestimator.models.drywall.DrywallScrew;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.drywall.Stud;
import com.jeffreyromero.materialestimator.models.drywall.Track;

import java.util.ArrayList;

public class DefaultDrywallPartitionMaterialList {

    public static MaterialList getList(Context context) {
        ArrayList<Material> l = new ArrayList<>();
        l.add(new Panel("Regular Boards", 7400, 96, 48));
        l.add(new Stud("Metal Studs", 1540, 2.5, 108));
        l.add(new Track("Metal Tracks", 1950, 2.5, 120));
        l.add(new DrywallScrew("Drywall Screws", 20));
        l.add(new FramingScrew("Framing Screws", 14));
        l.add(new JointCompoundAllPurpose("Joint Compound All Purpose", 13000));

        return new MaterialList(l, context.getString(R.string.default_drywall_partition_material_list));
    }
}
