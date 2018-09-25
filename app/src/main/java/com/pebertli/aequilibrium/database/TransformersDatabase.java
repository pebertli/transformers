/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 9:31 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/24/18 9:31 PM
 *
 */

package com.pebertli.aequilibrium.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.pebertli.aequilibrium.dao.TransformerDao;
import com.pebertli.aequilibrium.model.TransformerModel;

import java.util.List;

@Database(entities = {TransformerModel.class}, version = 2)
public abstract class TransformersDatabase extends RoomDatabase
{
    public abstract TransformerDao transformerDao();
    private static TransformersDatabase instance;

    public static TransformersDatabase getDatabase(final Context context)
    {
        if(instance == null)
        {
            synchronized (TransformersDatabase.class)
            {
                if (instance == null)
                {
                    instance =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    TransformersDatabase.class, "transformes_db")
                                    .fallbackToDestructiveMigration()
                                    .build();

                }
            }
        }

        return instance;
    }

    public void insertAsync(final TransformerModel model)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                model.setFlagInsert(true);
                instance.transformerDao().insertTransformer(model);
            }
        }) .start();
    }

    public List<TransformerModel> getAll()
    {
        return instance.transformerDao().getAll();
    }


}
