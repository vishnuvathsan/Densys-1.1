package com.densysMobile.android.dengue_phi_client.Model;

/**
 * Created by Sineth on 1/29/2017.
 */

public class Site {
    private String name, siteId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", siteId='" + siteId + '\'' +
                '}';
    }
}
