package com.densysMobile.android.dengue_phi_client.Network.NetworkModel;

/**
 * Created by Sineth on 3/9/2017. dengue-phi-client
 */

public class InstantLoginResponse {
    private Boolean isVerified;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
