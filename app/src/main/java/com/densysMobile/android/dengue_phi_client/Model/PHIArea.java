package com.densysMobile.android.dengue_phi_client.Model;

/**
 * Created by android on 1/29/17.
 */

public class PHIArea {
    private String id, mohId, phiDesc, active, phicodee;

    public String getMohId() {
        return mohId;
    }

    public void setMohId(String mohId) {
        this.mohId = mohId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhiDesc() {
        return phiDesc;
    }

    public void setPhiDesc(String phiDesc) {
        this.phiDesc = phiDesc;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPhicodee() {
        return phicodee;
    }

    public void setPhicodee(String phicodee) {
        this.phicodee = phicodee;
    }

    @Override
    public String toString() {
        return "PHIArea{" +
                "id='" + id + '\'' +
                ", mohId='" + mohId + '\'' +
                ", phiDesc='" + phiDesc + '\'' +
                ", active='" + active + '\'' +
                ", phicodee='" + phicodee + '\'' +
                '}';
    }
}
