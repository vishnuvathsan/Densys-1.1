package com.densysMobile.android.dengue_phi_client.Model;

/**
 * Created by Sineth on 1/29/2017.
 */

public class PatientDetails {
    private String site;
    private String BHT;
    private String ageStart;
    private String ageEnd;
    private String gender;
    private String dateType;

    @Override
    public String toString() {
        return "PatientDetails{" +
                "dateType='" + dateType + '\'' +
                ", gender='" + gender + '\'' +
                ", ageEnd='" + ageEnd + '\'' +
                ", ageStart='" + ageStart + '\'' +
                ", BHT='" + BHT + '\'' +
                ", site='" + site + '\'' +
                '}';
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getBHT() {
        return BHT;
    }

    public void setBHT(String BHT) {
        this.BHT = BHT;
    }

    public String getAgeStart() {
        return ageStart;
    }

    public void setAgeStart(String ageStart) {
        this.ageStart = ageStart;
    }

    public String getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(String ageEnd) {
        this.ageEnd = ageEnd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

}
