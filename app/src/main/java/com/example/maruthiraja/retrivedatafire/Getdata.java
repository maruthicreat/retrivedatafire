package com.example.maruthiraja.retrivedatafire;

/**
 * Created by MCA on 2/20/2018.
 */

public class Getdata {
    private String description;
    private String image;
    private String price;
    private String title;
    private String rating;
    private String review;
    private String quantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Getdata()
    {

    }

    public Getdata(String description, String image, String price,String quantity, String title) {

        this.description = description;
        this.image = image;
        this.price = price;
        this.title = title;
        this.quantity = quantity;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
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
