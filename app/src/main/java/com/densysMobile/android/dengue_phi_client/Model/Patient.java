package com.densysMobile.android.dengue_phi_client.Model;

/**
 * Created by Sineth on 1/27/2017.
 */

public class Patient {
    private String name;
    private String id;
    private String hospital;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", hospital='" + hospital + '\'' +
                '}';
    }
}
