/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 12:59 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/23/18 11:23 PM
 *
 */

package com.pebertli.aequilibrium.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pebertli.aequilibrium.R;
import com.pebertli.aequilibrium.model.TransformerModel;
import com.pebertli.aequilibrium.utils.BindSeekBar;

/**
 * Fragment to create or edit transformers
 */
public class EditFragment extends Fragment
{

    /**
     * Interface to call the listener when the fragment was canceled or confirmed
     */
    public interface FinishEditFragment
    {
        void CancelFragment();
        void ConfirmFragment(TransformerModel result, boolean editMode, int indexRow);

    }


    private TransformerModel model;
    private FinishEditFragment mListener;
    private boolean mEditMode = true;
    private int indexRow = -1;
    private EditText mNameEditText;
    private RadioButton mAutobotsRadioButton;
    private RadioButton mDecepticonsRadioButton;
    private BindSeekBar mStrengthSeekBar;
    private BindSeekBar mIntelligenceSeekBar;
    private BindSeekBar mSpeedSeekBar;
    private BindSeekBar mEnduranceSeekBar;
    private BindSeekBar mRankSeekBar;
    private BindSeekBar mCourageSeekBar;
    private BindSeekBar mFirepowerSeekBar;
    private BindSeekBar mSkillSeekBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        Bundle bundle = getArguments();

        mEditMode = true;

        mNameEditText = view.findViewById(R.id.nameEditTextFragment);
        mAutobotsRadioButton = view.findViewById(R.id.autobotsRadioButton);
        mDecepticonsRadioButton = view.findViewById(R.id.decepticonsRadioButton);
        mStrengthSeekBar = view.findViewById(R.id.strengthSeekBarFragment);
        mIntelligenceSeekBar = view.findViewById(R.id.intelligenceSeekBarFragment);
        mSpeedSeekBar = view.findViewById(R.id.speedSeekBarFragment);
        mEnduranceSeekBar = view.findViewById(R.id.enduranceSeekBarFragment);
        mRankSeekBar = view.findViewById(R.id.rankSeekBarFragment);
        mCourageSeekBar = view.findViewById(R.id.courageSeekBarFragment);
        mFirepowerSeekBar = view.findViewById(R.id.firepowerSeekBarFragment);
        mSkillSeekBar = view.findViewById(R.id.skillSeekBarFragment);


        //as Android does not recommend constructor for fragments, I could not do dependency injection
        if(bundle!=null)
        {
            model = (TransformerModel) bundle.getSerializable("model");
            mEditMode =  bundle.getBoolean("editMode");
            indexRow =  bundle.getInt("row");
        }


                mAutobotsRadioButton.setEnabled(!mEditMode);
                mDecepticonsRadioButton.setEnabled(!mEditMode);
                mNameEditText.setText(model.getName());

                if (model.getTeam().equals("A"))
                    mAutobotsRadioButton.setChecked(true);
                else
                    mDecepticonsRadioButton.setChecked(true);

                //bind seekbar to values
                mStrengthSeekBar.setTextFieldBind("Strength ", (TextView) view.findViewById(R.id.strengthTextViewFragment), model.getStrength());
                mIntelligenceSeekBar.setTextFieldBind("Intelligence ", (TextView) view.findViewById(R.id.intelligenceTextViewFragment), model.getIntelligence());
                mSpeedSeekBar.setTextFieldBind("Speed ", (TextView) view.findViewById(R.id.speedTextViewFragment), model.getSpeed());
                mEnduranceSeekBar.setTextFieldBind("Endurance ", (TextView) view.findViewById(R.id.enduranceTextViewFragment), model.getEndurance());
                mRankSeekBar.setTextFieldBind("Rank ", (TextView) view.findViewById(R.id.rankTextViewFragment), model.getRank());
                mCourageSeekBar.setTextFieldBind("Courage ", (TextView) view.findViewById(R.id.courageTextViewFragment), model.getCourage());
                mFirepowerSeekBar.setTextFieldBind("Firepower ", (TextView) view.findViewById(R.id.firepowerTextViewFragment), model.getFirepower());
                mSkillSeekBar.setTextFieldBind("Skill ", (TextView) view.findViewById(R.id.skillTextViewFragment), model.getSkill());


                //confirm button callback
        view.findViewById(R.id.confirmButtonFragment).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name = mNameEditText.getText().toString();
                if(mListener != null )
                {
                    //can't save if name is empty
                    if(!name.isEmpty())
                        mListener.ConfirmFragment(gatherTransformModel(), mEditMode, indexRow);
                    else
                        Toast.makeText(getActivity(), "The name can not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        //cancel button callback
        view.findViewById(R.id.cancelButtonFragment).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mListener != null)
                    mListener.CancelFragment();
            }
        });

        //fill UI with random values for transformer
        view.findViewById(R.id.shuffleButtonFragment).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                model.randomizeMe(!mEditMode);
                mNameEditText.setText(model.getName());
                if (model.getTeam().equals("A"))
                    mAutobotsRadioButton.setChecked(true);
                else
                    mDecepticonsRadioButton.setChecked(true);

                mStrengthSeekBar.setProgress(model.getStrength()-1);
                mIntelligenceSeekBar.setProgress(model.getIntelligence()-1);
                mSpeedSeekBar.setProgress(model.getSpeed()-1);
                mEnduranceSeekBar.setProgress(model.getEndurance()-1);
                mRankSeekBar.setProgress(model.getRank()-1);
                mCourageSeekBar.setProgress(model.getCourage()-1);
                mFirepowerSeekBar.setProgress(model.getFirepower()-1);
                mSkillSeekBar.setProgress(model.getSkill()-1);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        //if there was a listener when the parent activity was stopped, then uses the parent as listener
        //todo retrieve from saved instance
        if(getActivity() != null)
        {
            try
            {
                mListener = (FinishEditFragment) getActivity();
            } catch (ClassCastException e)
            {
                throw new ClassCastException(getActivity().toString() + " must implement FinishEditFragment Listener");
            }
        }
    }

    public void setFinishListener(FinishEditFragment mListener)
    {
        this.mListener = mListener;
    }

    /**
     * Get the info from UI and fill a Transformer
     */
    private TransformerModel gatherTransformModel()
    {
        TransformerModel m;

        if(model != null)
            m = model;
        else
            m = new TransformerModel();


        if(mAutobotsRadioButton.isChecked())
            m.setTeam("A");
        else
            m.setTeam("D");
        m.setName(mNameEditText.getText().toString());
        m.setStrength(mStrengthSeekBar.getProgress()+1);
        m.setIntelligence(mIntelligenceSeekBar.getProgress()+1);
        m.setSpeed(mSpeedSeekBar.getProgress()+1);
        m.setEndurance(mEnduranceSeekBar.getProgress()+1);
        m.setRank(mRankSeekBar.getProgress()+1);
        m.setCourage(mCourageSeekBar.getProgress()+1);
        m.setFirepower(mFirepowerSeekBar.getProgress()+1);
        m.setSkill(mSkillSeekBar.getProgress()+1);

        return m;
    }


    @Override
    public void onStop()
    {
        super.onStop();
        //hide the soft keyboard when finish the fragment
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(imm != null && getView() != null)
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}
