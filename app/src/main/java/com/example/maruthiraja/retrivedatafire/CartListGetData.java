package com.example.maruthiraja.retrivedatafire;


public class CartListGetData {
    private String imageid;
    private String itemid;
    private String itemname;
    private String itemprice;

    public CartListGetData()
    {

    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public CartListGetData(String imageid, String itemid, String itemname, String itemprice) {

        this.imageid = imageid;
        this.itemid = itemid;
        this.itemname = itemname;
        this.itemprice = itemprice;
    }
}
