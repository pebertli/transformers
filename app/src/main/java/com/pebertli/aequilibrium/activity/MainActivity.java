/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 12:22 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/23/18 11:01 PM
 *
 */

package com.pebertli.aequilibrium.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
;
import com.pebertli.aequilibrium.database.TransformersDatabase;
import com.pebertli.aequilibrium.fragment.EditFragment;
import com.pebertli.aequilibrium.R;
import com.pebertli.aequilibrium.network.Session;
import com.pebertli.aequilibrium.adapter.TransformerAdapter;
import com.pebertli.aequilibrium.model.TransformerModel;
import com.pebertli.aequilibrium.network.TransformersAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main activity of the app that receives callbacks from Rest API Recyclerview clicks and fragment response
 */
public class MainActivity extends FragmentActivity implements TransformersAPI.ApiListener, TransformerAdapter.ItemClickListener, EditFragment.FinishEditFragment
{
    private TransformerAdapter mAdapterAutobots;
    private TransformerAdapter mAdapterDecepticons;
    private TransformersAPI mApi;
    private RecyclerView mRecyclerViewAutobots;
    private RecyclerView mRecyclerViewDecepticons;
    private EditFragment editFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            //hardcoded the auth token
            Session.getInstance().init(this);
            Session.getInstance().setSessionStringValue("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0cmFuc2Zvcm1lcnNJZCI6Ii1MTWdhOExnLVJVMnpJWXVpUE5JIiwiaWF0IjoxNTM3MjcyNzQ3fQ.oZJ_dQKY_nPPFrbqpCDJR6Y1X49cXdU4Jbgv2NyvEWM");

            //create the recyclers and adapters
            mRecyclerViewAutobots = findViewById(R.id.autobotsRecycler);
            mAdapterAutobots = new TransformerAdapter(this, new ArrayList<TransformerModel>());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerViewAutobots.setLayoutManager(layoutManager);
            mRecyclerViewAutobots.setAdapter(mAdapterAutobots);
            RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
            mRecyclerViewAutobots.setItemAnimator(animator);
            mAdapterAutobots.setItemClickListener(this);

            mRecyclerViewDecepticons = findViewById(R.id.decepticonsRecycler);
            mAdapterDecepticons = new TransformerAdapter(this, new ArrayList<TransformerModel>());
            layoutManager = new LinearLayoutManager(this);
            mRecyclerViewDecepticons.setLayoutManager(layoutManager);
            mRecyclerViewDecepticons.setAdapter(mAdapterDecepticons);
            animator = new DefaultItemAnimator();
            mRecyclerViewDecepticons.setItemAnimator(animator);
            mAdapterDecepticons.setItemClickListener(this);


            //Rest API
            mApi = new TransformersAPI();
            mApi.setApiListener(this);

            //calls the creation fragment
            findViewById(R.id.addImageButton).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    showSkillEditFragment(TransformerModel.weakTransformer("A"), -1, false);
                }
            });

            //calls the fight animation
            findViewById(R.id.battleImageButton).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(view.getContext(), BattleActivity.class);

                    Bundle b = new Bundle();
                    b.putSerializable("autobots", (ArrayList<TransformerModel>) mAdapterAutobots.getItems());
                    b.putSerializable("decepticons", (ArrayList<TransformerModel>) mAdapterDecepticons.getItems());
                    intent.putExtras(b);

                    startActivity(intent);
                }
            });
    }

    /**
     * callback from adapter. Which row will be deleted?
     */
    @Override
    public void deleteClicked(TransformerModel model, int itemIndex)
    {
        if(model.getTeam().equals("A"))
            mApi.deleteTransformer(mAdapterAutobots.getItem(itemIndex));
        else if(model.getTeam().equals("D"))
            mApi.deleteTransformer(mAdapterDecepticons.getItem(itemIndex));
    }

    /**
     * callback from adapter. Which row will be edited?
     */
    @Override
    public void itemClicked(TransformerModel model, int itemIndex)
    {
        showSkillEditFragment(model, itemIndex, true);
    }

    /**
     * calls the edit fragment
     */
    private void showSkillEditFragment(TransformerModel model, int itemIndex, boolean editMode)
    {
        //a custom call to editFragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);

        //sending information to editFragment
        Bundle b = new Bundle();
        b.putSerializable("model", model);
        b.putBoolean("editMode", editMode);
        b.putInt("row", itemIndex);

        editFragment = new EditFragment();
        editFragment.setFinishListener(this);
        editFragment.setArguments(b);

        //put the editFragment on top
        ft.replace(R.id.editFragmentContainer, editFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * response from fragment without creation or edition
     */
    @Override
    public void CancelFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount() > 0)//there is a editFragment
        {
            //remove it from top
            fm.popBackStack();
            hideKeyboard();

        }
        else
        {
            //by default, android will finish activity
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        hideKeyboard();

    }

    private void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = this.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * response from fragment with creation or edition
     */
    @Override
    public void ConfirmFragment(TransformerModel result, boolean editMode, int indexRow)
    {
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount() > 0)//there is a editFragment
        {
            if(editMode && result != null)
            {
                mApi.updateTransformer(result);
            }
            else if(!editMode && result!=null)
            {
                mApi.addTransformer(result);
            }

        }
        else
        {
            //by default, android will finish activity
            super.onBackPressed();
        }

    }

    /**
     * response from Rest API with a created transformer
     */
    @Override
    public void onCreateResponse(TransformerModel model, int code)
    {
        if (code != 500)
        {
            //add on top and scroll to there
            if(model.getTeam().equals("A"))
            {
                mAdapterAutobots.addItem(model);
                mRecyclerViewAutobots.smoothScrollToPosition(0);
            }
            else if(model.getTeam().equals("D"))
            {
                mAdapterDecepticons.addItem(model);
                mRecyclerViewDecepticons.smoothScrollToPosition(0);
            }

            //make sure that saves the new list
            //todo SYNC with SQLite
            saveListOnPreferences();
        }

        //remove the fragment after receives an API response
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount() > 0)//there is a editFragment
        {
            //remove it from top
            fm.popBackStack();
        }
    }

    /**
     * response from Rest API with a deleted transformer
     */
    @Override
    public void onDeleteResponse(TransformerModel model, int code)
    {
        if(code != 500)
        {
            if(model.getTeam().equals("A"))
                mAdapterAutobots.removeItem(model);
            else if(model.getTeam().equals("D"))
                mAdapterDecepticons.removeItem(model);

            //make sure that saves the new list
            //todo SYNC with SQLite
            saveListOnPreferences();
        }
    }

    /**
     * response from Rest API with a updated transformer
     */
    @Override
    public void onUpdateResponse(TransformerModel originalModel, int code)
    {
        if (code != 500)
        {
            if(originalModel.getTeam().equals("A"))
                mAdapterAutobots.updateItem(originalModel, originalModel);
            else if(originalModel.getTeam().equals("D"))
                mAdapterDecepticons.updateItem(originalModel, originalModel);

            //make sure that saves the new list
            //todo SYNC with SQLite
            saveListOnPreferences();
        }

        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount() > 0)//there is a editFragment
        {
            //remove it from top
            fm.popBackStack();
        }
    }

    /**
     * response from Rest API with a GET of one transformer
     */
    @Override
    public void onGetResponse(TransformerModel model, int code)
    {

    }

    /**
     * response from Rest API with a GET of a list of transformers
     */
    @Override
    public void onGetResponse(List<TransformerModel> list, int code)
    {
        if(list!=null)
        {
            List<TransformerModel> autobots = new ArrayList<>();
            List<TransformerModel> decepticons = new ArrayList<>();
            //separate on each team
            for (TransformerModel t: list)
            {
                if(t.getTeam().equals("A"))
                    autobots.add(t);
                else if (t.getTeam().equals("D"))
                    decepticons.add(t);
            }

            mAdapterAutobots.setItems(autobots);
            mAdapterDecepticons.setItems(decepticons);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    /**
     * Try to retrieve saved transformes after an restart of app
     * otherwise, get from the Rest API
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        Gson gson = new Gson();

        String jsonText = Session.getInstance().getSessionStringValue("aList");
        TransformerModel[] text = gson.fromJson(jsonText, TransformerModel[].class);
        List<TransformerModel> aList = null;
        if(text != null)
            aList = new ArrayList<>(Arrays.asList(text));

        jsonText = Session.getInstance().getSessionStringValue("dList");
        text = gson.fromJson(jsonText, TransformerModel[].class);
        List<TransformerModel> dList = null;
        if(text != null)
            dList = new ArrayList<>(Arrays.asList(text));

        //if there aren't transformers, try to get from Rest API
        if((aList!= null && !aList.isEmpty()) || (dList != null  && !dList.isEmpty()))
        {
            mAdapterAutobots.setItems(aList);
            mAdapterDecepticons.setItems(dList);
        }
        else
        {
            mApi.getAll();
        }

        final TransformerModel model = TransformerModel.randomTransformer("A");
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<TransformerModel> mm = TransformersDatabase.getDatabase(MainActivity.this).transformerDao().getAll();
                mAdapterAutobots.setItems(mm);

            }
        }) .start();



    }

    @Override
    protected void onStop()
    {
        super.onStop();
        saveListOnPreferences();

    }

    /**
     * Save current transformers on cache, avoiding lose the list in restart and calls to Rest API
     *
     */
    private void saveListOnPreferences()
    {
        Gson gson = new Gson();

        String jsonText = gson.toJson(mAdapterAutobots.getItems());
        Session.getInstance().setSessionStringValue("aList", jsonText);
        jsonText = gson.toJson(mAdapterDecepticons.getItems());
        Session.getInstance().setSessionStringValue("dList", jsonText);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


}
