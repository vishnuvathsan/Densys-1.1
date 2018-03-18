package com.densysMobile.android.dengue_phi_client.Model;

import java.io.Serializable;

/**
 * Created by android on 3/9/17.
 */

public class RecordView implements Serializable {
    private String recid;
    private String ptName;
    private ptDateAdd ptDateAdd;
    private String hospitalName;
    private String phiDesc;
    private String BHT;
    private String visitCount;


    public String getRecid() {
        return recid;
    }

    public void setRecid(String recid) {
        this.recid = recid;
    }

    public String getPtName() {
        return ptName;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
    }

    public com.densysMobile.android.dengue_phi_client.Model.ptDateAdd getPtDateAdd() {
        return ptDateAdd;
    }

    public void setPtDateAdd(com.densysMobile.android.dengue_phi_client.Model.ptDateAdd ptDateAdd) {
        this.ptDateAdd = ptDateAdd;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getPhiDesc() {
        return phiDesc;
    }

    public void setPhiDesc(String phiDesc) {
        this.phiDesc = phiDesc;
    }


    public String getBHT() {
        return BHT;
    }

    public void setBHT(String BHT) {
        this.BHT = BHT;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }
}
