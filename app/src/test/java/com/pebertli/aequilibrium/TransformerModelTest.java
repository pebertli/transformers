package com.pebertli.aequilibrium;

import com.pebertli.aequilibrium.model.TransformerModel;

import org.junit.Assert;
import org.junit.Test;

public class TransformerModelTest
{
    @Test
    public void overall_is_correct()
    {
        TransformerModel m = new TransformerModel();
        m.setStrength(5);
        m.setIntelligence(5);
        m.setSpeed(5);
        m.setEndurance(5);
        m.setFirepower(5);

        Assert.assertEquals(25, m.getOverall());
    }

    @Test
    public void transformer_is_valid()
    {
        TransformerModel m = TransformerModel.randomTransformer("A");
        m.setId("");
        Assert.assertTrue(m.isValid());
    }

    @Test
    public void transformer_id_is_not_valid()
    {
        TransformerModel m = TransformerModel.randomTransformer("A");
        m.setId(null);
        Assert.assertFalse(m.isValid());
    }

    @Test
    public void transformer_skill_is_not_valid()
    {
        TransformerModel m = TransformerModel.randomTransformer("A");
        m.setId("");
        m.setFirepower(11);
        Assert.assertFalse(m.isValid());
    }

    @Test
    public void transformer_team_is_not_valid()
    {
        TransformerModel m = TransformerModel.randomTransformer("A");
        m.setId("");
        m.setTeam("C");
        Assert.assertFalse(m.isValid());
    }

    @Test
    public void transformer_name_is_not_valid()
    {
        TransformerModel m = TransformerModel.randomTransformer("A");
        m.setId("");
        m.setName("");
        Assert.assertFalse(m.isValid());
    }

    @Test
    public void transformer_name_is_null_valid()
    {
        TransformerModel m = TransformerModel.randomTransformer("A");
        m.setId("");
        m.setName(null);
        Assert.assertFalse(m.isValid());
    }


}