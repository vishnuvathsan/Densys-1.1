package com.densysMobile.android.dengue_phi_client.Model;

import java.io.Serializable;

/**
 * Created by Sineth on 2/7/2017.
 */

public class ptDateAdd implements Serializable {
    private String date,timezone_type,timezone;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimezone_type() {
        return timezone_type;
    }

    public void setTimezone_type(String timezone_type) {
        this.timezone_type = timezone_type;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "PtDateAdd{" +
                "date='" + date + '\'' +
                ", timezone_type='" + timezone_type + '\'' +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
