package com.purush.productListing.data.repository;

import com.purush.productListing.data.schemas.Root;

import retrofit2.Call;

public interface IProductRepository
{
    public Call<Root> getProducts( int pageNo, int limit );
}
