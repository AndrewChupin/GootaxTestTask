package com.example.barsik.gootax;

public class ListItem
{
    String address;
    double lat;
    double lang;

    ListItem()
    {

    }

    public String getAddress()
    {
        return address;
    }

    public double getLat()
    {
        return lat;
    }

    public double getLng()
    {
        return lang;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
    public void setLat(double lat)
    {
        this.lat = lat;
    }
    public void setLng (double lang)
    {
        this.lang = lang;
    }

}
