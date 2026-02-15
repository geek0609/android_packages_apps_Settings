/*
 * Copyright (C) 2023 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.lineage.health;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.android.internal.lineage.health.HealthInterface;
import com.google.android.material.slider.Slider;

import com.android.settings.R;

public class ChargingLimitPreference extends Preference
    implements Slider.OnChangeListener, Slider.OnSliderTouchListener {
    private static final String TAG = ChargingLimitPreference.class.getSimpleName();

    private TextView mChargingLimitValue;
    private Slider mChargingLimitBar;

    private final HealthInterface mHealthInterface;

    public ChargingLimitPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        setLayoutResource(R.layout.preference_charging_limit);

        mHealthInterface = HealthInterface.getInstance(context);
    }

    @Override
    public void onBindViewHolder(final PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        mChargingLimitValue = (TextView) holder.findViewById(R.id.value);

        mChargingLimitBar = (Slider) holder.findViewById(R.id.slider_widget);
        mChargingLimitBar.addOnChangeListener(this);
        mChargingLimitBar.addOnSliderTouchListener(this);

        int currLimit = getSetting();
        mChargingLimitBar.setValue(currLimit);
        updateValue(currLimit);
    }

    @Override
    public void onStartTrackingTouch(final Slider slider) {
    }

    @Override
    public void onStopTrackingTouch(final Slider slider) {
        setSetting(Math.round(slider.getValue()));
    }

    @Override
    public void onValueChange(final Slider slider, final float value,
            final boolean fromUser) {
        updateValue(Math.round(value));
    }

    public void setValue(final int value) {
        if (mChargingLimitBar != null) {
            mChargingLimitBar.setValue(value);
        }
        updateValue(value);
    }

    protected int getSetting() {
        return mHealthInterface.getLimit();
    }

    protected void setSetting(final int chargingLimit) {
        mHealthInterface.setLimit(chargingLimit);
    }

    private void updateValue(final int value) {
        if (mChargingLimitValue != null) {
            mChargingLimitValue.setText(String.format("%d%%", value));
        }
    }
}
