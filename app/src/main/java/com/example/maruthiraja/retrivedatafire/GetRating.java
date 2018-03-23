package com.example.maruthiraja.retrivedatafire;


public class GetRating {



    public GetRating()
    {

    }

    private String avgrat;
    private String comrate;
    private int rating;

    public String getAvgrat() {
        return avgrat;
    }

    public void setAvgrat(String avgrat) {
        this.avgrat = avgrat;
    }

    public String getComrate() {
        return comrate;
    }

    public void setComrate(String comrate) {
        this.comrate = comrate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public GetRating(String avgrat, String comrate, int rating, String review) {

        this.avgrat = avgrat;
        this.comrate = comrate;
        this.rating = rating;
        this.review = review;
    }

    private String review;
}
