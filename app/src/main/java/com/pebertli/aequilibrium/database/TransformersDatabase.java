/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 9:31 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/24/18 9:31 PM
 *
 */

package com.pebertli.aequilibrium.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.pebertli.aequilibrium.activity.MainActivity;
import com.pebertli.aequilibrium.dao.TransformerDao;
import com.pebertli.aequilibrium.model.TransformerModel;

import java.util.List;

@Database(entities = {TransformerModel.class}, version = 3)
public abstract class TransformersDatabase extends RoomDatabase
{

    public abstract TransformerDao transformerDao();
    private static TransformersDatabase instance;
    private static TransformersDatabase instanceTest;

    public static TransformersDatabase getDatabase(final Context context, boolean test)
    {
        if(!test)
        {
            if (instance == null)
            {
                synchronized (TransformersDatabase.class)
                {
                    if (instance == null)
                    {
                        instance =
                                Room.databaseBuilder(context.getApplicationContext(),
                                        TransformersDatabase.class, "transformes_db")
                                        .fallbackToDestructiveMigration()
                                        .allowMainThreadQueries()
                                        .build();


                    }
                }
            }

            return instance;
        }
        else
        {
            if (instanceTest == null)
            {
                synchronized (TransformersDatabase.class)
                {
                    if (instanceTest == null)
                    {
                        instanceTest =
                                Room.databaseBuilder(context.getApplicationContext(),
                                        TransformersDatabase.class, "transformes_test_db")
                                        .fallbackToDestructiveMigration()
                                        .allowMainThreadQueries()
                                        .build();


                    }
                }
            }

            return instanceTest;
        }
    }

}
