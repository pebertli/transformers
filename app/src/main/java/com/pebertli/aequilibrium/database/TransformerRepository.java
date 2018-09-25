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

    public void insertAsync(final TransformerModel model)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                model.setFlagInsert(true);
                TransformersDatabase.getDatabase(mContext).transformerDao().insertTransformer(model);
            }
        }) .start();
    }

    public Long insert(final TransformerModel model)
    {
        model.setFlagInsert(true);
        return TransformersDatabase.getDatabase(mContext).transformerDao().insertTransformer(model);
    }

    public void updateWithId(final TransformerModel model)
    {
        model.setFlagInsert(false);
        TransformersDatabase.getDatabase(mContext).transformerDao().updateTransformer(model);
    }

    public void insertNewAsync(final List<TransformerModel> models)
    {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//        instance.transformerDao().insertNewTransformers(models);
//            }
//        }) .start();
    }

    public List<TransformerModel> getAll()
    {

        return TransformersDatabase.getDatabase(mContext).transformerDao().getAll();
    }

    public List<TransformerModel> getAutobots()
    {

        return TransformersDatabase.getDatabase(mContext).transformerDao().getAutobots();
    }

    public List<TransformerModel> getDecepticons()
    {

        return TransformersDatabase.getDatabase(mContext).transformerDao().getDecepticons();
    }

    public void syncLocal(final List<TransformerModel> models)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

        List<TransformerModel> resultID = TransformersDatabase.getDatabase(mContext).transformerDao().getWithId();
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

                TransformersDatabase.getDatabase(mContext).transformerDao().insertNewTransformers(diff);

                if(mListener != null)
                    if(diff.isEmpty())
                        mListener.onSync(null
                                ,null
                        );
                else
                    mListener.onSync(TransformersDatabase.getDatabase(mContext).transformerDao().getAutobots()
                            ,TransformersDatabase.getDatabase(mContext).transformerDao().getDecepticons()
                    );
            }
        }) .start();
    }

    public boolean insertDiff(final List<TransformerModel> models)
    {

                List<TransformerModel> resultID = TransformersDatabase.getDatabase(mContext).transformerDao().getWithId();
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

                TransformersDatabase.getDatabase(mContext).transformerDao().insertNewTransformers(diff);
                return true;
    }

}
