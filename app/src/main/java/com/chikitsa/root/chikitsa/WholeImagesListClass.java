package com.chikitsa.root.chikitsa;

public class WholeImagesListClass {

    String extraComments, date, month, year;

    public WholeImagesListClass (String extraComments, String date) {
        this.extraComments = extraComments;
        this.date = get_date_date(date);
        this.month = get_date_month(date);
        this.year = get_date_year(date);
    }

    public String getExtraComments() {
        return extraComments;
    }

    public String getDateString(){
        return (date + "-" + month + "-" + year);
    }

    private String get_date_year(String date) {
        return date.split("-")[0];
    }

    private String get_date_month(String date) {
        String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int mon_int = Integer.parseInt(date.split("-")[1]);
        return months[mon_int];
    }

    private String get_date_date(String date) {
        return date.split("-")[2].split("T")[0];
    }

}
