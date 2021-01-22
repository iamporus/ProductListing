package com.purush.productListing.ui;

import android.util.Log;

import com.purush.productListing.data.repository.IProductRepository;
import com.purush.productListing.data.schemas.Datum;
import com.purush.productListing.data.schemas.Root;
import com.purush.productListing.utils.FilterUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;

import static com.purush.productListing.networking.Constants.MAX_RETRY_COUNT;
import static com.purush.productListing.utils.FilterUtils.applyComparativeFilter;
import static com.purush.productListing.utils.FilterUtils.applyPriceAndNameFilter;
import static com.purush.productListing.utils.FilterUtils.applyPriceFilter;
import static com.purush.productListing.utils.FilterUtils.isMultiValued;
import static com.purush.productListing.utils.FilterUtils.isNumber;
import static com.purush.productListing.utils.FilterUtils.isText;

public class ProductViewModel extends ViewModel
{
    private final IProductRepository mProductRepository;
    private final MutableLiveData< List< Datum > > mProductsMutableList;
    private final List< Datum > mProductsList;
    private static final String TAG = "ProductViewModel";

    public LiveData< List< Datum > > getProductsList()
    {
        return mProductsMutableList;
    }

    public ProductViewModel( IProductRepository productRepository )
    {
        mProductsList = new ArrayList<>();
        mProductsMutableList = new MutableLiveData<>();
        mProductRepository = productRepository;

        fetchProducts();
    }

    public void fetchProducts()
    {
        new Thread( () -> {

            try
            {
                int total = 0;
                int maxPages = 0;
                int currentPage = 0;
                Call< Root > call = mProductRepository.getProducts( currentPage++, 20 );
                Root products = call.execute().body();

                if( products != null )
                {
                    mProductsList.clear();
                    mProductsList.addAll( products.getData() );

                    total = products.getMeta().getPagination().getTotal();
                    maxPages = products.getMeta().getPagination().getPages();
                }

                int retryCounter = 0;

                while( mProductsList.size() != total || retryCounter != MAX_RETRY_COUNT || maxPages != currentPage )
                {
                    try
                    {
                        call = mProductRepository.getProducts( currentPage++, 20 );
                        products = call.execute().body();
                        if( products != null )
                        {
                            mProductsList.addAll( products.getData() );
                        }
                        retryCounter = 0;
                        if( mProductsList.size() == total || maxPages == currentPage )
                        {
                            break;
                        }
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                        retryCounter++;
                    }
                }

                mProductsMutableList.postValue( mProductsList );

            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }

        } ).start();
    }

    /**
     * Not really Deprecated.
     * <p>
     * Used to showcase the functionality of implementing pagination
     */
    @SuppressWarnings( "DeprecatedIsStillUsed" )
    @Deprecated
    public void fetchNextFewProducts()
    {
        // increment currentPage number and hit the API
    }

    /**
     * Gets invoked once user has stopped typing in edit text.
     *
     * @param query user entered text
     */
    public void onDebounceCompleted( String query )
    {
        Log.d( TAG, "onDebounceCompleted() called with: query = [" + query + "]" );

        //TODO: future implementation should extract out an enum for all filter types and
        // work with the specific enum values instead of if-else nesting.

        if( query.contains( "," ) )
        {
            //price and name filter comma separated
            //eg.
            // Boots, 5000
            // 4500, knife
            List< Datum > filteredList = new ArrayList<>();
            String[] token = query.split( "," );
            if( token.length == 2 )
            {
                if( isNumber( token[ 0 ].trim() ) )
                {
                    if( isText( token[ 1 ].trim() ) )
                    {
                        filteredList = applyPriceAndNameFilter( token[ 1 ], token[ 0 ], mProductsList );
                    }
                }
                else if( isNumber( token[ 1 ].trim() ) )
                {
                    if( isText( token[ 0 ].trim() ) )
                    {
                        filteredList = applyPriceAndNameFilter( token[ 0 ], token[ 1 ], mProductsList );
                    }
                }
            }
            mProductsMutableList.postValue( filteredList );
        }
        else if( FilterUtils.isPrice( query ) )
        {
            //price
            //eg.
            // 5234
            // 6777.9

            List< Datum > filteredList = applyPriceFilter( query, mProductsList );
            mProductsMutableList.postValue( filteredList );
        }
        else if( isMultiValued( query ) )
        {
            String[] token = query.split( " " );
            List< Datum > filteredList;

            if( token.length == 2 )
            {
                // ><= price filter
                // eg.
                // > 10000
                // < 24244

                String price = token[ 1 ];
                String operation = token[ 0 ];
                filteredList = applyComparativeFilter( price, operation, mProductsList );
            }
            else if( token.length == 3 )
            {
                // price ><= number filter
                // eg.
                // price > 10000
                // price < 24244

                String price = token[ 2 ];
                String operation = token[ 1 ];
                filteredList = applyComparativeFilter( price, operation, mProductsList );
            }
            else if( query.startsWith( "price" ) || query.startsWith( "Price" ) )
            {
                // price><=number filter (same as above, without spaces)
                // eg.
                // price>10000
                // price<24244

                String price = query.substring( 6 );
                if( price.isEmpty() )
                {
                    price = "0.0";
                }
                String operation = query.substring( 5, 6 );
                filteredList = applyComparativeFilter( price, operation, mProductsList );
            }
            else
            {
                // ><=price
                // eg.
                // >43300.0
                // <2342.4

                String operation = query.substring( 0, 1 );
                String price = query.substring( 1 );
                filteredList = applyComparativeFilter( price, operation, mProductsList );
            }

            mProductsMutableList.postValue( filteredList );
        }
        else
        {
            List< Datum > filteredList = mProductsList.stream()
                    .filter( datum -> datum.getName().toLowerCase().contains( query.toLowerCase() ) )
                    .collect( Collectors.toList() );

            mProductsMutableList.postValue( filteredList );
        }

    }

}
