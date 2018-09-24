package com.pebertli.aequilibrium;

import com.pebertli.aequilibrium.model.TransformerModel;
import com.pebertli.aequilibrium.utils.BattleController;
import com.pebertli.aequilibrium.utils.BattleController.FaceoffResult;
import com.pebertli.aequilibrium.utils.Utils;

import org.junit.Assert;
import org.junit.Test;

public class FaceoffTest {

    BattleController battleController = new BattleController(null, null);

    @Test
    public void faceoff_type_nuke_optimus_predaking()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("optimus prime");
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("predaking");

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_NUKE, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TIE, r.winner);
    }

    @Test
    public void faceoff_type_nuke_predaking_optimus()
    {
        TransformerModel a = TransformerModel.randomTransformer("D");
        a.setName("optimus prime");
        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("predaking");

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_NUKE, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TIE, r.winner);
    }

    @Test
    public void faceoff_type_nuke_predaking_predaking()
    {
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("predaking");

        FaceoffResult r = battleController.faceoff(b, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_NUKE, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TIE, r.winner);
    }

    @Test
    public void faceoff_type_nuke_optimus_optimus()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("optimus prime");

        FaceoffResult r = battleController.faceoff(a, a);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_NUKE, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TIE, r.winner);
    }

    @Test
    public void faceoff_type_not_nuke_predaking_optimus()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("optimus primea");
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("predaking");

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertNotEquals(FaceoffResult.FACEOFF_RESULT_TYPE_NUKE, r.type);
        Assert.assertNotEquals(FaceoffResult.FACEOFF_RESULT_TIE, r.winner);
    }

    @Test
    public void faceoff_type_predaking_autobots_win()
    {
        TransformerModel a = TransformerModel.weakTransformer("A");
        a.setName("predaking");
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("A");

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_STAR, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_DECEPTICONS, r.winner);
    }

    @Test
    public void faceoff_type_predaking_decepticons_win()
    {
        TransformerModel a = TransformerModel.weakTransformer("D");
        a.setName("predaking");
        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("A");

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_STAR, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_DECEPTICONS, r.winner);
    }

    @Test
    public void faceoff_type_optimus_autobots_win()
    {
        TransformerModel a = TransformerModel.weakTransformer("A");
        a.setName("optimus prime");
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("A");

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_STAR, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_AUTOBOTS, r.winner);
    }

    @Test
    public void faceoff_type_optimus_decepticons_win()
    {
        TransformerModel a = TransformerModel.weakTransformer("D");
        a.setName("optimus prime");
        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("A");

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_STAR, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_AUTOBOTS, r.winner);
    }

    @Test
    public void faceoff_type_run_autobots_a()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("A");
        a.setSkill(1);
        a.setCourage(9);
        a.setStrength(7);
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("D");
        b.setSkill(1);
        b.setCourage(Utils.randomInt(1,5));
        b.setStrength(Utils.randomInt(1,4));

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_RUN, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_AUTOBOTS, r.winner);
    }

    @Test
    public void faceoff_type_run_autobots_b()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("A");
        a.setSkill(1);
        a.setCourage(9);
        a.setStrength(7);
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("D");
        b.setSkill(1);
        b.setCourage(Utils.randomInt(1,5));
        b.setStrength(Utils.randomInt(1,4));

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_RUN, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_AUTOBOTS, r.winner);
    }

    @Test
    public void faceoff_type_run_decepticons()
    {
        TransformerModel a = TransformerModel.randomTransformer("D");
        a.setName("D");
        a.setSkill(1);
        a.setCourage(9);
        a.setStrength(7);
        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("D");
        b.setSkill(1);
        b.setCourage(Utils.randomInt(1,5));
        b.setStrength(Utils.randomInt(1,4));

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_RUN, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_DECEPTICONS, r.winner);
    }

    @Test
    public void faceoff_type_not_run_autobots()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("A");
        a.setSkill(1);
        a.setCourage(9);
        a.setStrength(7);
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("D");
        b.setSkill(1);
        b.setCourage(Utils.randomInt(6,10));
        b.setStrength(Utils.randomInt(5,9));

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertNotEquals(FaceoffResult.FACEOFF_RESULT_TYPE_RUN, r.type);
    }

    @Test
    public void faceoff_type_skill_autobots_a()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("A");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("D");
        b.setSkill(Utils.randomInt(1,4));
        b.setCourage(9);
        b.setStrength(7);

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_SKILL, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_AUTOBOTS, r.winner);
    }

    @Test
    public void faceoff_type_skill_autobots_b()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("A");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("D");
        b.setSkill(Utils.randomInt(1,4));
        b.setCourage(9);
        b.setStrength(7);

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_SKILL, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_AUTOBOTS, r.winner);
    }

    @Test
    public void faceoff_type_skill_decepticons_a()
    {
        TransformerModel a = TransformerModel.randomTransformer("D");
        a.setName("D");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("A");
        b.setSkill(Utils.randomInt(1,4));
        b.setCourage(9);
        b.setStrength(7);

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_SKILL, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_DECEPTICONS, r.winner);
    }

    @Test
    public void faceoff_type_skill_decepticons_b()
    {
        TransformerModel a = TransformerModel.randomTransformer("D");
        a.setName("D");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("A");
        b.setSkill(Utils.randomInt(1,4));
        b.setCourage(9);
        b.setStrength(7);

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_SKILL, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_DECEPTICONS, r.winner);
    }

    @Test
    public void faceoff_type_not_skill()
    {
        TransformerModel a = TransformerModel.randomTransformer("D");
        a.setName("D");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("A");
        b.setSkill(Utils.randomInt(5,9));
        b.setCourage(9);
        b.setStrength(7);

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertNotEquals(FaceoffResult.FACEOFF_RESULT_TYPE_SKILL, r.type);
    }

    @Test
    public void faceoff_type_overall_autobots_a()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("A");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        a.setIntelligence(7);
        a.setSpeed(7);
        a.setEndurance(7);
        a.setFirepower(8);

        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("D");
        b.setSkill(7);
        b.setCourage(9);

        b.setStrength(7);
        b.setIntelligence(7);
        b.setSpeed(7);
        b.setEndurance(7);
        b.setFirepower(7);

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_AUTOBOTS, r.winner);
    }

    @Test
    public void faceoff_type_overall_autobots_b()
    {
        TransformerModel a = TransformerModel.randomTransformer("A");
        a.setName("A");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        a.setIntelligence(7);
        a.setSpeed(7);
        a.setEndurance(7);
        a.setFirepower(8);

        TransformerModel b = TransformerModel.randomTransformer("D");
        b.setName("D");
        b.setSkill(7);
        b.setCourage(9);

        b.setStrength(7);
        b.setIntelligence(7);
        b.setSpeed(7);
        b.setEndurance(7);
        b.setFirepower(7);

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_AUTOBOTS, r.winner);
    }

    @Test
    public void faceoff_type_overall_decepticons_a()
    {
        TransformerModel a = TransformerModel.randomTransformer("D");
        a.setName("D");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        a.setIntelligence(7);
        a.setSpeed(7);
        a.setEndurance(7);
        a.setFirepower(8);

        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("A");
        b.setSkill(7);
        b.setCourage(9);

        b.setStrength(7);
        b.setIntelligence(7);
        b.setSpeed(7);
        b.setEndurance(7);
        b.setFirepower(7);

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_DECEPTICONS, r.winner);
    }

    @Test
    public void faceoff_type_overall_decepticons_b()
    {
        TransformerModel a = TransformerModel.randomTransformer("D");
        a.setName("D");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        a.setIntelligence(7);
        a.setSpeed(7);
        a.setEndurance(7);
        a.setFirepower(8);

        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("A");
        b.setSkill(7);
        b.setCourage(9);

        b.setStrength(7);
        b.setIntelligence(7);
        b.setSpeed(7);
        b.setEndurance(7);
        b.setFirepower(7);

        FaceoffResult r = battleController.faceoff(b, a);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_DECEPTICONS, r.winner);
    }

    @Test
    public void faceoff_type_overall_tie()
    {
        TransformerModel a = TransformerModel.randomTransformer("D");
        a.setName("D");
        a.setSkill(7);
        a.setCourage(9);
        a.setStrength(7);
        a.setIntelligence(7);
        a.setSpeed(7);
        a.setEndurance(7);
        a.setFirepower(7);

        TransformerModel b = TransformerModel.randomTransformer("A");
        b.setName("A");
        b.setSkill(7);
        b.setCourage(9);

        b.setStrength(7);
        b.setIntelligence(7);
        b.setSpeed(7);
        b.setEndurance(7);
        b.setFirepower(7);

        FaceoffResult r = battleController.faceoff(a, b);

        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL, r.type);
        Assert.assertEquals(FaceoffResult.FACEOFF_RESULT_TIE, r.winner);
    }


}
