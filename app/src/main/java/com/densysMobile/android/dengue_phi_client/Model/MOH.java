package com.densysMobile.android.dengue_phi_client.Model;

/**
 * Created by Sineth on 1/27/2017.
 */

public class MOH {
    private String id, mohCode, dpdhsCode, mohDesc, active, mohcodee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMohCode() {
        return mohCode;
    }

    public void setMohCode(String mohCode) {
        this.mohCode = mohCode;
    }

    public String getDpdhsCode() {
        return dpdhsCode;
    }

    public void setDpdhsCode(String dpdhsCode) {
        this.dpdhsCode = dpdhsCode;
    }

    public String getMohDesc() {
        return mohDesc;
    }

    public void setMohDesc(String mohDesc) {
        this.mohDesc = mohDesc;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMohcodee() {
        return mohcodee;
    }

    public void setMohcodee(String mohcodee) {
        this.mohcodee = mohcodee;
    }

    @Override
    public String toString() {
        return "MOH{" +
                "id='" + id + '\'' +
                ", mohCode='" + mohCode + '\'' +
                ", dpdhsCode='" + dpdhsCode + '\'' +
                ", mohDesc='" + mohDesc + '\'' +
                ", active='" + active + '\'' +
                ", mohcodee='" + mohcodee + '\'' +
                '}';
    }
}
