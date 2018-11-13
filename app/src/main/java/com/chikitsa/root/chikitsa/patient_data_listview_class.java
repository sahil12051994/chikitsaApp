package com.chikitsa.root.chikitsa;

import android.util.Log;

public class patient_data_listview_class {

    private int docScreen_or_pScreen = 0;
//  patient-> 0 , Doctor->1

    private int bp_sys;
    private int bp_dys, weight, dataId;
    private Double urine_albumin, bleedingVag;
    public String date_date, date_month, date_year, time_hour, time_min, time_period, time, timeExtracted;

    Boolean headache, abdominal_pain, visual_problems, decreased_fetal_movements, swelling_in_hands_or_face;
    String extra_comments;
    int bleeding_per_vaginum =0;

    public patient_data_listview_class(int dataId, String date,int bp_sysArg, int bp_dysArg, Double urine_albuminArg, int weight, Double bleedingVag) {
        this.bp_sys = bp_sysArg;
        this.bp_dys = bp_dysArg;
        this.urine_albumin = urine_albuminArg;
        this.bleedingVag = bleedingVag;
        this.weight = weight;
        this.date_date = get_date_date(date);
        this.date_month = get_date_month(date);
        this.date_year = get_date_year(date);
        this.timeExtracted = date.split("T")[1];
        this.time_hour = get_time_hour(timeExtracted);
        this.time_min = get_time_min(timeExtracted);
        this.time_period = get_time_period(timeExtracted);
        this.time = this.time_hour + ":" +time_min + " " + this.time_period;
        this.dataId = dataId;
        this.docScreen_or_pScreen=0;
    }

    public patient_data_listview_class(String date,int bp_sysArg, int bp_dysArg, Double urine_albuminArg, int weight, Boolean headache, Boolean abdominal_pain, Boolean visual_problems, Double bleedingVag, Boolean decreased_fetal_movements, Boolean swelling_in_hands_or_face, String extra_comments) {
        this.bp_sys = bp_sysArg;
        this.bp_dys = bp_dysArg;
        this.urine_albumin = urine_albuminArg;
        this.weight = weight;
        this.date_date = get_date_date(date);
        this.date_month = get_date_month(date);
        this.date_year = get_date_year(date);
        this.timeExtracted = date.split("T")[1];
        this.time_hour = get_time_hour(timeExtracted);
        this.time_min = get_time_min(timeExtracted);
        this.time_period = get_time_period(timeExtracted);
        this.time = this.time_hour + ":" +time_min + " " + this.time_period;

        this.headache = headache;
        this.abdominal_pain = abdominal_pain;
        this.visual_problems = visual_problems;
        this.decreased_fetal_movements = decreased_fetal_movements;
        this.swelling_in_hands_or_face = swelling_in_hands_or_face;
        this.bleedingVag = bleedingVag;
        this.extra_comments = extra_comments;

        this.docScreen_or_pScreen=1;
    }

    public int getDataId() {return  dataId;}

    public  int getDocOrPat() { return docScreen_or_pScreen; }

    public int bpSysValue(){
        return bp_sys;
    }

    public int bpDysValue(){
        return bp_dys;
    }

    public int weightValue(){
        return weight;
    }

    public Double urineAlValue(){
        return urine_albumin;
    }

    public Double bleedingValue(){
        return bleedingVag;
    }

    public String dateVal(){
        return date_date;
    }

    public String monthVal(){
        return date_month;
    }

    public String yearVal(){
        return date_year;
    }

    public String timeVal() { return time; }

    public Boolean getAbdominal_pain() {
        return abdominal_pain;
    }

    public Boolean getHeadache() {
        return headache;
    }

    public Boolean getSwelling_in_hands_or_face() {
        return swelling_in_hands_or_face;
    }

    public Boolean getVisual_problems() {
        return visual_problems;
    }

    public Boolean getDecreased_fetal_movements() {
        return decreased_fetal_movements;
    }

    public int getStatusId(){
        if (bp_sys > 150 && bp_dys > 110){
            return R.drawable.flame;
        }
        return R.drawable.health;
    }

    private String get_time_period(String time) {
        if (Integer.parseInt(time.split(":")[0]) > 12) {
            return "P.M";
        } else {
            return "A.M";
        }
    }

    private String get_time_min(String time) {
        return "" + time.split(":")[1];
    }

    private String get_time_hour(String time) {
        Log.d("TAG", time.toString());
        if (Integer.parseInt(time.split(":")[0]) > 12) {
            int hr_int = Integer.parseInt(time.split(":")[0]) - 12;
            return "" + hr_int;
        } else {
            return "" + time.split(":")[0];
        }
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
