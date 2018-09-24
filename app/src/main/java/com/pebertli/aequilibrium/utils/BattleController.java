/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 1:37 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/24/18 1:35 AM
 *
 */

package com.pebertli.aequilibrium.utils;

import com.pebertli.aequilibrium.model.TransformerModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Control faceoffs and the whole battle
 */
public class BattleController {

    public class BattleResult
    {
        public final static int BATTLE_RESULT_TIE = 0;
        public final static int BATTLE_RESULT_AUTOBOTS = 1;
        public final static int BATTLE_RESULT_DECEPTICONS = 2;
        public final static int BATTLE_RESULT_NUKE = 3;

        public int winner = 0;
        public int battleAmount = 0;
        public String message;
        public List<TransformerModel> autobotsSurvivor = new ArrayList<>();
        public List<TransformerModel> decepticonsSurvivor = new ArrayList<>();
    }

    public class FaceoffResult
    {

        public final static String TRANSFORMER_AUTOBOTS_STAR = "Optimus Prime";
        public final static String TRANSFORMER_DECEPTICONS_STAR = "Predaking";

        public final static int FACEOFF_RESULT_TIE = 0;
        public final static int FACEOFF_RESULT_AUTOBOTS = 1;
        public final static int FACEOFF_RESULT_DECEPTICONS = 2;

        public final static int FACEOFF_RESULT_TYPE_NUKE = 0;
        public final static int FACEOFF_RESULT_TYPE_OVERALL = 1;
        public final static int FACEOFF_RESULT_TYPE_RUN = 2;
        public final static int FACEOFF_RESULT_TYPE_SKILL = 3;
        public final static int FACEOFF_RESULT_TYPE_STAR = 4;


        public String message;
        public int winner;
        public int type;
        public boolean processed = false;

        public FaceoffResult(String message, int winner, int type)
        {
            this.message = message;
            this.winner = winner;
            this.type = type;
        }
    }

    private List<TransformerModel> mAutobots;
    private List<TransformerModel> mDecepticons;
    private int mIndexAutobots = 0;
    private int mIndexDecepticons = 0;
    private int mWins = 0;
    boolean mFinished = false;
    public BattleResult battleResult = new BattleResult();


    public BattleController(List<TransformerModel> mAutobots, List<TransformerModel> mDecepticons) {
        this.mAutobots = mAutobots;
        this.mDecepticons = mDecepticons;
    }

    /**
     * check the result of the next battle
     * if there is not a next battle, return null
     */
    public FaceoffResult nextFaceoff() {

        //there are some autobots or decepticons to battle each other?
        if (mIndexDecepticons < mAutobots.size() && mIndexDecepticons < mDecepticons.size()) {
            FaceoffResult result = faceoff(mAutobots.get(mIndexAutobots), mDecepticons.get(mIndexDecepticons));

            if (result.winner == FaceoffResult.FACEOFF_RESULT_AUTOBOTS) {
                mWins++;
                battleResult.autobotsSurvivor.add(mAutobots.get(mIndexAutobots));
                //I consider a run away a battle
                if (result.type == FaceoffResult.FACEOFF_RESULT_TYPE_RUN)
                    battleResult.decepticonsSurvivor.add(mDecepticons.get(mIndexDecepticons));

            } else if (result.winner == FaceoffResult.FACEOFF_RESULT_DECEPTICONS) {
                mWins--;
                battleResult.decepticonsSurvivor.add(mDecepticons.get(mIndexDecepticons));
                //I consider a run away a battle
                if (result.type == FaceoffResult.FACEOFF_RESULT_TYPE_RUN)
                    battleResult.autobotsSurvivor.add(mAutobots.get(mIndexAutobots));
            }

            //a nuke. No one survive
            if (result.winner == FaceoffResult.FACEOFF_RESULT_TIE && result.type == FaceoffResult.FACEOFF_RESULT_TYPE_NUKE) {
                mIndexAutobots = mAutobots.size();
                mIndexDecepticons = mDecepticons.size();
                battleResult.winner = BattleResult.BATTLE_RESULT_NUKE;
                battleResult.autobotsSurvivor.clear();
                battleResult.decepticonsSurvivor.clear();
            }

            ++mIndexAutobots;
            ++mIndexDecepticons;
            ++battleResult.battleAmount;

            return result;
        }

        //who didn't need to battle
        if(!mFinished)
        {
            for (int i = mIndexAutobots; i < mAutobots.size(); ++i)
            {
                battleResult.autobotsSurvivor.add(mAutobots.get(i));
            }

            for (int i = mIndexDecepticons; i < mDecepticons.size(); ++i)
            {
                battleResult.decepticonsSurvivor.add(mDecepticons.get(i));
            }

            mFinished = true;
        }

        //who won
        if(battleResult.winner != BattleResult.BATTLE_RESULT_NUKE)
        {
            if (mWins > 0)
                battleResult.winner = BattleResult.BATTLE_RESULT_AUTOBOTS;
            else if (mWins < 0)
                battleResult.winner = BattleResult.BATTLE_RESULT_DECEPTICONS;
            else
                battleResult.winner = BattleResult.BATTLE_RESULT_TIE;
        }


        battleResult.message = generateBattleResultMessage();

        return null;
    }

    /**
     * Return a fancy message for each battle, depending on the result, type of battle and fighters
     */
    public String generateBattleResultMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append(battleResult.battleAmount);

        if (battleResult.battleAmount > 1)
            builder.append(" battles");
        else
            builder.append(" battle");


        if (battleResult.winner == BattleResult.BATTLE_RESULT_TIE) {
            builder.append("\n");
            builder.append("Tied");
            builder.append("\n");
            builder.append("Survivors from Autobots: ");
            builder.append(survivors(BattleResult.BATTLE_RESULT_AUTOBOTS));
            builder.append("\n");
            builder.append("Survivors from Decepticons: ");
            builder.append(survivors(BattleResult.BATTLE_RESULT_DECEPTICONS));
        } else if (battleResult.winner == BattleResult.BATTLE_RESULT_NUKE) {
            builder.append("\nNuke!!\nNo survivors");
        } else {

            if (battleResult.winner == BattleResult.BATTLE_RESULT_AUTOBOTS) {
                builder.append("\n");
                builder.append("Winning team (Autobots): ");
                builder.append(survivors(BattleResult.BATTLE_RESULT_AUTOBOTS));
                builder.append("\n");
                builder.append("Survivors from the losing team (Decepticons): ");
                builder.append(survivors(BattleResult.BATTLE_RESULT_DECEPTICONS));

            } else {
                builder.append("\n");
                builder.append("Winning team (Decepticons): ");
                builder.append(survivors(BattleResult.BATTLE_RESULT_DECEPTICONS));
                builder.append("\n");
                builder.append("Survivors from the losing team (Autobots): ");
                builder.append(survivors(BattleResult.BATTLE_RESULT_AUTOBOTS));
            }
        }

        return builder.toString();
    }

    /**
     * Generate a readable list of survivors of the specified team
     */
    private String survivors(int team) {
        StringBuilder builder = new StringBuilder();

        if (team == BattleResult.BATTLE_RESULT_AUTOBOTS) {
            for (int i = 0; i < battleResult.autobotsSurvivor.size(); ++i) {
                TransformerModel t = battleResult.autobotsSurvivor.get(i);
                builder.append(t.getName());
                if (i < battleResult.autobotsSurvivor.size() - 1)
                    builder.append(", ");
            }
        } else if (team == BattleResult.BATTLE_RESULT_DECEPTICONS) {
            for (int i = 0; i < battleResult.decepticonsSurvivor.size(); ++i) {
                TransformerModel t = battleResult.decepticonsSurvivor.get(i);
                builder.append(t.getName());
                if (i < battleResult.decepticonsSurvivor.size() - 1)
                    builder.append(", ");
            }
        }

        return builder.toString();
    }

    /**
     * who should win on each battle?
     * based on the rules from the assigment
     */
    public FaceoffResult faceoff(TransformerModel a, TransformerModel d) {
        //Special Rules
        //nuke
        //duplicate of each other means that a predaking can be an autobot
        if ((a.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_AUTOBOTS_STAR) || a.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_DECEPTICONS_STAR))
                && (d.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_AUTOBOTS_STAR) || d.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_DECEPTICONS_STAR))) {
            return new FaceoffResult("Nuke!!!!!", FaceoffResult.FACEOFF_RESULT_TIE, FaceoffResult.FACEOFF_RESULT_TYPE_NUKE);
        }
        //Optimus Prime
        if ((a.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_AUTOBOTS_STAR) &&
                !d.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_DECEPTICONS_STAR))
                || (d.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_AUTOBOTS_STAR) &&
                !a.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_DECEPTICONS_STAR))) {
            return new FaceoffResult("Optimus Prime is the best!", FaceoffResult.FACEOFF_RESULT_AUTOBOTS, FaceoffResult.FACEOFF_RESULT_TYPE_STAR);
        }
        //Predaking
        if ((a.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_DECEPTICONS_STAR) &&
                !d.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_AUTOBOTS_STAR))
                || (d.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_DECEPTICONS_STAR) &&
                !a.getName().equalsIgnoreCase(FaceoffResult.TRANSFORMER_AUTOBOTS_STAR))) {
            return new FaceoffResult("Predaking crush!", FaceoffResult.FACEOFF_RESULT_DECEPTICONS, FaceoffResult.FACEOFF_RESULT_TYPE_STAR);
        }

        //rule run away
        int deltaCourage = a.getCourage() - d.getCourage();
        int deltaStrength = a.getStrength() - d.getStrength();
        if (deltaCourage >= 4 && deltaStrength >= 3) {
            if (a.getTeam().equalsIgnoreCase("A"))
                return new FaceoffResult(a.getName() + " Run away!", FaceoffResult.FACEOFF_RESULT_AUTOBOTS, FaceoffResult.FACEOFF_RESULT_TYPE_RUN);
            else if (a.getTeam().equalsIgnoreCase("D"))
                return new FaceoffResult(a.getName() + " Run away!", FaceoffResult.FACEOFF_RESULT_DECEPTICONS, FaceoffResult.FACEOFF_RESULT_TYPE_RUN);
        } else if (deltaCourage <= -4 && deltaStrength <= -3) {
            if (d.getTeam().equalsIgnoreCase("D"))
                return new FaceoffResult(d.getName() + " Run away!", FaceoffResult.FACEOFF_RESULT_DECEPTICONS, FaceoffResult.FACEOFF_RESULT_TYPE_RUN);
            if (d.getTeam().equalsIgnoreCase("A"))
                return new FaceoffResult(d.getName() + " Run away!", FaceoffResult.FACEOFF_RESULT_AUTOBOTS, FaceoffResult.FACEOFF_RESULT_TYPE_RUN);
        }

        //rule skill win
        int deltaSkill = a.getSkill() - d.getSkill();
        if (Math.abs(deltaSkill) >= 3) {
            if (deltaSkill > 0) {
                if (a.getTeam().equalsIgnoreCase("A"))
                    return new FaceoffResult(a.getName() + " won by skill!", FaceoffResult.FACEOFF_RESULT_AUTOBOTS, FaceoffResult.FACEOFF_RESULT_TYPE_SKILL);
                else if (a.getTeam().equalsIgnoreCase("D"))
                    return new FaceoffResult(a.getName() + " won by skill!", FaceoffResult.FACEOFF_RESULT_DECEPTICONS, FaceoffResult.FACEOFF_RESULT_TYPE_SKILL);
            } else {
                if (d.getTeam().equalsIgnoreCase("A"))
                    return new FaceoffResult(d.getName() + " won by skill!", FaceoffResult.FACEOFF_RESULT_AUTOBOTS, FaceoffResult.FACEOFF_RESULT_TYPE_SKILL);
                else if (d.getTeam().equalsIgnoreCase("D"))
                    return new FaceoffResult(d.getName() + " won by skill!", FaceoffResult.FACEOFF_RESULT_DECEPTICONS, FaceoffResult.FACEOFF_RESULT_TYPE_SKILL);


            }
        }

        //main rulle
        int deltaOverall = a.getOverall() - d.getOverall();
        if (deltaOverall > 0) {
            if (a.getTeam().equalsIgnoreCase("A"))
                return new FaceoffResult(a.getName() + " won by overall!", FaceoffResult.FACEOFF_RESULT_AUTOBOTS, FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL);
            else if (a.getTeam().equalsIgnoreCase("D"))
                return new FaceoffResult(a.getName() + " won by overall!", FaceoffResult.FACEOFF_RESULT_DECEPTICONS, FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL);
        } else if (deltaOverall < 0) {
            if (d.getTeam().equalsIgnoreCase("A"))
                return new FaceoffResult(d.getName() + " won by overall!", FaceoffResult.FACEOFF_RESULT_AUTOBOTS, FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL);
            if (d.getTeam().equalsIgnoreCase("D"))
                return new FaceoffResult(d.getName() + " won by overall!", FaceoffResult.FACEOFF_RESULT_DECEPTICONS, FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL);
        } else
            return new FaceoffResult("Tied", FaceoffResult.FACEOFF_RESULT_TIE, FaceoffResult.FACEOFF_RESULT_TYPE_OVERALL);

        return null;
    }
}
