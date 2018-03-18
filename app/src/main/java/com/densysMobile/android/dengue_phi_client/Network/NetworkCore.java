package com.densysMobile.android.dengue_phi_client.Network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.densysMobile.android.dengue_phi_client.AddNewPatientForm;
import com.densysMobile.android.dengue_phi_client.Model.GN;
import com.densysMobile.android.dengue_phi_client.Model.MOH;
import com.densysMobile.android.dengue_phi_client.PatientDetails;
import com.densysMobile.android.dengue_phi_client.Login;
import com.densysMobile.android.dengue_phi_client.MainActivity;
import com.densysMobile.android.dengue_phi_client.Model.District;
import com.densysMobile.android.dengue_phi_client.Model.NeighbourPatient;
import com.densysMobile.android.dengue_phi_client.Model.PHIArea;
import com.densysMobile.android.dengue_phi_client.Model.Record;
import com.densysMobile.android.dengue_phi_client.Model.RecordView;
import com.densysMobile.android.dengue_phi_client.Model.SearchParam;
import com.densysMobile.android.dengue_phi_client.Model.Site;
import com.densysMobile.android.dengue_phi_client.Model.VisitDetail;
import com.densysMobile.android.dengue_phi_client.Network.NetworkModel.InstantLoginResponse;
import com.densysMobile.android.dengue_phi_client.Prefs.AppPreferenceHelper;
import com.densysMobile.android.dengue_phi_client.R;
import com.densysMobile.android.dengue_phi_client.Util.Constants;
import com.densysMobile.android.dengue_phi_client.Util.PatientVisitHistoryDataAdapter;
import com.densysMobile.android.dengue_phi_client.Util.RecordTableDataAdapter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.codecrafters.tableview.TableView;

/**
 * Created by Sineth on 1/26/2017.
 */
public class NetworkCore {
    private final static String TAG = "NetworkCore";

    public static void login(final String username, String md5, final EditText usernameText, final EditText passwordText, final Context context) {
        final AppPreferenceHelper apiKeySharedPreferenceHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        final AppPreferenceHelper jwtSharedPreferenceHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        final AppPreferenceHelper isLoggedInPreferenceHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_IS_LOGGED_IN);
        final AppPreferenceHelper userLevelPreferenceHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_USER_LEVEL);
        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .cancelable(false)
                .theme(Theme.LIGHT)
                .build();
        progressDialog.show();
        AndroidNetworking.post(Constants.URL_BASE_PATH_LOGIN + "login")
                .addHeaders("USERNAME", username)
                .addHeaders("PASSWORD", md5)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.i(TAG, "Login Successful");
                        try {
                            apiKeySharedPreferenceHelper.setApiKey(response.getString("apikey"));
                            jwtSharedPreferenceHelper.setJwt(response.getString("jwt"));
                            isLoggedInPreferenceHelper.setIsLoggedIn(true);
/**
 * User level is not sent as of now 2017/05/13
 */
//                            String userType = response.getString("usertype");
//                            if ("ADMIN".equals(userType)){
//                                new MaterialDialog.Builder(context)
//                                        .title("Administrator Login")
//                                        .theme(Theme.LIGHT)
//                                        .content("It seems like you are trying to access an Administrator account. " +
//                                                "Please try again with a valid PHI or MOH account.")
//                                        .positiveText("OK")
//                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
//                                            @Override
//                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                                            }
//                                        })
//                                        .show();
//                            }else{
//                                userLevelPreferenceHelper.setUserLevel(userType); //To set the user level
//                            }
                            context.startActivity(new Intent(context, MainActivity.class));
                            ((Activity) context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        if (anError.getErrorCode() == 0) {
                            new MaterialDialog.Builder(context)
                                    .title("Cannot Login")
                                    .theme(Theme.LIGHT)
                                    .content("Something went wrong with the network connection. " +
                                            "Please check your internet connection and try again.")
                                    .positiveText("OK")
                                    .show();
                        } else {
                            Log.e(TAG, "Error while login due to " + anError.getErrorCode());
                            isLoggedInPreferenceHelper.setIsLoggedIn(false);
                            String message = "Login Failed. Username or password is incorrect";
                            try {
                                JSONObject jsonObject = new JSONObject(anError.getErrorBody());
                                message = (String) jsonObject.get("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                            usernameText.setText("");
                            passwordText.setText("");
                            usernameText.requestFocus();
                        }
                    }
                });
    }

    public static void getRecordList(final TableView<RecordView> sortableTableView, final Context context, SearchParam searchParam) {
        AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeyPrefHelper.getApiKey();
        String jwt = jwtKeyPrefHelper.getJwt();
        Log.i(TAG, searchParam.toString());
        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.fetching_data)
                .content(R.string.please_wait)
                .progress(true, 0)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .build();
        progressDialog.show();
        AndroidNetworking.get(Constants.URL_BASE_PATH + Constants.SEARCH_RECORDS)
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .addQueryParameter("bht", searchParam.getBht())
                .addQueryParameter("ptSex", searchParam.getPtSex())
                .addQueryParameter("month", searchParam.getMonth())
                .addQueryParameter("year", searchParam.getYear())
                .addQueryParameter("hosid", searchParam.getHosid())
                .addQueryParameter("startFrom", searchParam.getStartFrom())
                .addQueryParameter("dateType", searchParam.getDateType())
                .addQueryParameter("date1", searchParam.getDate1())
                .addQueryParameter("date2", searchParam.getDate2())
                .addQueryParameter("age1", String.valueOf(searchParam.getAge1()))
                .addQueryParameter("age2", String.valueOf(searchParam.getAge2()))
                .build()
                .getAsParsed(new TypeToken<List<Record>>() {
                }, new ParsedRequestListener<List<Record>>() {
                    @Override
                    public void onResponse(List<Record> response) {
                        List<RecordView> tableRecords = new ArrayList<RecordView>();
                        for (int rec = 0; rec < response.size(); rec++) {
                            RecordView recordView = new RecordView();
                            Record record = response.get(rec);
                            recordView.setRecid(record.getRecid());
                            recordView.setPtName(record.getPtName());
                            recordView.setHospitalName(record.getHospitalName());
                            recordView.setPhiDesc(record.getPhiDesc());
                            recordView.setPtDateAdd(record.getPtDateAdd());
                            recordView.setBHT(record.getBht());
                            recordView.setVisitCount(record.getPatientVisitCount());
                            tableRecords.add(recordView);
                        }
                        Collections.sort(tableRecords, new Comparator<RecordView>() {
                            @Override
                            public int compare(RecordView o1, RecordView o2) {
                                return ComparisonChain.start().compare(o1.getPhiDesc(), o2.getPhiDesc(), Ordering.natural().nullsLast())
                                        .compare(o1.getPtDateAdd().getDate(), o2.getPtDateAdd().getDate(), Ordering.natural().nullsLast())
                                        .result();

                            }
                        });
                        progressDialog.dismiss();
                        sortableTableView.setDataAdapter(new RecordTableDataAdapter(context, tableRecords));
                        PatientDetails.setRecordList(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, anError.getErrorDetail());
                    }
                });
    }

    public static void getAllDistrict(final HashMap<String, Integer> map, final Spinner spinner, final Context context) {
        AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeyPrefHelper.getApiKey();
        String jwt = jwtKeyPrefHelper.getJwt();

        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.dataLoadingTitle)
                .content(R.string.please_wait)
                .progress(true, 0)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .build();
        progressDialog.show();


        AndroidNetworking.get(Constants.URL_BASE_PATH + "getAllDistricts")
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .setPriority(Priority.LOW)
                .build()
                .getAsParsed(new TypeToken<List<District>>() {
                }, new ParsedRequestListener<List<District>>() {
                    @Override
                    public void onResponse(List<District> districts) {
                        progressDialog.dismiss();
                        String[] dpdhsDesc = new String[districts.size()];
                        for (int x = 0; x < districts.size(); x++) {
                            String name = districts.get(x).getDpdhsDesc();
                            Log.d("TAG", String.valueOf(districts.get(x).getDpdhsCode()));
                            Integer id = Integer.parseInt(districts.get(x).getDpdhsCode());
                            map.put(name, id);
                            dpdhsDesc[x] = name;
                        }
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dpdhsDesc);
                        spinner.setAdapter(districtAdapter);
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getMessage());
                        // handle error
                    }
                });
    }

    public static void getMOHList(final HashMap<String, Integer> map, final Spinner moh, final Context context, int dpdhsCode) {
        AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeyPrefHelper.getApiKey();
        String jwt = jwtKeyPrefHelper.getJwt();


        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.dataLoadingTitle)
                .content(R.string.please_wait)
                .progress(true, 0)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .build();
        progressDialog.show();


        AndroidNetworking.get(Constants.URL_BASE_PATH + "getMohsOfDistrict")
                .setPriority(Priority.LOW)
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .addQueryParameter("dpdhsCode", String.valueOf(dpdhsCode))
                .build()
                .getAsParsed(new TypeToken<List<MOH>>() {
                }, new ParsedRequestListener<List<MOH>>() {
                    @Override
                    public void onResponse(List<MOH> mohs) {
                        progressDialog.dismiss();
                        String[] mohsDesc = new String[mohs.size()];
                        for (int x = 0; x < mohs.size(); x++) {
                            String name = mohs.get(x).getMohDesc();
                            Integer id = Integer.parseInt(mohs.get(x).getId());
                            map.put(name, id);
                            mohsDesc[x] = name;
                        }
                        ArrayAdapter<String> mohAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mohsDesc);
                        moh.setAdapter(mohAdapter);
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getMessage());
                        Log.e(this.getClass().getSimpleName(), error.getMessage());
                    }
                });
    }

    public static void getPHIList(final HashMap<String, Integer> map, final Spinner phi, final Context context, int id) {
        AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeyPrefHelper.getApiKey();
        String jwt = jwtKeyPrefHelper.getJwt();

        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.dataLoadingTitle)
                .content(R.string.please_wait)
                .progress(true, 0)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .build();
        progressDialog.show();


        AndroidNetworking.get(Constants.URL_BASE_PATH + "getPhiAreasOfMoh")
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .setPriority(Priority.LOW)
                .addQueryParameter("mohId", String.valueOf(id))
                .build()
                .getAsParsed(new TypeToken<List<PHIArea>>() {
                }, new ParsedRequestListener<List<PHIArea>>() {
                    @Override
                    public void onResponse(List<PHIArea> phis) {
                        progressDialog.dismiss();
                        String[] phisDesc = new String[phis.size()];
                        for (int x = 0; x < phis.size(); x++) {
                            String name = phis.get(x).getPhiDesc();
                            Integer id = Integer.parseInt(phis.get(x).getId());
                            map.put(name, id);
                            phisDesc[x] = name;
                        }
                        ArrayAdapter<String> phiAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, phisDesc);
                        phi.setAdapter(phiAdapter);
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getMessage());
                        Log.e(this.getClass().getSimpleName(), error.getMessage());
                    }
                });
    }

    public static void getGNList(final HashMap<String, Integer> map, final Spinner gn, final Context context, int dpdhsCode) {
        AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeyPrefHelper.getApiKey();
        String jwt = jwtKeyPrefHelper.getJwt();

        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.dataLoadingTitle)
                .content(R.string.please_wait)
                .progress(true, 0)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .build();
        progressDialog.show();

        AndroidNetworking.get(Constants.URL_BASE_PATH + "getGnsOfDistrict")
                .setPriority(Priority.LOW)
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .addQueryParameter("dpdhsCode", String.valueOf(dpdhsCode))
                .build()
                .getAsParsed(new TypeToken<List<GN>>() {
                }, new ParsedRequestListener<List<GN>>() {
                    @Override
                    public void onResponse(List<GN> gns) {
                        progressDialog.dismiss();
                        String[] gnsDesc = new String[gns.size()];
                        for (int x = 0; x < gns.size(); x++) {
                            String name = gns.get(x).getGnName();
                            Integer id = Integer.parseInt(gns.get(x).getGnId());
                            map.put(name, id);
                            gnsDesc[x] = name;
                        }
                        ArrayAdapter<String> gndAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, gnsDesc);
                        gn.setAdapter(gndAdapter);
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println("Error in ANError");
                        // System.out.println(error.getErrorBody());
                    }
                });
    }

    public static void getSiteList(final HashMap<String, Integer> map, final Spinner hospital, final Context context) {
        AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeyPrefHelper.getApiKey();
        String jwt = jwtKeyPrefHelper.getJwt();
        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.fetching_data)
                .content(R.string.please_wait)
                .progress(true, 0)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .build();
        progressDialog.show();
        AndroidNetworking.get(Constants.URL_BASE_PATH + Constants.GET_ALL_SITES)
                .setPriority(Priority.LOW)
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .build()
                .getAsParsed(new TypeToken<List<Site>>() {
                }, new ParsedRequestListener<List<Site>>() {
                    @Override
                    public void onResponse(List<Site> siteList) {
                        progressDialog.dismiss();
                        String[] hospitalList = new String[siteList.size() + 1];
                        hospitalList[0] = "All";
                        map.put("All", -1);
                        for (int x = 1; x < (siteList.size() + 1); x++) {
                            String name = siteList.get(x - 1).getName();
                            int id = Integer.parseInt(siteList.get(x - 1).getSiteId());
                            map.put(name, id);
                            hospitalList[x] = name;
                        }
                        ArrayAdapter<String> gndAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, hospitalList);
                        hospital.setAdapter(gndAdapter);
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        Log.e(TAG, error.getMessage());
                    }
                });
    }

    public static void checkLoginStatus(final Context context, final Class<?> destination) {
        AppPreferenceHelper apiKeySharedPreference = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtSharedPreference = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeySharedPreference.getApiKey();
        String jwt = jwtSharedPreference.getJwt();
        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .build();
        progressDialog.show();
        AndroidNetworking.get(Constants.URL_BASE_PATH + Constants.VERIFY_LOGIN)
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .build()
                .getAsParsed(new TypeToken<InstantLoginResponse>() {
                }, new ParsedRequestListener<InstantLoginResponse>() {
                    @Override
                    public void onResponse(InstantLoginResponse response) {
                        if (response.getIsVerified()) {
                            context.startActivity(new Intent(context, destination));
                            progressDialog.dismiss();
                            ((Activity) context).finish();
                        } else {
                            Toast.makeText(context, "Instant Login failed " + response.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
                            Log.e(TAG, "Instant Login failed " + anError.getErrorDetail());
                            progressDialog.dismiss();
                            Snackbar.with(context, null)
                                    .type(Type.UPDATE)
                                    .message("Please Enter User Credentials")
                                    .duration(Duration.CUSTOM, 5000)
                                    .show();
                        } else {
                            progressDialog.dismiss();
                            new MaterialDialog.Builder(context)
                                    .title("Cannot Login")
                                    .theme(Theme.LIGHT)
                                    .content("Something went wrong with the network connection. " +
                                            "Please check your internet connection and try again.")
                                    .positiveText("OK")
                                    .show();
                        }

                    }
                });
    }

    public static void submitPatient(final Context context, NeighbourPatient neighbourPatient) {
        AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeyPrefHelper.getApiKey();
        String jwt = jwtKeyPrefHelper.getJwt();
        AndroidNetworking.post(Constants.URL_BASE_PATH + "addNeighbourRecord")
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .addBodyParameter("ptName", neighbourPatient.getPtName())
                .addBodyParameter("ptSex", neighbourPatient.getPtSex())
                .addBodyParameter("ptAdd", neighbourPatient.getAddress())
                .addBodyParameter("ptAge", neighbourPatient.getPtAge())
                .addBodyParameter("district", neighbourPatient.getDistrict())
                .addBodyParameter("moh", neighbourPatient.getMoh())
                .addBodyParameter("phi", neighbourPatient.getPhi())
                .addBodyParameter("gnId", neighbourPatient.getGnId())
                .addBodyParameter("ptRem", neighbourPatient.getPtRem())
                .addBodyParameter("PtDateAdd", neighbourPatient.getPtDateAdd())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("Added Successfully")) {
                                new MaterialDialog.Builder(context)
                                        .title("New Patient Added")
                                        .content("Successfully added patient ")
                                        .cancelable(false)
                                        .positiveText("OK")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Intent intent = new Intent(context, MainActivity.class);
                                                context.startActivity(intent);
                                                ((Activity) context).finish();

                                            }
                                        }).show();
                            } else {
                                Log.e(TAG, response.getString("Status"));
                            }
                        } catch (JSONException jsonE) {
                            new MaterialDialog.Builder(context)
                                    .title("Error")
                                    .content("Somethings went wrong, please check your internet connectivity")
                                    .positiveText("OK")
                                    .show();
                            Log.e(this.getClass().getSimpleName(), jsonE.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, " Error occurred due to " + error.getErrorDetail());
                        new MaterialDialog.Builder(context)
                                .title("Error")
                                .content("Somethings went wrong, please check your internet connectivity")
                                .positiveText("OK")
                                .show();
                        AddNewPatientForm addNewPatientForm = (AddNewPatientForm) context;
                        addNewPatientForm.enableSubmitButton();

                    }
                });

    }

    public static void resetPassword(String currentPass, String newPass, final Context context) {
        AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeyPrefHelper.getApiKey();
        String jwt = jwtKeyPrefHelper.getJwt();
        AndroidNetworking.post(Constants.URL_BASE_PATH + "changeUserPassword")
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .addBodyParameter("exPassword", currentPass)
                .addBodyParameter("newPassword", newPass)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if ("Successfully Updated".equals(response.getString("status"))) {
                                Toast.makeText(context, "Password Successfully Updated", Toast.LENGTH_SHORT).show();
                                AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
                                AppPreferenceHelper jwtPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
                                apiKeyPrefHelper.setApiKey("");
                                jwtPrefHelper.setJwt("");
                                context.startActivity(new Intent(context, Login.class));
                            } else if ("Password Mismatched".equals(response.getString("status"))) {
                                Toast.makeText(context, "Error occurred due to password mismatch.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public static void getPatientVisitHistory(final Context context, String patientNo, String patientType, final TableView<VisitDetail> sortableTableView) {
        AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_API_KEY);
        AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(context, AppPreferenceHelper.PREF_KEY_JWT);
        String apiKey = apiKeyPrefHelper.getApiKey();
        String jwt = jwtKeyPrefHelper.getJwt();
        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.fetching_data)
                .content(R.string.please_wait)
                .progress(true, 0)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .build();
        progressDialog.show();
        AndroidNetworking.get(Constants.URL_BASE_PATH + Constants.GET_PATIENT_VISIT_DETAILS)
                .addHeaders("X-AUTH-TOKEN", apiKey)
                .addHeaders("Auth", "Bearer " + jwt)
                .addQueryParameter("patientNo", patientNo)
                .addQueryParameter("type", patientType)
                .build()
                .getAsParsed(new TypeToken<List<VisitDetail>>() {
                }, new ParsedRequestListener<List<VisitDetail>>() {
                    @Override
                    public void onResponse(List<VisitDetail> response) {
                        Log.d("TAG", String.valueOf(response));
                        List<VisitDetail> visitDetailsList = new ArrayList<>();
                        for (int rec = 0; rec < response.size(); rec++) {
                            VisitDetail visitDetail = response.get(rec);
                            visitDetailsList.add(visitDetail);
                        }
                        progressDialog.dismiss();
                        sortableTableView.setDataAdapter(new PatientVisitHistoryDataAdapter(context, visitDetailsList, sortableTableView));

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, anError.getErrorDetail());
                    }
                });
    }


}
