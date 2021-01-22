package com.purush.productListing.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.purush.productListing.ProductApplication;
import com.purush.productListing.R;
import com.purush.productListing.utils.ViewModelFactory;
import com.purush.productListing.adapter.ProductDiffUtil;
import com.purush.productListing.adapter.ProductListAdapter;
import com.purush.productListing.adapter.RecyclerScrollListener;
import com.purush.productListing.data.repository.IProductRepository;
import com.purush.productListing.utils.DebounceTextWatcher;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements RecyclerScrollListener.OnReachedEndOfListListener,
                                                               DebounceTextWatcher.OnDebounceCompletedListener
{
    private ProductListAdapter mProductListAdapter;
    private ProgressBar mProgressBar;
    private ProductViewModel mProductsViewModel;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        IProductRepository productRepository = ( ( ProductApplication ) getApplication() ).getProductRepository();
        ViewModelFactory viewModelFactory = new ViewModelFactory( productRepository );

        setupProductList();
        setupFilter();

        mProductsViewModel = new ViewModelProvider( this, viewModelFactory ).get( ProductViewModel.class );

        mProductsViewModel.getProductsList().observe( this, datumList -> {

            mProductListAdapter.submitList( new ArrayList<>( datumList ) );
            mProgressBar.setVisibility( View.INVISIBLE );

        } );

    }

    private void setupFilter()
    {
        EditText editText = findViewById( R.id.searchEditText );
        editText.addTextChangedListener( new DebounceTextWatcher( this ) );
    }

    private void setupProductList()
    {
        RecyclerView recyclerView = findViewById( R.id.recylerView );
        mProductListAdapter = new ProductListAdapter( new ProductDiffUtil() );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( linearLayoutManager );
        recyclerView.setAdapter( mProductListAdapter );

        mProgressBar = findViewById( R.id.progressBar );
        mProgressBar.setVisibility( View.VISIBLE );

        mProductListAdapter.registerAdapterDataObserver( new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onItemRangeChanged( int positionStart, int itemCount )
            {
                super.onItemRangeChanged( positionStart, itemCount );
                linearLayoutManager.scrollToPositionWithOffset( 0, 0 );
            }

            @Override
            public void onChanged()
            {
                super.onChanged();
                linearLayoutManager.scrollToPositionWithOffset( 0, 0 );
            }

            @Override
            public void onItemRangeRemoved( int positionStart, int itemCount )
            {
                super.onItemRangeRemoved( positionStart, itemCount );
                linearLayoutManager.scrollToPositionWithOffset( 0, 0 );
            }
        } );

        // Not really needed as we are pulling all the data in one go.
        // Just added here to showcase capability.
        // Read the javadoc of RecyclerScrollListener for further info.
        recyclerView.addOnScrollListener( new RecyclerScrollListener( this ) );
    }

    public void onReachedEndOfList()
    {
        mProductsViewModel.fetchNextFewProducts();
    }

    @Override
    public void onDebounceCompleted( String query )
    {
        mProductsViewModel.onDebounceCompleted( query );
    }
}