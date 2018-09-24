/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 1:10 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/23/18 11:24 PM
 *
 */

package com.pebertli.aequilibrium.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Bean for list of transformers
 */
public class TransformerListModel {

    @SerializedName("transformers")
    @Expose
    private List<TransformerModel> transformers = null;

    public List<TransformerModel> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<TransformerModel> transformers) {
        this.transformers = transformers;
    }

}