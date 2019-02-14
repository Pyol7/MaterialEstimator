package com.jeffreyromero.materialestimator.models.ItemTypes;

import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.Quantifiable;
import com.jeffreyromero.materialestimator.models.non_quantifiables.MainSupportFastener;
import com.jeffreyromero.materialestimator.models.quantifiables.FurringChannel;
import com.jeffreyromero.materialestimator.models.quantifiables.Hanger;
import com.jeffreyromero.materialestimator.models.quantifiables.MainSupport;
import com.jeffreyromero.materialestimator.models.quantifiables.JointCompound;
import com.jeffreyromero.materialestimator.models.quantifiables.Panel;
import com.jeffreyromero.materialestimator.models.non_quantifiables.PanelFastener;
import com.jeffreyromero.materialestimator.models.quantifiables.WallAngle;
import com.jeffreyromero.materialestimator.models.quantifiables.WallAngleFastener;

import java.util.ArrayList;
import java.util.List;

public class DrywallCeiling extends BaseItem {

    private int layersOfBoards;

    public DrywallCeiling(String name) {
        super("Drywall Ceiling", name);
        setMaterialList(initMaterialList());
    }

    @Override
    public MaterialList initMaterialList() {
        List<BaseMaterial> list = new ArrayList<>();
        list.add(new WallAngle("Metal Wall Angles", 15.25, 120));
        list.add(new WallAngleFastener("Nails 3/4", 0.16,12));
        list.add(new Hanger("Wire 14g",8.50, 48));
        list.add(new MainSupport("C Channels", 51.50, 192,48, 12));
        list.add(new MainSupportFastener("Tec Point Screws", 0.11));
        list.add(new FurringChannel("Furring Channels",20.00,144, 16, 12));
        list.add(new Panel("Ultra Light Boards", 74.00, 96, 48));
        list.add(new PanelFastener("Drywall Screws", 0.20, 16));
        list.add(new JointCompound("Joint Compound All Purpose", 130.00,50400));
        return new MaterialList(list, buildMaterialListNameFromClassName());
    }

    @Override
    public void calcQuantities(){
        // Analyse dimensions
        double length = Math.max(getLength(), getWidth());
        double width = Math.min(getLength(), getWidth());
        setLength(length);
        setWidth(width);
        // Calculate quantities
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

            } else if (bm instanceof MainSupportFastener) {
                MainSupportFastener mainSupportFastener = (MainSupportFastener) bm;
                mainSupportFastener.calcFastenerQuantity(
                        length,
                        width,
                        ml.get(MainSupport.class),
                        ml.get(FurringChannel.class)
                );
                tempList.add(mainSupportFastener);
            } else {
                // Handle materials that implements Quantifiable
                ((Quantifiable)bm).calcQuantity(length, width);
                tempList.add(bm);
            }
        }
        // Update materialList
        ml.setList(tempList);
    }

    public int getLayersOfBoards() {
        return layersOfBoards;
    }

    public void setLayersOfBoards(int layersOfBoards) {
        this.layersOfBoards = layersOfBoards;
    }
}
