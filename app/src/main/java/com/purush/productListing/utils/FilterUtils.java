package com.purush.productListing.utils;

import com.purush.productListing.data.schemas.Datum;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;

public class FilterUtils
{
    public static boolean isPrice( String query )
    {
        return query.matches( "[0-9]+[.[0-9]+]*" );
    }

    public static boolean isNumber( String query )
    {
        return query.matches( "^[0-9]*$" );
    }

    public static boolean isText( String query )
    {
        return query.matches( "^[A-z]*$" );
    }

    public static boolean isMultiValued( String query )
    {
        return query.matches( "(price)?[ ]?[><=][ ]?[0-9]+[.[0-9]+]*" );
    }

    @NotNull
    public static List< Datum > applyPriceFilter( String query, List< Datum > datumList )
    {
        return datumList.stream()
                .filter( datum -> {
                    float price = Float.parseFloat( datum.getPrice() );
                    float queryPrice = Float.parseFloat( query );
                    return price > queryPrice;
                } ).sorted()
                .collect( Collectors.toList() );
    }

    @NonNull
    public static List< Datum > applyPriceAndNameFilter( String productNameQuery, String productPriceQuery, List< Datum > datumList )
    {
        return datumList.stream()
                .filter( datum -> datum.getName().toLowerCase().contains( productNameQuery ) )
                .filter( datum -> {
                    float price = Float.parseFloat( productPriceQuery );
                    float datumPrice = Float.parseFloat( datum.getPrice() );
                    return datumPrice > price;
                } ).sorted()
                .collect( Collectors.toList() );

    }

    @NotNull
    public static List< Datum > applyComparativeFilter( String enteredPrice, String operation, List< Datum > datumList )
    {
        return datumList.stream()
                .filter( datum -> {
                    float price = Float.parseFloat( datum.getPrice() );
                    float queryPrice = Float.parseFloat( enteredPrice );
                    if( operation.contentEquals( ">" ) )
                    {
                        return price > queryPrice;
                    }
                    else if( operation.contentEquals( "<" ) )
                    {
                        return price < queryPrice;
                    }
                    else
                        return price == queryPrice;

                } ).sorted()
                .collect( Collectors.toList() );
    }

    //TODO: create enum for different Filter types and pass that
    public enum FILTER_TYPE
    {
        PLAIN_PRICE,
        PRODUCT_NAME,
        PRICE_LT_GT_EQ_PRICE_VALUE
    }
}
