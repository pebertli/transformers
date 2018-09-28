package com.pebertli.aequilibrium.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.pebertli.aequilibrium.dao.TransformerDao;
import com.pebertli.aequilibrium.model.TransformerModel;

import java.util.ArrayList;
import java.util.List;

public class TransformerRepository
{
    public interface LocalDatabaseListener
    {
        void onSync(final List<TransformerModel> autobots, final List<TransformerModel> decepticons);
        //void onInsert()

    }

    private Context mContext;
    private LocalDatabaseListener mListener;

    public TransformerRepository(Context mContext)
    {
        this.mContext = mContext;
    }

    public void setListener(LocalDatabaseListener mListener)
    {
        this.mListener = mListener;
    }

//    public void insertAsync(final TransformerModel model)
//    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                model.setFlagInsert(true);
//                TransformersDatabase.getDatabase(mContext).transformerDao().insertTransformer(model);
//            }
//        }) .start();
//    }

    public Long insert(final TransformerModel model, boolean test)
    {
        model.setFlagInsert(true);
        return TransformersDatabase.getDatabase(mContext, test).transformerDao().insertTransformer(model);
    }

    public void updateWithId(final TransformerModel model, boolean test)
    {
        model.setFlagInsert(false);
        TransformersDatabase.getDatabase(mContext, test).transformerDao().updateTransformer(model);
    }

    public void update(final TransformerModel model, boolean test)
    {
        if(model.getId() != null && !model.getId().isEmpty())
        {
            model.setFlagInsert(false);
            model.setFlagUpdate(true);
        }
        else
        {
            model.setFlagInsert(true);
            model.setFlagUpdate(false);
        }

        TransformersDatabase.getDatabase(mContext, test).transformerDao().updateTransformer(model);
    }

    public List<TransformerModel> getAll(boolean test)
    {

        return TransformersDatabase.getDatabase(mContext, test).transformerDao().getAll();
    }

    public List<TransformerModel> getAutobots(boolean test)
    {

        return TransformersDatabase.getDatabase(mContext, test).transformerDao().getAutobots();
    }

    public List<TransformerModel> getDecepticons(boolean test)
    {

        return TransformersDatabase.getDatabase(mContext, test).transformerDao().getDecepticons();
    }

    public void syncLocal(final List<TransformerModel> models,final  boolean test)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

        List<TransformerModel> resultID = TransformersDatabase.getDatabase(mContext, test).transformerDao().getWithId();
        List<TransformerModel> diff = new ArrayList<>();
        for(TransformerModel t1 : models)
        {
            boolean isNew = true;
            for (TransformerModel t2 : resultID)
            {
                if (t1.getId().equals(t2.getId()))
                {
                    isNew = false;
                    continue;
                }
            }
            if(isNew)
                diff.add(t1);
        }

                TransformersDatabase.getDatabase(mContext, test).transformerDao().insertNewTransformers(diff);

                if(mListener != null)
                    if(diff.isEmpty())
                        mListener.onSync(null
                                ,null
                        );
                else
                    mListener.onSync(TransformersDatabase.getDatabase(mContext, test).transformerDao().getAutobots()
                            ,TransformersDatabase.getDatabase(mContext, test).transformerDao().getDecepticons()
                    );
            }
        }) .start();
    }

    public boolean insertDiff(final List<TransformerModel> models, boolean test)
    {

                List<TransformerModel> resultID = TransformersDatabase.getDatabase(mContext, test).transformerDao().getWithId();
                List<TransformerModel> diff = new ArrayList<>();
                for(TransformerModel t1 : models)
                {
                    boolean isNew = true;
                    for (TransformerModel t2 : resultID)
                    {
                        if (t1.getId().equals(t2.getId()))
                        {
                            isNew = false;
                            continue;
                        }
                    }
                    if(isNew)
                        diff.add(t1);
                }

                if(diff.isEmpty())
                    return false;

                TransformersDatabase.getDatabase(mContext, test).transformerDao().insertNewTransformers(diff);
                return true;
    }

}
