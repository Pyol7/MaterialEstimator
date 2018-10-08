package com.jeffreyromero.materialestimator.models.defaultProjectItems;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.ProjectItem;
import com.jeffreyromero.materialestimator.models.QuantifiableProjectItem;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.FramingFastener;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.MainChannelFastener;
import com.jeffreyromero.materialestimator.models.drywall.materials.FurringChannel;
import com.jeffreyromero.materialestimator.models.drywall.materials.Hanger;
import com.jeffreyromero.materialestimator.models.drywall.materials.MainChannel;
import com.jeffreyromero.materialestimator.models.drywall.materials.JointCompound;
import com.jeffreyromero.materialestimator.models.drywall.materials.Panel;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.PanelFastener;
import com.jeffreyromero.materialestimator.models.drywall.materials.WallAngle;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.WallAngleFastener;

import java.util.ArrayList;
import java.util.List;

public class DrywallCeiling extends ProjectItem {

    public DrywallCeiling(String name) {
        super("Drywall Ceiling", name);
        setMaterialList(initMaterialList());
    }

    private MaterialList initMaterialList() {
        List<BaseMaterial> list = new ArrayList<>();
        list.add(new WallAngle("Metal Wall Angles", 15.25, 120));
        list.add(new WallAngleFastener("Nails 3/4", 0.16,12));
        list.add(new Hanger("Wire 14g",8.50, 48));
        list.add(new MainChannel("C Channels", 51.50, 192,48));
        list.add(new MainChannelFastener("Tec Point Screws", 0.11));
        list.add(new FurringChannel("Furring Channels",20.00,144,16));
        list.add(new FramingFastener("Framing Screws", 0.14));
        list.add(new Panel("Ultra Light Boards", 74.00, 96, 48, 8));
        list.add(new PanelFastener("Drywall Screws", 0.20));
        list.add(new JointCompound("Joint Compound All Purpose", 130.00,50400));
        return new MaterialList(list, generateNameFromClassName());
    }

    public void calcQuantities(double length, double width){
        setLength(length);
        setWidth(width);
        List<BaseMaterial> tempList = new ArrayList<>();
        MaterialList ml = getMaterialList();
        for (BaseMaterial bm : ml.getList()){
            // Handle materials that do not implement Quantifiable
            if (bm instanceof PanelFastener){
                PanelFastener panelFastener = (PanelFastener) bm;
                panelFastener.calcFastenerQuantity(
                        length,
                        width,
                        ml.get(Panel.class),
                        ml.get(FurringChannel.class)
                );
                tempList.add(panelFastener);

            } else if (bm instanceof MainChannelFastener) {
                MainChannelFastener mainChannelFastener = (MainChannelFastener) bm;
                mainChannelFastener.calcFastenerQuantity(
                        length,
                        width,
                        ml.get(MainChannel.class),
                        ml.get(FurringChannel.class)
                );
                tempList.add(mainChannelFastener);

            } else if (bm instanceof FramingFastener) {
                FramingFastener framingFastener = (FramingFastener) bm;
                framingFastener.calcFastenerQuantity(
                        length,
                        width,
                        ml.get(FurringChannel.class)
                );
                tempList.add(framingFastener);

            } else {
                // Handle materials that implements Quantifiable
                ((Material)bm).calcQuantity(length, width);
                tempList.add(bm);
            }
        }
        // Update materialList
        ml.setList(tempList);
    }
}
