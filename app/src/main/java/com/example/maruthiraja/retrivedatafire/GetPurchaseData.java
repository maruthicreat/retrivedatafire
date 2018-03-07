package com.example.maruthiraja.retrivedatafire;


public class GetPurchaseData {
    private String amountpayable;
    private String ispayed;
    private String itemid;
    private String paymentMode;
    private String itemimage;
    private String itemname;
    private String purchaseTime;

    public GetPurchaseData()
    {
    }

    public GetPurchaseData(String amountpayable, String ispayed, String itemid, String paymentMode, String itemimage, String itemname, String purchaseTime, String itemrating) {
        this.amountpayable = amountpayable;
        this.ispayed = ispayed;
        this.itemid = itemid;
        this.paymentMode = paymentMode;
        this.itemimage = itemimage;
        this.itemname = itemname;
        this.purchaseTime = purchaseTime;
        this.itemrating = itemrating;
    }

    public String getItemrating() {
        return itemrating;
    }

    public void setItemrating(String itemrating) {
        this.itemrating = itemrating;
    }

    private String itemrating;

    public String getAmountpayable() {
        return amountpayable;
    }

    public void setAmountpayable(String amountpayable) {
        this.amountpayable = amountpayable;
    }

    public String getIspayed() {
        return ispayed;
    }

    public void setIspayed(String ispayed) {
        this.ispayed = ispayed;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getItemimage() {
        return itemimage;
    }

    public void setItemimage(String itemimage) {
        this.itemimage = itemimage;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }



    public GetPurchaseData(String amountpayable, String ispayed, String itemid, String paymentMode, String itemimage, String itemname, String purchaseTime) {
        this.amountpayable = amountpayable;
        this.ispayed = ispayed;
        this.itemid = itemid;
        this.paymentMode = paymentMode;
        this.itemimage = itemimage;
        this.itemname = itemname;
        this.purchaseTime = purchaseTime;
    }
}
