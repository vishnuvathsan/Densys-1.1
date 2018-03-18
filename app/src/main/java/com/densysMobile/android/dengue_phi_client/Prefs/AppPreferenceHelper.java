package com.densysMobile.android.dengue_phi_client.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sineth on 2/14/2017.
 */

public class AppPreferenceHelper implements SharedPreferenceHelper {

    public static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";
    public static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    public static final String PREF_KEY_BEARER_TOKEN = "PREF_KEY_BEARER_TOKEN";
    public static final String PREF_KEY_API_KEY = "PREF_KEY_API_KEY";
    public static final String PREF_KEY_JWT = "PREF_KEY_JWT";
    public static final String PREF_KEY_HASHED_PASSWORD = "PREF_KEY_HASHED_PASSWORD";
    public static final String PREF_KEY_IS_LOGGED_IN = "PREF_KEY_IS_LOGGED_IN";
    public static final String PREF_KEY_USER_LEVEL = "USER LEVEL";
    public final SharedPreferences mPrefs;

    public AppPreferenceHelper(Context context, String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String getCurrentUserName() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public String getBearerToken() {
        return mPrefs.getString(PREF_KEY_BEARER_TOKEN, null);
    }

    @Override
    public void setBearerToken(String bearerToken) {
        mPrefs.edit().putString(PREF_KEY_BEARER_TOKEN, bearerToken).apply();
    }

    @Override
    public String getApiKey() {
        return mPrefs.getString(PREF_KEY_API_KEY, null);
    }

    @Override
    public void setApiKey(String apiKey) {
        mPrefs.edit().putString(PREF_KEY_API_KEY, apiKey).apply();
    }

    @Override
    public String getJwt() {
        return mPrefs.getString(PREF_KEY_JWT, null);
    }

    @Override
    public void setJwt(String jwt) {
        mPrefs.edit().putString(PREF_KEY_JWT, jwt).apply();
    }

    @Override
    public Boolean isLoggedIn() {
        return mPrefs.getBoolean(PREF_KEY_IS_LOGGED_IN, false);
    }

    @Override
    public void setIsLoggedIn(Boolean isLoggedIn) {
        mPrefs.edit().putBoolean(PREF_KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    @Override
    public String getHashedPassword() {
        return mPrefs.getString(PREF_KEY_HASHED_PASSWORD, null);
    }

    @Override
    public void setHashedPassword(String hashedPassword) {
        mPrefs.edit().putString(PREF_KEY_HASHED_PASSWORD, hashedPassword).apply();
    }

    @Override
    public String getUserLevel() {
        return mPrefs.getString(PREF_KEY_USER_LEVEL, null);
    }

    @Override
    public void setUserLevel(String userLevel) {
        mPrefs.edit().putString(PREF_KEY_USER_LEVEL, userLevel).apply();
    }


}
