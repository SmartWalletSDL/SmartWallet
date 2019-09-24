package com.example.smartwallet;

public class TopUI {
    String isOwed;
    String prevTransactionName;
    String total;
    String prevTransactionValue;


    public TopUI(){

    }

    public String getPrevTransactionValue() {
        return prevTransactionValue;
    }

    public void setPrevTransactionValue(String prevTransactionValue) {
        this.prevTransactionValue = prevTransactionValue;
    }

    public String getIsOwed() {
        return isOwed;
    }

    public void setIsOwed(String isOwed) {
        this.isOwed = isOwed;
    }

    public String getPrevTransactionName() {
        return prevTransactionName;
    }

    public void setPrevTransactionName(String prevTransactionName) {
        this.prevTransactionName = prevTransactionName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
