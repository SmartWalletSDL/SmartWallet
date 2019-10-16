package com.example.smartwallet;

public class History {
    private String isOwed;
    private String name;
    private String tag;
    private String theyPay;
    private String youPay;
//    private String timeStamp;
//    private String youPay;

    public History() {
    }


    public History(String isOwed, String name, String tag, String theyPay) {
        this.isOwed = isOwed;
        this.name = name;
        this.tag = tag;
        this.theyPay = theyPay;
//        this.timeStamp = timeStamp;
//        this.youPay = youPay;
    }

    public String getYouPay() {
        return youPay;
    }

    public void setYouPay(String youPay) {
        this.youPay = youPay;
    }


    public String getIsOwed() {
        return isOwed;
    }

    public void setIsOwed(String isOwed) {
        this.isOwed = isOwed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTheyPay() {
        return theyPay;
    }

    public void setTheyPay(String theyPay) {
        this.theyPay = theyPay;
    }

//    public String getTimeStamp() {
//        return timeStamp;
//    }
//
//    public void setTimeStamp(String timeStamp) {
//        this.timeStamp = timeStamp;
//    }
//
//    public String getYouPay() {
//        return youPay;
//    }
//
//    public void setYouPay(String youPay) {
//        this.youPay = youPay;
//    }
}
