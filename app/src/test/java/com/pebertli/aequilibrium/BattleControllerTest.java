package com.pebertli.aequilibrium;

import com.pebertli.aequilibrium.model.TransformerModel;
import com.pebertli.aequilibrium.utils.BattleController;
import com.pebertli.aequilibrium.utils.BattleController.BattleResult;
import com.pebertli.aequilibrium.utils.BattleController.FaceoffResult;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleControllerTest {

    @Test
    public void battle_nuke()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(0).setName("optimus prime");
        autobots.get(0).setRank(7);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.get(0).setName("predaking");
        decepticons.get(0).setRank(7);

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(BattleResult.BATTLE_RESULT_NUKE, br.winner);
    }

    @Test
    public void battle_not_nuke()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(0).setName("optimus prime");
        autobots.get(0).setRank(7);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.get(0).setName("d");
        decepticons.get(1).setRank(7);
        decepticons.get(0).setName("predaking");
        decepticons.get(0).setRank(6);

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertNotEquals(BattleResult.BATTLE_RESULT_NUKE, br.winner);
    }

    @Test
    public void battle_tie()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(BattleResult.BATTLE_RESULT_TIE, br.winner);
    }

    @Test
    public void battle_not_tie()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(0).setFirepower(7);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertNotEquals(BattleResult.BATTLE_RESULT_TIE, br.winner);
    }

    @Test
    public void battle_autobots_win_a()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(0).setFirepower(7);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(BattleResult.BATTLE_RESULT_AUTOBOTS, br.winner);
    }

    @Test
    public void battle_decepticons_win_a()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.get(0).setFirepower(7);

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(BattleResult.BATTLE_RESULT_DECEPTICONS, br.winner);
    }

    @Test
    public void battle_battles_a()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(4, br.battleAmount);
    }

    @Test
    public void battle_battles_b()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(3, br.battleAmount);
    }

    @Test
    public void battle_battles_c()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(0).setCourage(9);
        autobots.get(0).setStrength(9);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.get(0).setCourage(1);
        decepticons.get(0).setStrength(1);

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(4, br.battleAmount);
    }

    @Test
    public void battle_battles_d()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(1).setName("A");
        autobots.get(1).setRank(9);
        autobots.get(0).setName("optimus prime");
        autobots.get(0).setRank(7);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.get(1).setName("D");
        decepticons.get(1).setRank(9);
        decepticons.get(0).setName("predaking");
        decepticons.get(0).setRank(7);

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(2, br.battleAmount);
    }

    @Test
    public void battle_battles_e()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(0).setName("optimus prime");
        autobots.get(0).setRank(7);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.get(0).setName("predaking");
        decepticons.get(0).setRank(7);

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(1, br.battleAmount);
    }

    @Test
    public void battle_survivors_a()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(1).setName("A");
        autobots.get(1).setRank(9);
        autobots.get(0).setName("optimus prime");
        autobots.get(0).setRank(7);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.get(1).setName("D");
        decepticons.get(1).setRank(9);
        decepticons.get(0).setName("predaking");
        decepticons.get(0).setRank(7);

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(0, br.autobotsSurvivor.size());
        Assert.assertEquals(0, br.decepticonsSurvivor.size());
    }

    @Test
    public void battle_survivors_b()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(1).setName("A");
        autobots.get(1).setFirepower(9);

        autobots.get(0).setName("B");
        autobots.get(0).setFirepower(1);
        autobots.get(0).setRank(9);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.get(1).setName("C");
        decepticons.get(1).setFirepower(9);
        decepticons.get(1).setRank(9);

        decepticons.get(0).setName("D");
        decepticons.get(0).setFirepower(1);

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(1, br.autobotsSurvivor.size());
        Assert.assertEquals(3, br.decepticonsSurvivor.size());
    }

    @Test
    public void battle_survivors_c()
    {
        List<TransformerModel> autobots = new ArrayList<>();
        List<TransformerModel> decepticons = new ArrayList<>();

        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.add(TransformerModel.weakTransformer("A"));
        autobots.get(0).setName("A");
        autobots.get(0).setCourage(9);
        autobots.get(0).setStrength(9);
        autobots.get(0).setRank(9);

        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.add(TransformerModel.weakTransformer("D"));
        decepticons.get(0).setName("C");
        decepticons.get(0).setCourage(4);
        decepticons.get(0).setStrength(4);
        decepticons.get(0).setRank(9);

        Collections.sort(autobots);
        Collections.sort(decepticons);

        BattleController battleController = new BattleController(autobots, decepticons);

        FaceoffResult fr = battleController.nextFaceoff();
        while(fr != null)
        {
            fr = battleController.nextFaceoff();
        }

        BattleResult br = battleController.battleResult;

        Assert.assertEquals(1, br.autobotsSurvivor.size());
        Assert.assertEquals(3, br.decepticonsSurvivor.size());
    }
}
