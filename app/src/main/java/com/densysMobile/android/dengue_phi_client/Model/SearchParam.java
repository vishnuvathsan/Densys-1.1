package com.densysMobile.android.dengue_phi_client.Model;

import java.io.Serializable;

/**
 * Created by Sineth on 2/7/2017.
 */

public class SearchParam implements Serializable {
    private String bht, ptSex, dateType, date1, date2, month, year, hosid, startFrom;

    private int age1, age2;

    public String getBht() {
        return bht;
    }

    public void setBht(String bht) {
        this.bht = bht;
    }

    public String getPtSex() {
        return ptSex;
    }

    public void setPtSex(String ptSex) {
        this.ptSex = ptSex;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public int getAge1() {
        return age1;
    }

    public void setAge1(int age1) {
        this.age1 = age1;
    }

    public int getAge2() {
        return age2;
    }

    public void setAge2(int age2) {
        this.age2 = age2;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHosid() {
        return hosid;
    }

    public void setHosid(String hosid) {
        this.hosid = hosid;
    }

    public String getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(String startFrom) {
        this.startFrom = startFrom;
    }

    @Override
    public String toString() {
        return "SearchParam{" +
                "bht='" + bht + '\'' +
                ", ptSex='" + ptSex + '\'' +
                ", dateType='" + dateType + '\'' +
                ", date1='" + date1 + '\'' +
                ", date2='" + date2 + '\'' +
                ", age1='" + age1 + '\'' +
                ", age2='" + age2 + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", hosid='" + hosid + '\'' +
                ", startFrom='" + startFrom + '\'' +
                '}';
    }
}
