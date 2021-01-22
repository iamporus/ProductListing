package com.purush.productListing.utils;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

public class DebounceTextWatcher implements TextWatcher
{
    private final OnDebounceCompletedListener mListener;

    public interface OnDebounceCompletedListener
    {
        void onDebounceCompleted( String query );
    }

    public DebounceTextWatcher( @NonNull OnDebounceCompletedListener listener )
    {
        mListener = listener;
    }

    private Runnable runnable;
    private final Handler handler = new Handler();

    @Override
    public void beforeTextChanged( CharSequence s, int start, int count, int after )
    {

    }

    @Override
    public void onTextChanged( CharSequence s, int start, int before, int count )
    {
        handler.removeCallbacks( runnable );
    }

    @Override
    public void afterTextChanged( Editable s )
    {
        if( s.toString().trim().contentEquals( "<" )
                || s.toString().trim().contentEquals( ">" )
                || s.toString().trim().contentEquals( "=" ) || s.toString().trim().contentEquals( "price" ) )
        {
            runnable = () -> mListener.onDebounceCompleted( "" );
        }
        else
        {
            runnable = () -> mListener.onDebounceCompleted( s.toString().trim() );
        }
        handler.postDelayed( runnable, 300 );
    }

}
