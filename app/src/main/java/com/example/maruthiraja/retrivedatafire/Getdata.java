package com.example.maruthiraja.retrivedatafire;

/**
 * Created by MCA on 2/20/2018.
 */

public class Getdata {
    private String description;
    private String image;
    private String price;
    private String title;
    public Getdata()
    {

    }

    public Getdata(String description, String image, String price, String title) {

        this.description = description;
        this.image = image;
        this.price = price;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
