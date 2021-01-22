package com.purush.productListing.data.repository;

import com.purush.productListing.data.schemas.Root;
import com.purush.productListing.networking.ProductApi;

import androidx.annotation.NonNull;
import retrofit2.Call;

public class DefaultProductRepository implements IProductRepository
{

    private final ProductApi mProductApi;

    public DefaultProductRepository( @NonNull ProductApi productApi )
    {
        mProductApi = productApi;
    }

    @Override
    public Call< Root > getProducts( int pageNo, int limit )
    {
        return mProductApi.getProducts( pageNo, limit );
    }
}
