package com.jeffreyromero.materialestimator.data;

import android.content.Context;

import java.util.ArrayList;

public class ProjectItemTypes {

    public static ArrayList<String> getList(Context context) {
        ArrayList<String> l = new ArrayList<>();
        l.add("Gypsum Ceiling");
        l.add("Gypsum Partition");
        l.add("Suspended Ceiling");

        return l;
    }
}
