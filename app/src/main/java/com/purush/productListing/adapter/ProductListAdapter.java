package com.purush.productListing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purush.productListing.R;
import com.purush.productListing.data.schemas.Datum;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends ListAdapter< Datum, ProductListAdapter.ViewHolder >
{
    public ProductListAdapter( @NonNull ProductDiffUtil diffCallback )
    {
        super( diffCallback );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.product_list_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position )
    {
        Datum datum = getItem( position );

        holder.bind( datum );
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder( @NonNull View itemView )
        {
            super( itemView );
        }

        public void bind( Datum datum )
        {
            TextView nameTextView = itemView.findViewById( R.id.productNameTextView );
            TextView priceTextView = itemView.findViewById( R.id.productPriceTextView );
            nameTextView.setText( datum.getName() );
            priceTextView.setText( datum.getPrice() );
        }
    }
}
