package com.purush.productListing;

import com.purush.productListing.data.repository.DefaultProductRepository;
import com.purush.productListing.data.repository.IProductRepository;
import com.purush.productListing.networking.ProductApi;
import com.purush.productListing.networking.ProductServiceGenerator;

/**
 * Having a service locator framework enables to separate the dependency construction logic from
 * the actual business logic.
 * The Application class helps creating a Dependency graph with the usage of this service locator.
 * All the dependencies should be created here and exposed through Application class to UI controller
 */
@SuppressWarnings( "InstantiationOfUtilityClass" )
public class ServiceLocator
{
    private volatile static ServiceLocator INSTANCE = new ServiceLocator();
    private static IProductRepository mProductRepository;

    private ServiceLocator()
    {
    }

    public IProductRepository provideProductRepository()
    {
        if( mProductRepository == null )
        {
            synchronized( this )
            {
                mProductRepository = getProductRepository();
            }
        }
        return mProductRepository;
    }

    private IProductRepository getProductRepository()
    {
        return new DefaultProductRepository( getProductApi() );
    }

    private ProductApi getProductApi()
    {
        return ProductServiceGenerator.createService( ProductApi.class );
    }

    public static ServiceLocator getInstance()
    {
        if( INSTANCE == null )
        {
            synchronized( ServiceLocator.class )
            {
                if( INSTANCE == null )
                {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }
}
