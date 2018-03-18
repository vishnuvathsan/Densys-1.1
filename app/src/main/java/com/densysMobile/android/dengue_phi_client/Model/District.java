package com.densysMobile.android.dengue_phi_client.Model;

/**
 * Created by Sineth on 1/26/2017.
 */

public class District {
    private String dpdhsCode;
    private String proCode;
    private String dpdhsDesc;
    private String active;
    private String discodee;

    public String getDpdhsCode() {
        return dpdhsCode;
    }

    public void setDpdhsCode(String dpdhsCode) {
        this.dpdhsCode = dpdhsCode;
    }

    public String getDiscodee() {
        return discodee;
    }

    public void setDiscodee(String discodee) {
        this.discodee = discodee;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDpdhsDesc() {
        return dpdhsDesc;
    }

    public void setDpdhsDesc(String dpdhsDesc) {
        this.dpdhsDesc = dpdhsDesc;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    @Override
    public String toString() {
        return "District{" +
                "dpdhsCode='" + dpdhsCode + '\'' +
                ", proCode='" + proCode + '\'' +
                ", dpdhsDesc='" + dpdhsDesc + '\'' +
                ", active='" + active + '\'' +
                ", discodee='" + discodee + '\'' +
                '}';
    }
}
