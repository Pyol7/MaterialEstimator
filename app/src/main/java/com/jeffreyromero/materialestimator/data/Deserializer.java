package com.jeffreyromero.materialestimator.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.ProjectItem;
import com.jeffreyromero.materialestimator.models.defaultProjectItems.DroppedCeiling;
import com.jeffreyromero.materialestimator.models.defaultProjectItems.DrywallCeiling;
import com.jeffreyromero.materialestimator.models.defaultProjectItems.DrywallPartition;
import com.jeffreyromero.materialestimator.models.droppedCeiling.CeilingTile;
import com.jeffreyromero.materialestimator.models.droppedCeiling.CrossTeeLong;
import com.jeffreyromero.materialestimator.models.droppedCeiling.CrossTeeShort;
import com.jeffreyromero.materialestimator.models.droppedCeiling.MainTee;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.FramingFastener;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.MainChannelFastener;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.PanelFastener;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.TrackFastener;
import com.jeffreyromero.materialestimator.models.drywall.materials.FurringChannel;
import com.jeffreyromero.materialestimator.models.drywall.materials.Hanger;
import com.jeffreyromero.materialestimator.models.drywall.materials.MainChannel;
import com.jeffreyromero.materialestimator.models.drywall.materials.JointCompound;
import com.jeffreyromero.materialestimator.models.drywall.fasteners.WallAngleFastener;
import com.jeffreyromero.materialestimator.models.drywall.materials.WallAngle;
import com.jeffreyromero.materialestimator.models.drywall.materials.Stud;
import com.jeffreyromero.materialestimator.models.drywall.materials.Track;
import com.jeffreyromero.materialestimator.models.drywall.materials.Panel;
import com.jeffreyromero.materialestimator.models.MaterialList;

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
        // BaseMaterial
        GsonRuntimeTypeAdapterFactory<BaseMaterial> BaseMaterialTypeAdapter = GsonRuntimeTypeAdapterFactory
                .of(BaseMaterial.class, "type")
                .registerSubtype(WallAngle.class, "Wall Angle")
                .registerSubtype(WallAngleFastener.class, "Wall Angle Fastener")
                .registerSubtype(Hanger.class, "Hanger")
                .registerSubtype(MainChannel.class, "Main Channel")
                .registerSubtype(MainChannelFastener.class, "Main Channel Fastener")
                .registerSubtype(FurringChannel.class, "Furring Channel")
                .registerSubtype(FramingFastener.class, "Framing Fastener")
                .registerSubtype(Stud.class, "Stud")
                .registerSubtype(Track.class, "Track")
                .registerSubtype(TrackFastener.class, "Track Fastener")
                .registerSubtype(Panel.class,"Panel")
                .registerSubtype(PanelFastener.class, "Panel Fastener")
                .registerSubtype(JointCompound.class, "Joint Compound")
                .registerSubtype(CeilingTile.class,"Ceiling Tile")
                .registerSubtype(MainTee.class,"Main Tee")
                .registerSubtype(CrossTeeLong.class,"Cross Tee Long")
                .registerSubtype(CrossTeeShort.class,"Cross Tee Short");

        // ProjectItem
        GsonRuntimeTypeAdapterFactory<ProjectItem> ProjectItemTypeAdapter = GsonRuntimeTypeAdapterFactory
                .of(ProjectItem.class, "type")
                .registerSubtype(DroppedCeiling.class, "Dropped Ceiling")
                .registerSubtype(DrywallCeiling.class, "Drywall Ceiling")
                .registerSubtype(DrywallPartition.class, "Drywall Partition");

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(BaseMaterialTypeAdapter)
                .registerTypeAdapterFactory(ProjectItemTypeAdapter)
                .create();
    }

    public static ArrayList<MaterialList> toArrayListOfMaterialList(String json) {
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

    public static ProjectItem toProjectItem(String json) {
        return gson.fromJson(json, ProjectItem.class);
    }

    public static ArrayList<ProjectItem> toProjectItems(String json) {
        Type type = new TypeToken<ArrayList<ProjectItem>>(){}.getType();
        return gson.fromJson(json, type);
    }

}
