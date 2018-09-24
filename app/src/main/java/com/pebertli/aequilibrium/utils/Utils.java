/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 1:38 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/24/18 1:29 AM
 *
 */

package com.pebertli.aequilibrium.utils;

import android.webkit.URLUtil;

import java.util.Random;

/**
 * set of helper functions
 */
public class Utils
{

    private final static String []sintaxTokens = {"A","BA","CE","DO","E","FI","GU","H","I","JA","K","LE","MI","NO","O","PU","Q","RA","SE","TA","U","VE","W","X","Y","ZO"};

    /*
    * True random int number between start and end, inclusive
    * */
    public static int randomInt(int start, int end)
    {
        Random r = new Random();

        return start+r.nextInt(end-start+1);
    }

    /**
     * A dumb random name generator
     */
    public static String randomString()
    {

        StringBuilder result = new StringBuilder();
        result.append(sintaxTokens[randomInt(0,sintaxTokens.length-1)]);
        result.append(sintaxTokens[randomInt(0,sintaxTokens.length-1)]);
        result.append(sintaxTokens[randomInt(0,sintaxTokens.length-1)]);

        return result.toString();
    }

    /**
     * check if a int value is between two int values, inclusive
     */
    public static boolean isIntBetween(int value, int start, int end)
    {
        if(value< start || value >end)
            return false;

        return true;
    }

}
