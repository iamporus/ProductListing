package com.purush.productListing.adapter;

import com.purush.productListing.data.schemas.Datum;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ProductDiffUtil extends DiffUtil.ItemCallback< Datum >
{
    @Override
    public boolean areItemsTheSame( @NonNull Datum oldItem, @NonNull Datum newItem )
    {
        return oldItem.getId().equals( newItem.getId() ) && oldItem.getPrice().equals( newItem.getPrice() );
    }

    @Override
    public boolean areContentsTheSame( @NonNull Datum oldItem, @NonNull Datum newItem )
    {
        return oldItem.getDescription().contentEquals( newItem.getDescription() );
    }
}
