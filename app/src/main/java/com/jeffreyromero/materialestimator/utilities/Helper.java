package com.jeffreyromero.materialestimator.utilities;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jeffreyromero.materialestimator.R;

public class Helper {

    public static String addFragment(AppCompatActivity activity, Fragment f, Boolean addToBackStack){
        String tag = f.getClass().getSimpleName();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, f, tag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        return tag;
    }

    public static String replaceFragment(AppCompatActivity activity, Fragment f, Boolean addToBackStack){
        String tag = f.getClass().getSimpleName();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f, tag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        return tag;
    }
}
