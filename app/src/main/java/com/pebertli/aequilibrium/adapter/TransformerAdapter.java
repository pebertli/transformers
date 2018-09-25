/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 12:41 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/23/18 10:58 PM
 *
 */

package com.pebertli.aequilibrium.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pebertli.aequilibrium.R;
import com.pebertli.aequilibrium.model.TransformerModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter Pattern for Recyclerview
 */
public class TransformerAdapter extends RecyclerView.Adapter<TransformerHolder>
{

    /**
     * Interface that dispatch callbacks when click on row and when click on delete button
     */
    public interface ItemClickListener{
        void deleteClicked(TransformerModel model, int itemIndex);
        void itemClicked(TransformerModel model, int itemIndex);

    }

    private List<TransformerModel> mItems;
    private Context mContext;
    private ItemClickListener mListener;

    public TransformerAdapter(Context context, List<TransformerModel> items)
    {
        mContext = context;
        mItems = items;
    }

    public void setItemClickListener(ItemClickListener mListener)
    {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public TransformerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.row_transformers, viewGroup, false);

        //I decided to put the icons on create, since their values (images) will not chance
        ((AppCompatImageView)view.findViewById(R.id.strenghtCriteria).findViewById(R.id.iconCriteria)).setImageResource(R.drawable.strength_icon);
        ((AppCompatImageView)view.findViewById(R.id.intelligenceCriteria).findViewById(R.id.iconCriteria)).setImageResource(R.drawable.intelligence_icon);
        ((AppCompatImageView)view.findViewById(R.id.speedCriteria).findViewById(R.id.iconCriteria)).setImageResource(R.drawable.speed_icon);
        ((AppCompatImageView)view.findViewById(R.id.enduranceCriteria).findViewById(R.id.iconCriteria)).setImageResource(R.drawable.endurance_icon);
        ((AppCompatImageView)view.findViewById(R.id.rankCriteria).findViewById(R.id.iconCriteria)).setImageResource(R.drawable.rank_icon);
        ((AppCompatImageView)view.findViewById(R.id.courageCriteria).findViewById(R.id.iconCriteria)).setImageResource(R.drawable.courage_icon);
        ((AppCompatImageView)view.findViewById(R.id.firepowerCriteria).findViewById(R.id.iconCriteria)).setImageResource(R.drawable.firepower_icon);
        ((AppCompatImageView)view.findViewById(R.id.skillCriteria).findViewById(R.id.iconCriteria)).setImageResource(R.drawable.skill_icon);

        return new TransformerHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final TransformerHolder viewHolder, final int i)
    {
        viewHolder.name.setText(mItems.get(i).getName());

        //using Picasso to async download the media
        //It's important to say that I am not using any UI part of the library, as transform
        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.build().load(mItems.get(i).getTeam_icon())
                .into(viewHolder.image);

        viewHolder.strength.setText(Integer.toString(mItems.get(i).getStrength()));
        viewHolder.intelligence.setText(Integer.toString(mItems.get(i).getIntelligence()));
        viewHolder.speed.setText(Integer.toString(mItems.get(i).getSpeed()));
        viewHolder.endurance.setText(Integer.toString(mItems.get(i).getEndurance()));
        viewHolder.rank.setText(Integer.toString(mItems.get(i).getRank()));
        viewHolder.courage.setText(Integer.toString(mItems.get(i).getCourage()));
        viewHolder.firepower.setText(Integer.toString(mItems.get(i).getFirepower()));
        viewHolder.skill.setText(Integer.toString(mItems.get(i).getSkill()));

        viewHolder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mListener != null)
                    mListener.deleteClicked(mItems.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mListener != null)
                    mListener.itemClicked(mItems.get(viewHolder.getAdapterPosition()),viewHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public void setItems(List<TransformerModel> items)
    {
        mItems = items;
        notifyDataSetChanged();
    }

    public List<TransformerModel> getItems()
    {
        return mItems;
    }

    public TransformerModel getItem(int position)
    {
        return mItems.get(position);
    }

    public void addItem(TransformerModel item)
    {
        mItems.add(0, item);
        notifyItemInserted(0);
    }

    public void removeItem(TransformerModel model)
    {
        removeItem(mItems.indexOf(model));
    }

    public void removeItem(int position)
    {
        mItems.remove(position);
        notifyItemRangeRemoved(position,1);
    }

    public void updateItem(TransformerModel originalModel, TransformerModel model)
    {
        updateItem(mItems.indexOf(originalModel), model);
    }

    public void updateItem(int position, TransformerModel model)
    {
        if(position >=0)
        {
            mItems.set(position, model);
            notifyItemChanged(position);
        }
    }
}

class TransformerHolder extends RecyclerView.ViewHolder
{

    ImageView image;
    TextView name;
    TextView strength;
    TextView intelligence;
    TextView endurance;
    TextView speed;
    TextView rank;
    TextView courage;
    TextView firepower;
    TextView skill;

    ImageButton delete;

    public TransformerHolder(@NonNull View itemView)
    {
        super(itemView);

        name = itemView.findViewById(R.id.transformerName);
        image = itemView.findViewById(R.id.transformerIcon);
        delete = itemView.findViewById(R.id.deleteButton);

        strength = itemView.findViewById(R.id.strenghtCriteria).findViewById(R.id.textCriteria);
        intelligence = itemView.findViewById(R.id.intelligenceCriteria).findViewById(R.id.textCriteria);
        endurance = itemView.findViewById(R.id.enduranceCriteria).findViewById(R.id.textCriteria);
        speed = itemView.findViewById(R.id.speedCriteria).findViewById(R.id.textCriteria);
        rank = itemView.findViewById(R.id.rankCriteria).findViewById(R.id.textCriteria);
        courage = itemView.findViewById(R.id.courageCriteria).findViewById(R.id.textCriteria);
        firepower = itemView.findViewById(R.id.firepowerCriteria).findViewById(R.id.textCriteria);
        skill = itemView.findViewById(R.id.skillCriteria).findViewById(R.id.textCriteria);

    }
}
