package com.jeffreyromero.materialestimator.models.defaultProjectItems;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.ProjectItem;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.FramingFastener;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.MainChannelFastener;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.PanelFastener;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.TrackFastener;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.WallAngleFastener;
import com.jeffreyromero.materialestimator.models.drywall.materials.FurringChannel;
import com.jeffreyromero.materialestimator.models.drywall.materials.Hanger;
import com.jeffreyromero.materialestimator.models.drywall.materials.JointCompound;
import com.jeffreyromero.materialestimator.models.drywall.materials.MainChannel;
import com.jeffreyromero.materialestimator.models.drywall.materials.Panel;
import com.jeffreyromero.materialestimator.models.drywall.materials.Stud;
import com.jeffreyromero.materialestimator.models.drywall.materials.Track;
import com.jeffreyromero.materialestimator.models.drywall.materials.WallAngle;

import java.util.ArrayList;
import java.util.List;

public class DrywallPartition extends ProjectItem {

    public DrywallPartition(String name) {
        super("Drywall Partition", name);
        setMaterialList(initMaterialList());
    }

    private MaterialList initMaterialList() {
        List<BaseMaterial> list = new ArrayList<>();
        list.add(new Track("Metal Tracks",19.50,120));
        list.add(new TrackFastener("Concrete nails for floor tracks",0.11,16));
        list.add(new TrackFastener("Hit-it Fasteners for ceiling tracks",1.50,24));
        list.add(new Stud("Metal Studs",15.40,108,24));
        list.add(new Panel("Regular Boards",74.00,96,48,16));
        list.add(new PanelFastener("Drywall Screws for Panels",0.20));
        list.add(new FramingFastener("Framing Screws for studs",0.14));
        list.add(new JointCompound("Joint Compound All Purpose",130.00,50400));
        return new MaterialList(list, generateNameFromClassName());
    }

    public void calcQuantities(double length, double height){
        setLength(length);
        setWidth(height);
        List<BaseMaterial> tempList = new ArrayList<>();
        MaterialList ml = getMaterialList();
        for (BaseMaterial bm : ml.getList()){
            // Handle materials that do not implement Quantifiable
            if (bm instanceof PanelFastener){
                PanelFastener panelFastener = (PanelFastener) bm;
                panelFastener.calcFastenerQuantity(
                        length,
                        height,
                        ml.get(Panel.class),
                        ml.get(Stud.class)
                );
                tempList.add(panelFastener);

            } else if (bm instanceof FramingFastener) {
                FramingFastener framingFastener = (FramingFastener) bm;
                framingFastener.calcFastenerQuantity(
                        length,
                        height,
                        ml.get(Stud.class)
                );
                tempList.add(framingFastener);

            } else {
                // Handle materials that implements Quantifiable
                ((Material)bm).calcQuantity(length, height);
                tempList.add(bm);
            }
        }
        // Update materialList
        ml.setList(tempList);
    }
}
