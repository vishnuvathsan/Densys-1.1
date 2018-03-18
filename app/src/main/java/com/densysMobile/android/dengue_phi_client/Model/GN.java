package com.densysMobile.android.dengue_phi_client.Model;

/**
 * Created by android on 1/29/17.
 */

public class GN {
    String gnId, gnName, gncodee;

    public String getGnId() {
        return gnId;
    }

    public void setGnId(String gnId) {
        this.gnId = gnId;
    }

    public String getGncodee() {
        return gncodee;
    }

    public void setGncodee(String gncodee) {
        this.gncodee = gncodee;
    }

    public String getGnName() {
        return gnName;
    }

    public void setGnName(String gnName) {
        this.gnName = gnName;
    }

    @Override
    public String toString() {
        return "GN{" +
                "gnId='" + gnId + '\'' +
                ", gnName='" + gnName + '\'' +
                ", gncodee='" + gncodee + '\'' +
                '}';
    }
}
