package com.jeffreyromero.materialestimator.utilities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class Helper {

    public static String addFragment(AppCompatActivity activity, Fragment f, int container, Boolean addToBackStack){
        String tag = f.getClass().getCanonicalName();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(container, f, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
        return tag;
    }

    public static String addFragment(AppCompatActivity activity, Fragment f, Fragment target, int container, Boolean addToBackStack){
        String tag = f.getClass().getCanonicalName();
        f.setTargetFragment(target, 0);
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(container, f, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
        return tag;
    }

    public static String replaceFragment(AppCompatActivity activity, Fragment f, int container, Boolean addToBackStack){
        String tag = f.getClass().getCanonicalName();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(container, f, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
        return tag;
    }

    public static String replaceFragment(AppCompatActivity activity, Fragment f, Fragment target, int container, Boolean addToBackStack){
        String tag = f.getClass().getCanonicalName();
        f.setTargetFragment(target, 0);
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(container, f, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
        return tag;
    }

    public static void clearBackStack(FragmentManager fragmentManager){
        if (fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack(
                    fragmentManager.getBackStackEntryAt(0).getName(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
            );
        }
    }
}
