/*
 * *
 *  * Created by Pebertli Barata on 9/23/18 11:52 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/23/18 11:19 PM
 *
 */

package com.pebertli.aequilibrium.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pebertli.aequilibrium.R;
import com.pebertli.aequilibrium.model.TransformerModel;
import com.pebertli.aequilibrium.utils.BattleController;
import com.pebertli.aequilibrium.utils.BattleController.FaceoffResult;

import java.util.Collections;
import java.util.List;

/**
 * Activity that shows the animation of battle, based on the current autobots and decepticons list
 */
public class BattleActivity extends AppCompatActivity
{

    private List<TransformerModel> autobots;
    private List<TransformerModel> decepticons;
    private BattleController mBattleController;
    private Handler mHandler;
    private ViewGroup mAutobotsLayoutList;
    private ViewGroup mDecepticonsLayoutList;
    private FaceoffResult mLastResult = null;
    final private int mAnimationDuration = 5000;

    private final int BATTLE_STATE_READY = 0;
    private final int BATTLE_STATE_FIGHTING = 1;
    private final int BATTLE_STATE_DONE = 2;

    private int mState = BATTLE_STATE_READY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        Bundle bundle = getIntent().getExtras();

        //as Android does not recommend constructor for fragments, I could not do dependency injection
        if(bundle!=null)
        {
            try
            {
                autobots = (List<TransformerModel>) bundle.getSerializable("autobots");
                decepticons = (List<TransformerModel>) bundle.getSerializable("decepticons");
            }
            catch (ClassCastException e)
            {
                Log.e("Expception", "Not a list");
            }
        }

        //sort autobots by rank, with best ranks first
        Collections.sort(autobots);
        Collections.sort(decepticons);

        mBattleController = new BattleController(autobots, decepticons);

        //generate/draw autobots on the layout
        mAutobotsLayoutList = findViewById(R.id.autobotsBattleLayout);
        for (TransformerModel t : autobots)
        {
            View layout = configureLayout(getLayoutInflater(), t);
            mAutobotsLayoutList.addView(layout);
        }

        //generate/draw decepticons on the layout
        mDecepticonsLayoutList = findViewById(R.id.decepticonsBattleLayout);
        for (TransformerModel t : decepticons)
        {
            View layout = configureLayout(getLayoutInflater(), t);
            mDecepticonsLayoutList.addView(layout);
        }

        //handler for the animation thread
        //TODO improve the state when pause aplication
        mHandler = new Handler();

        //button to control the animation
        findViewById(R.id.skipAnimationButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch (mState)
                {
                    //starts animation after 1 second
                    case BATTLE_STATE_READY:
                        mHandler.postDelayed(mNextBattle, 1000);
                        mState = BATTLE_STATE_FIGHTING;
                        ((AppCompatImageButton)view).setImageResource(R.drawable.skip_icon);
                        break;
                     //skip the animation and show the final result
                    case BATTLE_STATE_FIGHTING:
                        skipBattle();
                        mState = BATTLE_STATE_DONE;
                        ((AppCompatImageButton)view).setImageResource(R.drawable.cancel_icon);
                        break;
                     //close the activity and back to the main activity
                    case BATTLE_STATE_DONE:
                        onBackPressed();
                        break;
                }


            }
        });
    }

    /**
     * Stop the animation and show the final result
     */
    private void skipBattle()
    {
        //stop all animations
        mHandler.removeCallbacksAndMessages(null);

        do
        {
            //remove the top transformers
            battle(mLastResult, false);
            //if it's a nuke, remove all transformers
            if(mLastResult!=null && mLastResult.type == FaceoffResult.FACEOFF_RESULT_TYPE_NUKE)
            {
                nuke();
                break;
            }
            //next battle
            mLastResult = mBattleController.nextFaceoff();

        }
        while (mLastResult != null );

        //show the final result
        log(mBattleController.generateBattleResultMessage());
    }

    /**
     * remove all transformers
     */
    private void nuke()
    {
        while(mAutobotsLayoutList.getChildCount() >0)
            mAutobotsLayoutList.removeViewAt(0);
        while(mDecepticonsLayoutList.getChildCount() >0)
            mDecepticonsLayoutList.removeViewAt(0);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mHandler.removeCallbacks(mNextBattle);
    }

    /**
     * thread to animate the battle
     */
    private Runnable mNextBattle = new Runnable()
    {
        @Override
        public void run()
        {

            mLastResult = mBattleController.nextFaceoff();
            if (mLastResult != null )
            {
                //Nuke, stop animation and destroy all transformers
                if(mLastResult.type == FaceoffResult.FACEOFF_RESULT_TYPE_NUKE)
                    skipBattle();

                //animate the battle

                battle(mLastResult, true);
                //next battle wit delay
                mHandler.postDelayed(mNextBattle, mAnimationDuration);
            }
            else
            {
                skipBattle();
            }

        }
    };

    /**
     * Animate the winner transformer, according to the type of battle
     */
    private void battle(FaceoffResult result, boolean animate)
    {
        //end of the battle or the row current animating was already removed
        if(result == null || result.processed)
        {
            log(mBattleController.generateBattleResultMessage());
            mState = BATTLE_STATE_DONE;
            ((AppCompatImageButton)findViewById(R.id.skipAnimationButton)).setImageResource(R.drawable.cancel_icon);
            return;
        }

        final ViewGroup autobotsLayoutList = findViewById(R.id.autobotsBattleLayout);
        final ViewGroup decepticonsLayoutList = findViewById(R.id.decepticonsBattleLayout);
        //update log
        log(result.message);

        //animate the criteria that made the transformer won
        if(animate)
        {
            //which transformer won
            View row = null;
            if(result.winner == FaceoffResult.FACEOFF_RESULT_AUTOBOTS)
                row = autobotsLayoutList.getChildAt(0);
            else if(result.winner == FaceoffResult.FACEOFF_RESULT_DECEPTICONS)
                row = decepticonsLayoutList.getChildAt(0);

            if(row != null)
            {
                //animate name
                TextView name = row.findViewById(R.id.transformerName);
                animateTextView(name);

                if (result.type == FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL)
                {
                    animateTextView((TextView) row.findViewById(R.id.strenghtCriteria).findViewById(R.id.textCriteria));
                    animateTextView((TextView) row.findViewById(R.id.intelligenceCriteria).findViewById(R.id.textCriteria));
                    animateTextView((TextView) row.findViewById(R.id.speedCriteria).findViewById(R.id.textCriteria));
                    animateTextView((TextView) row.findViewById(R.id.enduranceCriteria).findViewById(R.id.textCriteria));
                    animateTextView((TextView) row.findViewById(R.id.firepowerCriteria).findViewById(R.id.textCriteria));
                }

                if (result.type == FaceoffResult.FACEOFF_RESULT_TYPE_SKILL)
                {
                    animateTextView((TextView) row.findViewById(R.id.skillCriteria).findViewById(R.id.textCriteria));
                }

                if (result.type == FaceoffResult.FACEOFF_RESULT_TYPE_RUN)
                {
                    animateTextView((TextView) row.findViewById(R.id.courageCriteria).findViewById(R.id.textCriteria));
                    animateTextView((TextView) row.findViewById(R.id.strenghtCriteria).findViewById(R.id.textCriteria));
                }

                //don't animate nuke
                if (result.type == FaceoffResult.FACEOFF_RESULT_TYPE_NUKE)
                {
                    skipBattle();
                }
            }

            removeTop(mAnimationDuration/2);
        }
        else//just remove without animation
        {

            removeTop(0);
        }
    }

    /**
     * Just post the what's going on
     */
    private void log(String message)
    {
        ((TextView)findViewById(R.id.logTextView)).setText(message);
    }

    /**
     * remove the top views of transformers, with delay or not
     */
    private void removeTop(int delay)
    {
        if(delay > 0)
        {
            mHandler.postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    mLastResult.processed = true;
                    mAutobotsLayoutList.removeViewAt(0);
                    mDecepticonsLayoutList.removeViewAt(0);
                }

            }, delay);
        }
        else
        {
            mLastResult.processed = true;
            mAutobotsLayoutList.removeViewAt(0);
            mDecepticonsLayoutList.removeViewAt(0);
        }
    }

    /**
     * Animate the color of an text view
     */
    private void animateTextView(final TextView v)
    {

        Integer colorFrom = getResources().getColor(R.color.cardview_dark_background);
        Integer colorTo = getResources().getColor(R.color.colorAccent);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                v.setTextColor((Integer)animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    /**
     * Configure the list of the transformers with the proper icon and value of criteria
     */
    private View configureLayout(@NonNull LayoutInflater inflater, @NonNull TransformerModel t)
    {
        View layout = inflater.inflate(R.layout.layout_criteria_set, null, false);
        layout.setBackground(getResources().getDrawable(R.drawable.skill_layout_bg));
        ((AppCompatTextView)layout.findViewById(R.id.transformerName)).setText(t.getName());

        LinearLayoutCompat l =  layout.findViewById(R.id.strenghtCriteria);
        ((AppCompatTextView)l.findViewById(R.id.textCriteria)).setText(Integer.toString(t.getStrength()));
        ((AppCompatImageView) l.findViewById(R.id.iconCriteria)).setImageResource(R.drawable.strength_icon);
        l =  layout.findViewById(R.id.intelligenceCriteria);
        ((AppCompatTextView)l.findViewById(R.id.textCriteria)).setText(Integer.toString(t.getIntelligence()));
        ((AppCompatImageView) l.findViewById(R.id.iconCriteria)).setImageResource(R.drawable.intelligence_icon);
        l =  layout.findViewById(R.id.speedCriteria);
        ((AppCompatTextView)l.findViewById(R.id.textCriteria)).setText(Integer.toString(t.getSpeed()));
        ((AppCompatImageView) l.findViewById(R.id.iconCriteria)).setImageResource(R.drawable.speed_icon);
        l =  layout.findViewById(R.id.enduranceCriteria);
        ((AppCompatTextView)l.findViewById(R.id.textCriteria)).setText(Integer.toString(t.getEndurance()));
        ((AppCompatImageView) l.findViewById(R.id.iconCriteria)).setImageResource(R.drawable.endurance_icon);
        l =  layout.findViewById(R.id.rankCriteria);
        ((AppCompatTextView)l.findViewById(R.id.textCriteria)).setText(Integer.toString(t.getRank()));
        ((AppCompatImageView) l.findViewById(R.id.iconCriteria)).setImageResource(R.drawable.rank_icon);
        l =  layout.findViewById(R.id.courageCriteria);
        ((AppCompatTextView)l.findViewById(R.id.textCriteria)).setText(Integer.toString(t.getCourage()));
        ((AppCompatImageView) l.findViewById(R.id.iconCriteria)).setImageResource(R.drawable.courage_icon);
        l =  layout.findViewById(R.id.firepowerCriteria);
        ((AppCompatTextView)l.findViewById(R.id.textCriteria)).setText(Integer.toString(t.getFirepower()));
        ((AppCompatImageView) l.findViewById(R.id.iconCriteria)).setImageResource(R.drawable.firepower_icon);
        l =  layout.findViewById(R.id.skillCriteria);
        ((AppCompatTextView)l.findViewById(R.id.textCriteria)).setText(Integer.toString(t.getSkill()));
        ((AppCompatImageView) l.findViewById(R.id.iconCriteria)).setImageResource(R.drawable.skill_icon);

        return layout;
    }
}
