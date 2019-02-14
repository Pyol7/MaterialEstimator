package com.jeffreyromero.materialestimator.models.ItemTypes;

import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.Quantifiable;
import com.jeffreyromero.materialestimator.models.quantifiables.CeilingTile;
import com.jeffreyromero.materialestimator.models.quantifiables.Tee;
import com.jeffreyromero.materialestimator.models.quantifiables.WallAngleFastener;
import com.jeffreyromero.materialestimator.models.quantifiables.Hanger;
import com.jeffreyromero.materialestimator.models.quantifiables.WallAngle;

import java.util.ArrayList;
import java.util.List;

public class DroppedCeiling extends BaseItem {

    public DroppedCeiling(String name) {
        super("Dropped Ceiling", name);
        setMaterialList(initMaterialList());
    }

    @Override
    public MaterialList initMaterialList() {
        List<BaseMaterial> list = new ArrayList<>();
        list.add(new CeilingTile("Ceiling Tiles 24x24", 11.00, 24, 24));
        list.add(new Tee("Main Tees", 22.53, 144));
        list.add(new Tee("4ft Cross Tees", 7.30, 48));
        list.add(new Tee("2ft Cross Tees", 4.04, 24));
        list.add(new WallAngle("Dropped Ceiling Wall Angles", 15.25, 120));
        list.add(new WallAngleFastener("Nails 3/4", 0.16, 12));
        list.add(new Hanger("Hanger 14g", 8.50, 48));
        return new MaterialList(list, buildMaterialListNameFromClassName());
    }

    @Override
    public void calcQuantities() {
        // Analyse dimensions
        double length = Math.max(getLength(), getWidth());
        double width = Math.min(getLength(), getWidth());
        // Calculate material
        List<BaseMaterial> tempList = new ArrayList<>();
        MaterialList ml = getMaterialList();
        for (BaseMaterial bm : getMaterialList().getList()) {
            ((Quantifiable)bm).calcQuantity(length, width);
            tempList.add(bm);
        }
        // Update materialList
        ml.setList(tempList);
    }
}
