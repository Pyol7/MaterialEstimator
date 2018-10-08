package com.jeffreyromero.materialestimator.utilities;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;
import android.util.AttributeSet;

public class CustomEditTextPreference extends EditTextPreference {

    public CustomEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditTextPreference(Context context) {
        super(context);
    }

    /**
     * Returns the title of this Preference.
     *
     * @return The title.
     * @see #setTitle(CharSequence)
     */
    @Override
    public CharSequence getTitle() {
        //Get default value
        String value = getSharedPreferences().getString(getKey(), "");
        //Display the value in the title
        return super.getTitle() + " : " + value;
    }
}
