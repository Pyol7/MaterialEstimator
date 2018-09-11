package com.jeffreyromero.materialestimator.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.ProjectItem;
import com.jeffreyromero.materialestimator.models.droppedCeiling.CeilingTile;
import com.jeffreyromero.materialestimator.models.droppedCeiling.CrossTeeLong;
import com.jeffreyromero.materialestimator.models.droppedCeiling.CrossTeeShort;
import com.jeffreyromero.materialestimator.models.droppedCeiling.MainTee;
import com.jeffreyromero.materialestimator.models.drywall.CChannel;
import com.jeffreyromero.materialestimator.models.drywall.DrywallScrew;
import com.jeffreyromero.materialestimator.models.drywall.FramingScrew;
import com.jeffreyromero.materialestimator.models.drywall.FurringChannel;
import com.jeffreyromero.materialestimator.models.drywall.WallAngle;
import com.jeffreyromero.materialestimator.models.drywall.Stud;
import com.jeffreyromero.materialestimator.models.drywall.Track;
import com.jeffreyromero.materialestimator.models.drywall.JointCompoundAllPurpose;
import com.jeffreyromero.materialestimator.models.drywall.JointCompoundSettingType;
import com.jeffreyromero.materialestimator.models.drywall.Panel;
import com.jeffreyromero.materialestimator.models.Material;
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
        GsonRuntimeTypeAdapterFactory<Material> MaterialTypeAdapter = GsonRuntimeTypeAdapterFactory
                .of(Material.class, "type")
                .registerSubtype(Panel.class,"Panel")

                .registerSubtype(CChannel.class, "CChannel")
                .registerSubtype(FurringChannel.class, "FurringChannel")
                .registerSubtype(WallAngle.class, "WallAngle")

                .registerSubtype(Stud.class, "Stud")
                .registerSubtype(Track.class, "Track")

                .registerSubtype(DrywallScrew.class, "DrywallScrew")
                .registerSubtype(FramingScrew.class, "FramingScrew")

                .registerSubtype(JointCompoundAllPurpose.class, "JointCompoundAllPurpose")
                .registerSubtype(JointCompoundSettingType.class, "JointCompoundSettingType")

                .registerSubtype(CeilingTile.class,"CeilingTile")
                .registerSubtype(MainTee.class,"MainTee")
                .registerSubtype(CrossTeeLong.class,"CrossTeeLong")
                .registerSubtype(CrossTeeShort.class,"CrossTeeShort");

        gson = new GsonBuilder().registerTypeAdapterFactory(MaterialTypeAdapter).create();
    }

    public static ArrayList<MaterialList> toArrayListOfMaterialList(String json) {
        Type type = new TypeToken<ArrayList<MaterialList>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static MaterialList toMaterialList(String json) {
        return gson.fromJson(json, MaterialList.class);
    }

    public static Material toMaterial(String json) {
        return gson.fromJson(json, Material.class);
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

}
