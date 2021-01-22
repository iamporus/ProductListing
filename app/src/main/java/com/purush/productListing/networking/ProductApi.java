package com.purush.productListing.networking;

import com.purush.productListing.data.schemas.Root;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApi
{
    @GET( "products/" )
    Call< Root > getProducts( @Query( "page" ) int pageNo, @Query( "limit" ) int limit );

}
