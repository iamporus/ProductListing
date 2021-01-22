package com.purush.productListing;

import android.app.Application;

import com.purush.productListing.data.repository.IProductRepository;

public class ProductApplication extends Application
{
    public IProductRepository getProductRepository()
    {
        return ServiceLocator.getInstance().provideProductRepository();
    }
}
