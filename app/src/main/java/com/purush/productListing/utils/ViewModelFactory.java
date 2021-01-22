package com.purush.productListing.utils;

import com.purush.productListing.data.repository.IProductRepository;
import com.purush.productListing.ui.ProductViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

@SuppressWarnings( "unchecked" )
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory
{
    private final IProductRepository mProductRepository;

    public ViewModelFactory( @NonNull IProductRepository productRepository )
    {
        mProductRepository = productRepository;
    }

    @NonNull
    @Override
    public < T extends ViewModel > T create( @NonNull Class< T > modelClass )
    {
        if( modelClass == ProductViewModel.class )
        {
            return ( T ) new ProductViewModel( mProductRepository );
        }
        return super.create( modelClass );
    }
}
