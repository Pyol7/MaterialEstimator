package com.jeffreyromero.materialestimator.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.ItemTypes.DroppedCeiling;
import com.jeffreyromero.materialestimator.models.ItemTypes.DrywallCeiling;
import com.jeffreyromero.materialestimator.models.ItemTypes.DrywallPartition;
import com.jeffreyromero.materialestimator.models.quantifiables.CeilingTile;
import com.jeffreyromero.materialestimator.models.quantifiables.Tee;
import com.jeffreyromero.materialestimator.models.non_quantifiables.MainSupportFastener;
import com.jeffreyromero.materialestimator.models.non_quantifiables.PanelFastener;
import com.jeffreyromero.materialestimator.models.quantifiables.TrackFastener;
import com.jeffreyromero.materialestimator.models.quantifiables.FurringChannel;
import com.jeffreyromero.materialestimator.models.quantifiables.Hanger;
import com.jeffreyromero.materialestimator.models.quantifiables.MainSupport;
import com.jeffreyromero.materialestimator.models.quantifiables.JointCompound;
import com.jeffreyromero.materialestimator.models.quantifiables.WallAngleFastener;
import com.jeffreyromero.materialestimator.models.quantifiables.WallAngle;
import com.jeffreyromero.materialestimator.models.quantifiables.Stud;
import com.jeffreyromero.materialestimator.models.quantifiables.Track;
import com.jeffreyromero.materialestimator.models.quantifiables.Panel;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.floorFinishes.FloorFinish;
import com.jeffreyromero.materialestimator.models.floorFinishes.Tile;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Gson would serialize anything.
 * Deserialization requires a little more effort:
 * Array - the square brackets is needed. e.g. fromJson(json, array[].class).
 * IList - a Type is needed using a TypeToken. e.g. new TypeToken<ArrayList<Object>(){}.getType().
 * Object must not be a subclass.
 * IList<subtype> - Gson must know every subtype. This is achieved by registering the
 * GsonRuntimeTypeAdapterFactory, adding a "type" field in the superclass and have all subtypes
 * pass their class name as the type to the superclass.
 * To avoid this extra boilerplate assign the IList<Object> to a field inside of a class.
 */
public class Deserializer {
    private static Gson gson;

    static {
        // FloorFinish
        GsonRuntimeTypeAdapterFactory<FloorFinish> FloorFinishTypeAdapter = GsonRuntimeTypeAdapterFactory
                .of(FloorFinish.class, "subType")
                .registerSubtype(Tile.class);

        // BaseMaterial
        GsonRuntimeTypeAdapterFactory<BaseMaterial> BaseMaterialTypeAdapter = GsonRuntimeTypeAdapterFactory
                .of(BaseMaterial.class, "subType")
                .registerSubtype(WallAngle.class)
                .registerSubtype(WallAngleFastener.class)
                .registerSubtype(Hanger.class)
                .registerSubtype(MainSupport.class)
                .registerSubtype(MainSupportFastener.class)
                .registerSubtype(FurringChannel.class)
                .registerSubtype(Stud.class)
                .registerSubtype(Track.class)
                .registerSubtype(TrackFastener.class)
                .registerSubtype(Panel.class)
                .registerSubtype(PanelFastener.class)
                .registerSubtype(JointCompound.class)
                .registerSubtype(CeilingTile.class)
                .registerSubtype(Tee.class);

        // BaseItem
        GsonRuntimeTypeAdapterFactory<BaseItem> itemTypeAdapter = GsonRuntimeTypeAdapterFactory
                .of(BaseItem.class, "subType")
                .registerSubtype(DroppedCeiling.class, "Dropped Ceiling")
                .registerSubtype(DrywallCeiling.class, "Drywall Ceiling")
                .registerSubtype(DrywallPartition.class, "Drywall Partition");

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(BaseMaterialTypeAdapter)
                .registerTypeAdapterFactory(itemTypeAdapter)
                .create();
    }

    public static ArrayList<MaterialList> toMaterialLists(String json) {
        Type type = new TypeToken<ArrayList<MaterialList>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static MaterialList toMaterialList(String json) {
        return gson.fromJson(json, MaterialList.class);
    }

    public static BaseMaterial toMaterial(String json) {
        return gson.fromJson(json, BaseMaterial.class);
    }

    public static ArrayList<Project> toProjects(String json) {
        Type type = new TypeToken<ArrayList<Project>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static Project toProject(String json) {
        return gson.fromJson(json, Project.class);
    }

    public static BaseItem toItemType(String json) {
        return gson.fromJson(json, BaseItem.class);
    }

    public static ArrayList<BaseItem> toItemTypes(String json) {
        Type type = new TypeToken<ArrayList<BaseItem>>(){}.getType();
        return gson.fromJson(json, type);
    }

}
