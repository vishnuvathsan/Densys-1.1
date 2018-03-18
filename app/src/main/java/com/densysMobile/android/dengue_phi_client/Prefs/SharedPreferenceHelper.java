package com.densysMobile.android.dengue_phi_client.Prefs;

/**
 * Created by Sineth on 2/14/2017.
 */

public interface SharedPreferenceHelper {

    String getCurrentUserName();
    void setCurrentUserName(String userName);
    String getAccessToken();
    void setAccessToken(String accessToken);
    String getBearerToken();
    void setBearerToken(String bearerToken);
    String getApiKey();
    void setApiKey(String apiKey);
    String getJwt();
    void setJwt(String jwt);
    Boolean isLoggedIn();
    void setIsLoggedIn(Boolean isLoggedIn);
    String getHashedPassword();
    void setHashedPassword(String hashedPassword);
    String getUserLevel();
    void setUserLevel(String userLevel);
}
