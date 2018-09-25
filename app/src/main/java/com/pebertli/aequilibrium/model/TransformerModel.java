/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 1:10 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/24/18 12:08 AM
 *
 */

package com.pebertli.aequilibrium.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pebertli.aequilibrium.utils.Utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Bean for Transformer
 */
public class TransformerModel implements Serializable, Comparable<TransformerModel>, Cloneable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("team")
    @Expose
    private String team;
    @SerializedName("strength")
    @Expose
    private int strength;
    @SerializedName("intelligence")
    @Expose
    private int intelligence;
    @SerializedName("speed")
    @Expose
    private int speed;
    @SerializedName("endurance")
    @Expose
    private int endurance;
    @SerializedName("rank")
    @Expose
    private int rank;
    @SerializedName("courage")
    @Expose
    private int courage;
    @SerializedName("firepower")
    @Expose
    private int firepower;
    @SerializedName("skill")
    @Expose
    private int skill;
    @SerializedName("team_icon")
    @Expose
    private String team_icon;

    public TransformerModel()
    {

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTeam()
    {
        return team;
    }

    public void setTeam(String team)
    {
        this.team = team;
    }

    public int getStrength()
    {
        return strength;
    }

    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    public int getIntelligence()
    {
        return intelligence;
    }

    public void setIntelligence(int intelligence)
    {
        this.intelligence = intelligence;
    }

    public int getSpeed()
    {
        return speed;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public int getEndurance()
    {
        return endurance;
    }

    public void setEndurance(int endurance)
    {
        this.endurance = endurance;
    }

    public int getRank()
    {
        return rank;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public int getCourage()
    {
        return courage;
    }

    public void setCourage(int courage)
    {
        this.courage = courage;
    }

    public int getFirepower()
    {
        return firepower;
    }

    public void setFirepower(int firepower)
    {
        this.firepower = firepower;
    }

    public int getSkill()
    {
        return skill;
    }

    public void setSkill(int skill)
    {
        this.skill = skill;
    }

    public String getTeam_icon()
    {
        return team_icon;
    }

    public void setTeam_icon(String team_icon)
    {
        this.team_icon = team_icon;
    }

    /**
     * generate a random values for this transformer
     *
     */
    public void randomizeMe(boolean randomTeam)
    {
        if(randomTeam)
        {
            int i = Utils.randomInt(0, 1);
            if (i == 0)
            {
                setTeam("A");
            } else
                setTeam("D");
        }

        setName(Utils.randomString());
        setStrength(Utils.randomInt(1,10));
        setIntelligence(Utils.randomInt(1,10));
        setSpeed(Utils.randomInt(1,10));
        setEndurance(Utils.randomInt(1,10));
        setRank(Utils.randomInt(1,10));
        setCourage(Utils.randomInt(1,10));
        setFirepower(Utils.randomInt(1,10));
        setSkill(Utils.randomInt(1,10));
    }

    /**
     * generate a random transformer from a specific team, inside the requirements
     *
     */
    public static TransformerModel randomTransformer(String team)
    {
        TransformerModel result = new TransformerModel();

        result.setName(Utils.randomString());
        result.setTeam(team);
        result.setStrength(Utils.randomInt(1,10));
        result.setIntelligence(Utils.randomInt(1,10));
        result.setSpeed(Utils.randomInt(1,10));
        result.setEndurance(Utils.randomInt(1,10));
        result.setRank(Utils.randomInt(1,10));
        result.setCourage(Utils.randomInt(1,10));
        result.setFirepower(Utils.randomInt(1,10));
        result.setSkill(Utils.randomInt(1,10));


        return result;
    }

    /**
     * generate a transformer with minimum values
     *
     */
    public static TransformerModel weakTransformer(String team)
    {
        TransformerModel result = new TransformerModel();

        result.setName("");
        result.setTeam(team);
        result.setStrength(1);
        result.setIntelligence(1);
        result.setSpeed(1);
        result.setEndurance(1);
        result.setRank(1);
        result.setCourage(1);
        result.setFirepower(1);
        result.setSkill(1);

        return result;
    }

    /**
     * check if the criteria are inside the requirements
     *
     */
    public boolean isValid()
    {
        if( id == null)
            return false;
        if(name == null || name.isEmpty())
            return false;
        if(!team.equals("D") && !team.equals("A"))
            return false;
        if(!Utils.isIntBetween(strength, 1, 10))
            return false;
        if(!Utils.isIntBetween(intelligence, 1, 10))
            return false;
        if(!Utils.isIntBetween(speed, 1, 10))
            return false;
        if(!Utils.isIntBetween(endurance, 1, 10))
            return false;
        if(!Utils.isIntBetween(rank, 1, 10))
            return false;
        if(!Utils.isIntBetween(courage, 1, 10))
            return false;
        if(!Utils.isIntBetween(firepower, 1, 10))
            return false;
        if(!Utils.isIntBetween(skill, 1, 10))
            return false;

        return true;
    }

    /**
     * calculate the overall of this transformer
     *
     */
    public int getOverall()
    {
        return strength+intelligence+speed+endurance+firepower;
    }

    @Override
    public int compareTo(@NonNull TransformerModel model)
    {
        if(getRank() > model.getRank())
            return -1;
        else if(getRank() < model.getRank())
            return 1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransformerModel that = (TransformerModel) o;
        return strength == that.strength &&
                intelligence == that.intelligence &&
                speed == that.speed &&
                endurance == that.endurance &&
                rank == that.rank &&
                courage == that.courage &&
                firepower == that.firepower &&
                skill == that.skill &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(team, that.team) &&
                Objects.equals(team_icon, that.team_icon);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(id, name, team, strength, intelligence, speed, endurance, rank, courage, firepower, skill, team_icon);
    }

    public TransformerModel getClone() {
        try {
            // call clone in Object.
            return (TransformerModel) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println (" Cloning not allowed. " );
            return this;
        }
    }
}
