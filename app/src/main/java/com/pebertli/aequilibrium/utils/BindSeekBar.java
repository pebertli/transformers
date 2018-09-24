/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 1:38 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/24/18 1:29 AM
 *
 */

package com.pebertli.aequilibrium.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A custom seek bar component that can be binded to a text view
 * The text view shows the value of the seekbar automatically
 * starting from 1
 * //TODO get rid off this offset hardcoded
 */
public class BindSeekBar extends AppCompatSeekBar implements AppCompatSeekBar.OnSeekBarChangeListener
{
    private TextView mTextView;
    private String mAppendText;

    public BindSeekBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context);
    }

    public BindSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BindSeekBar(Context context) {
        super(context);

    }

    public void setTextFieldBind(String appendText, TextView textView, int initialValue)
    {
        this.setOnSeekBarChangeListener(this);
        mTextView = textView;
        mAppendText = appendText;
        //if changed, calls set progress programatically will call onProgressChanged too
        if(getProgress() != initialValue-1)
            setProgress(initialValue-1);
        else // if not changed, onProgressChanged should be called manually
        {
            onProgressChanged(this, initialValue-1, false);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {

        int value = i+1;
        //was called programatically?
        if(b)
            value = i+1;
        if(mAppendText != null && !mAppendText.isEmpty())
            mTextView.setText(mAppendText+value);
        else
            mTextView.setText(value);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }
}
