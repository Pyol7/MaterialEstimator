package com.jeffreyromero.materialestimator.models.ItemTypes;

import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.Quantifiable;
import com.jeffreyromero.materialestimator.models.non_quantifiables.PanelFastener;
import com.jeffreyromero.materialestimator.models.quantifiables.TrackFastener;
import com.jeffreyromero.materialestimator.models.quantifiables.JointCompound;
import com.jeffreyromero.materialestimator.models.quantifiables.Panel;
import com.jeffreyromero.materialestimator.models.quantifiables.Stud;
import com.jeffreyromero.materialestimator.models.quantifiables.Track;

import java.util.ArrayList;
import java.util.List;

public class DrywallPartition extends BaseItem {

    private int layersOfBoards;
    private double totalOpeningArea;

    public DrywallPartition(String name) {
        super("Drywall Partition", name);
        setMaterialList(initMaterialList());
    }

    @Override
    public MaterialList initMaterialList() {
        List<BaseMaterial> list = new ArrayList<>();
        list.add(new Track("Metal Tracks",19.50,120));
        list.add(new TrackFastener("Concrete nails for floor tracks",0.11,16));
        list.add(new TrackFastener("Hit-it Fasteners for ceiling tracks",1.50,24));
        list.add(new Stud("Metal Studs",15.40,108,24));
        list.add(new Panel("Regular Boards",74.00,96,48));
        list.add(new PanelFastener("Drywall Screws for Panels",0.20, 16));
//        list.add(new FramingFastener("Framing Screws for studs",0.14));
        list.add(new JointCompound("Joint Compound All Purpose",130.00,50400));
        return new MaterialList(list, buildMaterialListNameFromClassName());
    }

    @Override
    public void calcQuantities(){
        // Analyse dimensions
        double length = getLength();
        double height = getHeight();
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

//            } else if (bm instanceof FramingFastener) {
//                FramingFastener framingFastener = (FramingFastener) bm;
//                framingFastener.calcFastenerQuantity(
//                        length,
//                        height,
//                        ml.get(Stud.class)
//                );
//                tempList.add(framingFastener);

            } else {
                // Handle materials that implements Quantifiable
                ((Quantifiable)bm).calcQuantity(length, height);
                tempList.add(bm);
            }
        }
        // Update materialList
        ml.setList(tempList);
    }

    public int getLayersOfBoards() {
        return layersOfBoards;
    }

    public void setLayersOfBoards(int layerOfBoards) {
        this.layersOfBoards = layerOfBoards;
    }

    public double getTotalOpeningArea() {
        return totalOpeningArea;
    }

    public void setTotalOpeningArea(double totalOpeningArea) {
        this.totalOpeningArea = totalOpeningArea;
    }
}
