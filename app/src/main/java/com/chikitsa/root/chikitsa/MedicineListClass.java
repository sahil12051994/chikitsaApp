package com.chikitsa.root.chikitsa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MedicineListClass {

    private String medName, comments, frequency;
    public String startDate_date, startDate_month, startDate_year, endDate_date, endDate_month, endDate_year, image;
    Date startDate, endDate;

    public MedicineListClass (String medName, String startDate, String endDate, String freq, String comments, String Img) throws ParseException {

        SimpleDateFormat dateFormatterServer = new SimpleDateFormat("yyyy-MM-dd");

        this.medName = medName;
        this.frequency = freq;
        this.comments = comments;
        this.startDate = dateFormatterServer.parse(startDate);
        this.endDate = dateFormatterServer.parse(endDate);
        this.startDate_date = get_date_date(startDate);
        this.startDate_month = get_date_month(startDate);
        this.startDate_year = get_date_year(startDate);
        this.endDate_date = get_date_date(endDate);
        this.endDate_month = get_date_month(endDate);
        this.endDate_year = get_date_year(endDate);
        this.image = Img;
    }

    public String getMedName(){ return medName; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public String getComments() { return comments; }
    public String getfreq() { return frequency; }

    public String getImage() {
        return image;
    }

    public String getStartDate_date(){
        return startDate_date;
    }

    public String getStartDate_month(){
        return startDate_month;
    }

    public String getStartDate_year(){
        return startDate_year;
    }

    public String getEndDate_date(){
        return endDate_date;
    }

    public String getEndDate_month(){
        return endDate_month;
    }

    public String getEndDate_year(){
        return endDate_year;
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
