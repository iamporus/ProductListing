package com.purush.productListing.data.schemas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class Datum implements Serializable, Comparable< Datum >
{
    @SerializedName( "id" )
    @Expose
    private Integer id;
    @SerializedName( "name" )
    @Expose
    private String name;
    @SerializedName( "description" )
    @Expose
    private String description;
    @SerializedName( "image" )
    @Expose
    private String image;
    @SerializedName( "price" )
    @Expose
    private String price;
    @SerializedName( "discount_amount" )
    @Expose
    private String discountAmount;
    @SerializedName( "status" )
    @Expose
    private Boolean status;
    @SerializedName( "categories" )
    @Expose
    private List< Category > categories = null;

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage( String image )
    {
        this.image = image;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice( String price )
    {
        this.price = price;
    }

    public String getDiscountAmount()
    {
        return discountAmount;
    }

    public void setDiscountAmount( String discountAmount )
    {
        this.discountAmount = discountAmount;
    }

    public Boolean getStatus()
    {
        return status;
    }

    public void setStatus( Boolean status )
    {
        this.status = status;
    }

    public List< Category > getCategories()
    {
        return categories;
    }

    public void setCategories( List< Category > categories )
    {
        this.categories = categories;
    }

    @Override
    public String toString()
    {
        return "Datum{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price='" + price + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", status=" + status +
                ", categories=" + categories +
                '}';
    }

    @Override
    public int compareTo( @NotNull Datum datum )
    {
        float price = Float.parseFloat( getPrice() );
        float newPrice = Float.parseFloat( datum.getPrice() );
        if( newPrice < price )
            return 1;
        else if( newPrice > price )
            return -1;
        return 0;
    }
}
