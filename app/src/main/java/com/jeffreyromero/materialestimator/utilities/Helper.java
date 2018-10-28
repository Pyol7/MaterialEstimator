package com.jeffreyromero.materialestimator.utilities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jeffreyromero.materialestimator.R;

public class Helper {

    public static String addFragment(AppCompatActivity activity, Fragment f, Boolean addToBackStack){
        String tag = f.getTag();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, f, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
        return tag;
    }

    public static String replaceFragment(AppCompatActivity activity, Fragment f, Boolean addToBackStack){
        String tag = f.getTag();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
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
