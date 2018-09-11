package com.jeffreyromero.materialestimator.data;

import android.content.Context;

import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.models.drywall.FramingScrew;
import com.jeffreyromero.materialestimator.models.drywall.Panel;
import com.jeffreyromero.materialestimator.models.drywall.CChannel;
import com.jeffreyromero.materialestimator.models.drywall.FurringChannel;
import com.jeffreyromero.materialestimator.models.drywall.JointCompoundAllPurpose;
import com.jeffreyromero.materialestimator.models.drywall.DrywallScrew;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.drywall.Stud;
import com.jeffreyromero.materialestimator.models.drywall.Track;
import com.jeffreyromero.materialestimator.models.drywall.WallAngle;

import java.util.ArrayList;

public class DefaultMaterialList {

    public static MaterialList getList(Context context) {
        ArrayList<Material> l = new ArrayList<>();
        l.add(new Panel("Ultra Light Boards", 74.00, 96, 48));
        l.add(new FurringChannel("Furring Channels", 20.00, 144));
        l.add(new CChannel("C Channels", 51.50, 192));
        l.add(new WallAngle("Metal Wall Angles", 15.25, 120));
        l.add(new Stud("Metal Studs", 15.40, 2.5, 108));
        l.add(new Track("Metal Tracks", 19.50, 2.5, 120));
        l.add(new DrywallScrew("Drywall Screws", 0.20));
        l.add(new FramingScrew("Framing Screws", 0.14));
        l.add(new JointCompoundAllPurpose("Joint Compound All Purpose", 130.00));

        return new MaterialList(l, context.getString(R.string.default_material_list));
    }
}
