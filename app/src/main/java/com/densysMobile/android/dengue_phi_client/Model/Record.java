package com.densysMobile.android.dengue_phi_client.Model;

import java.io.Serializable;

/**
 * Created by Sineth on 1/29/2017. dengue-phi-client
 */

public class Record implements Serializable {
    private String recid;
    private String bht;
    private String ptName;
    private ptDateAdd ptDateAdd;
    private String ptSex;
    private String hospitalName;
    private String district;
    private String mohDesc;
    private String phiDesc;
    private String gnName;
    private String patientVisitCount;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Record() {

    }

    public String getRecid() {
        return recid;
    }

    public void setRecid(String recid) {
        this.recid = recid;
    }

    public String getBht() {
        return bht;
    }

    public void setBht(String bht) {
        this.bht = bht;
    }

    public String getPtName() {
        return ptName;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
    }

    public ptDateAdd getPtDateAdd() {
        return ptDateAdd;
    }

    public void setPtDateAdd(ptDateAdd PtDateAdd) {
        this.ptDateAdd = PtDateAdd;
    }

    public String getPtSex() {
        return ptSex;
    }

    public void setPtSex(String ptSex) {
        this.ptSex = ptSex;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMohDesc() {
        return mohDesc;
    }

    public void setMohDesc(String mohDesc) {
        this.mohDesc = mohDesc;
    }

    public String getPhiDesc() {
        return phiDesc;
    }

    public void setPhiDesc(String phiDesc) {
        this.phiDesc = phiDesc;
    }

    public String getGnName() {
        return gnName;
    }

    public void setGnName(String gnName) {
        this.gnName = gnName;
    }

    public String getPatientVisitCount() {
        return patientVisitCount;
    }

    public void setPatientVisitCount(String patientVisitCount) {
        this.patientVisitCount = patientVisitCount;
    }

    @Override
    public String toString() {
        return "Record{" +
                "recid='" + recid + '\'' +
                ", bht='" + bht + '\'' +
                ", ptName='" + ptName + '\'' +
                ", PtDateAdd=" + ptDateAdd +
                ", ptSex='" + ptSex + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", district='" + district + '\'' +
                ", mohDesc='" + mohDesc + '\'' +
                ", phiDesc='" + phiDesc + '\'' +
                ", gnName='" + gnName + '\'' +
                ", patientVisitCount='" + patientVisitCount + '\'' +
                '}';
    }
}
