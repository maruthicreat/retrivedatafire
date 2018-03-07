package com.example.maruthiraja.retrivedatafire;


public class GetPurchaseData {
    String Amount_payable;
    String ispayed;
    String itemid;
    String paymentMode;

    public String getAmount_payable() {
        return Amount_payable;
    }

    public void setAmount_payable(String amount_payable) {
        Amount_payable = amount_payable;
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

    public GetPurchaseData(String amount_payable, String ispayed, String itemid, String paymentMode) {

        Amount_payable = amount_payable;
        this.ispayed = ispayed;
        this.itemid = itemid;
        this.paymentMode = paymentMode;
    }

    public GetPurchaseData()
    {

    }
}
