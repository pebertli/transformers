package com.pebertli.aequilibrium;

import android.support.test.InstrumentationRegistry;

import com.pebertli.aequilibrium.database.TransformerRepository;
import com.pebertli.aequilibrium.model.TransformerModel;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DatabaseTransformerTest
{
    @Test
    public void addTransformers()
    {
        TransformerRepository rep = new TransformerRepository(InstrumentationRegistry.getTargetContext());
        long id = rep.insert(TransformerModel.randomTransformer("A"), true);

        List<TransformerModel> ret = rep.getAll(true);
        Assert.assertEquals(id, ret.get(ret.size()-1).getSurrogateKey());

    }
}
