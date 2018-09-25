/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 9:15 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/24/18 9:15 PM
 *
 */

package com.pebertli.aequilibrium.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pebertli.aequilibrium.model.TransformerModel;

import java.util.List;

@Dao
public interface TransformerDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertTransformer(TransformerModel model);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public List<Long> insertNewTransformers(List<TransformerModel> models);

    @Update
    public void updateTransformer(TransformerModel model);

    @Update
    public int updateTransformers(List<TransformerModel> models);

    @Delete
    public int deleteTransformer(TransformerModel model);

    @Delete
    public int deleteTransformers(List<TransformerModel> models);

    @Query("SELECT * FROM TransformerModel")
    public List<TransformerModel> getAll();

    @Query("SELECT * FROM TransformerModel where id is not null")
    public List<TransformerModel> getWithId();


    @Query("SELECT * FROM TransformerModel where team = 'A'")
    public List<TransformerModel> getAutobots();

    @Query("SELECT * FROM TransformerModel where team = 'D'")
    public List<TransformerModel> getDecepticons();
}
