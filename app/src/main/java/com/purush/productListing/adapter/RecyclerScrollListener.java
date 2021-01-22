package com.purush.productListing.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Not really deprecated.
 *
 * This is not used in current implementation.
 * The current implementation pulls the entire listing in one shot, allowing the end user to perform
 * searching on entire product listing.
 *
 * This has been only added to showcase the capability on how we should handle pagination on a recyclerview using
 * user scroll position
 */
@SuppressWarnings( "FieldCanBeLocal" )
@Deprecated
public class RecyclerScrollListener extends RecyclerView.OnScrollListener
{
    private final OnReachedEndOfListListener mListener;

    public interface OnReachedEndOfListListener
    {
        void onReachedEndOfList();
    }

    public RecyclerScrollListener( @NonNull OnReachedEndOfListListener listener )
    {
        mListener = listener;
    }

    @Override
    public void onScrollStateChanged( @NonNull RecyclerView recyclerView, int newState )
    {
        super.onScrollStateChanged( recyclerView, newState );
    }

    @Override
    public void onScrolled( @NonNull RecyclerView recyclerView, int dx, int dy )
    {
        super.onScrolled( recyclerView, dx, dy );
        LinearLayoutManager linearLayoutManager = ( LinearLayoutManager ) recyclerView.getLayoutManager();
        if( linearLayoutManager != null )
        {
            int visibleItems = linearLayoutManager.getChildCount();
            int totalItems = linearLayoutManager.getItemCount();
            int firstVisible = linearLayoutManager.findFirstVisibleItemPosition();

            if( firstVisible + visibleItems >= totalItems && firstVisible != 0 )
            {
                //TODO: uncomment this to enable pagination
                //mListener.onReachedEndOfList();
            }
        }
    }

}
