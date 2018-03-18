package com.densysMobile.android.dengue_phi_client.Model;

/**
 * Created by android on 2/14/17.
 */

public class AddPatientParams {
    String ptName;
    String ptSex;
    String ptAdd;
    String ptAge;
    String district;
    String moh;
    String phi;
    String gnId;
    String ptRem;
    String PtDateAdd;

    public String getPtDateAdd() {
        return PtDateAdd;
    }

    public void setPtDateAdd(String ptDateAdd) {
        PtDateAdd = ptDateAdd;
    }

    public String getPtName() {
        return ptName;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
    }

    public String getPtSex() {
        return ptSex;
    }

    public void setPtSex(String ptSex) {
        this.ptSex = ptSex;
    }

    public String getPtAdd() {
        return ptAdd;
    }

    public void setPtAdd(String ptAdd) {
        this.ptAdd = ptAdd;
    }

    public String getPtAge() {
        return ptAge;
    }

    public void setPtAge(String ptAge) {
        this.ptAge = ptAge;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMoh() {
        return moh;
    }

    public void setMoh(String moh) {
        this.moh = moh;
    }

    public String getPhi() {
        return phi;
    }

    public void setPhi(String phi) {
        this.phi = phi;
    }

    public String getGnId() {
        return gnId;
    }

    public void setGnId(String gnId) {
        this.gnId = gnId;
    }

    public String getPtRem() {
        return ptRem;
    }

    public void setPtRem(String ptRem) {
        this.ptRem = ptRem;
    }
}
