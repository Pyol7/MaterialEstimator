package com.jeffreyromero.materialestimator.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.jeffreyromero.materialestimator.R;

public class Settings {
    private static double feetToInches = 12;
    private static double feet2ToInch2 = 144;
    private static double inchesToFeet = 0.0833;
    private static double inch2ToFeet2 = 0.0069;
    private static double wastage = 5;    //%
    private static double screwsPerPanel = 28;
    private static double furringChannelSpacing = 16;
    private static double furringLapLength = 12;
    private static double cChannelSpacing = 48;
    private static double cChannelLapLength = 16;
    private static double nailSpacing = 16;
    private static double JointCompoundSettingTypeCoverage = 21600;   //sq.in (150sq.ft)
    private static double JointCompoundAllPurposeCoverage = 57600;    //sq.in (400sq.ft)
    private static double studSpacing = 24;
    private static double mainTeeSpacing = 48;
    private static double CrossTeeLongSpacing = 24;
    private static double CrossTeeShortSpacing = 24;
    private static double WireSpacing = 48;
    private static double WireHeight = 72;
    private static double WireLengthPerPound = 288;  // 288in/ib
    private static SharedPreferences sharedPref;
    private Context context;

    public Settings(Context context) {
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }
//        String syncConnPref = sharedPref.getString(SettingsActivity.KEY_PREF_SYNC_CONN, "");

    public void getScrewsPerPanel() {
        String s = sharedPref.getString(context.getString(R.string.screws_per_panel_key), "");
    }

    public static void setScrewsPerPanel(double screwsPerPanel) {
        Settings.screwsPerPanel = screwsPerPanel;
    }

    public static double getFurringChannelSpacing() {
        return furringChannelSpacing;
    }

    public static void setFurringChannelSpacing(double furringChannelSpacing) {
        Settings.furringChannelSpacing = furringChannelSpacing;
    }

    public static double getFurringLapLength() {
        return furringLapLength;
    }

    public static void setFurringLapLength(double furringLapLength) {
        Settings.furringLapLength = furringLapLength;
    }

    public static double getcChannelSpacing() {
        return cChannelSpacing;
    }

    public static void setcChannelSpacing(double cChannelSpacing) {
        Settings.cChannelSpacing = cChannelSpacing;
    }

    public static double getcChannelLapLength() {
        return cChannelLapLength;
    }

    public static void setcChannelLapLength(double cChannelLapLength) {
        Settings.cChannelLapLength = cChannelLapLength;
    }

    public static double getNailSpacing() {
        return nailSpacing;
    }

    public static void setNailSpacing(double nailSpacing) {
        Settings.nailSpacing = nailSpacing;
    }

    public static double getJointCompoundSettingTypeCoverage() {
        return JointCompoundSettingTypeCoverage;
    }

    public static void setJointCompoundSettingTypeCoverage(double jointCompoundSettingTypeCoverage) {
        JointCompoundSettingTypeCoverage = jointCompoundSettingTypeCoverage;
    }

    public static double getJointCompoundAllPurposeCoverage() {
        return JointCompoundAllPurposeCoverage;
    }

    public static void setJointCompoundAllPurposeCoverage(double jointCompoundAllPurposeCoverage) {
        JointCompoundAllPurposeCoverage = jointCompoundAllPurposeCoverage;
    }

    public static double getStudSpacing() {
        return studSpacing;
    }

    public static void setStudSpacing(double studSpacing) {
        Settings.studSpacing = studSpacing;
    }

    public static double getWastage() {
        return wastage;
    }

    public static void setWastage(double wastage) {
        Settings.wastage = wastage;
    }

    public static double getMainTeeSpacing() {
        return mainTeeSpacing;
    }

    public static void setMainTeeSpacing(double mainTeeSpacing) {
        Settings.mainTeeSpacing = mainTeeSpacing;
    }

    public static double getCrossTeeLongSpacing() {
        return CrossTeeLongSpacing;
    }

    public static void setCrossTeeLongSpacing(double crossTeeLongSpacing) {
        CrossTeeLongSpacing = crossTeeLongSpacing;
    }

    public static double getCrossTeeShortSpacing() {
        return CrossTeeShortSpacing;
    }

    public static void setCrossTeeShortSpacing(double crossTeeShortSpacing) {
        CrossTeeShortSpacing = crossTeeShortSpacing;
    }

    public static double getWireSpacing() {
        return WireSpacing;
    }

    public static void setWireSpacing(double wireSpacing) {
        WireSpacing = wireSpacing;
    }

    public static double getWireHeight() {
        return WireHeight;
    }

    public static void setWireHeight(double wireHeight) {
        WireHeight = wireHeight;
    }


    public static double getWireLengthPerPound() {
        return WireLengthPerPound;
    }

    public static void setWireLengthPerPound(double wireLengthPerPound) {
        WireLengthPerPound = wireLengthPerPound;
    }

    public static double getFeetToInches() {
        return feetToInches;
    }

    public static void setFeetToInches(double feetToInches) {
        Settings.feetToInches = feetToInches;
    }

    public static double getInchesToFeet() {
        return inchesToFeet;
    }

    public static void setInchesToFeet(double inchesToFeet) {
        Settings.inchesToFeet = inchesToFeet;
    }

    public static double getFeet2ToInch2() {
        return feet2ToInch2;
    }

    public static void setFeet2ToInch2(double feet2ToInch2) {
        Settings.feet2ToInch2 = feet2ToInch2;
    }

    public static double getInch2ToFeet2() {
        return inch2ToFeet2;
    }

    public static void setInch2ToFeet2(double inch2ToFeet2) {
        Settings.inch2ToFeet2 = inch2ToFeet2;
    }
}
