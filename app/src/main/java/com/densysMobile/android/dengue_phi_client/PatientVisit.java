package com.densysMobile.android.dengue_phi_client;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.densysMobile.android.dengue_phi_client.Prefs.AppPreferenceHelper;
import com.densysMobile.android.dengue_phi_client.Util.LocationTracker;
import com.densysMobile.android.dengue_phi_client.Model.Record;

import java.util.Calendar;

public class PatientVisit extends AppCompatActivity {
    double latText;
    double longText;

    public double getLongText() {
        return longText;
    }

    public void setLongText(double longText) {
        this.longText = longText;
    }

    public double getLatText() {
        return latText;
    }

    public void setLatText(double latText) {
        this.latText = latText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_visit);
        getSupportActionBar().setHomeButtonEnabled(true);
        final Record key = (Record) getIntent().getSerializableExtra("KEY");
        final EditText latitude = (EditText) findViewById(R.id.input_latitude);
        final EditText longitude = (EditText) findViewById(R.id.input_longitude);
        final AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.typeFeverSelect);
        final CheckBox changeLoc = (CheckBox) findViewById(R.id.editLoc);
        final AppCompatSpinner occupationSpinner = (AppCompatSpinner) findViewById(R.id.occupation);
        final EditText comment = (EditText) findViewById(R.id.comment);
        final Button nextButton = (Button) findViewById(R.id.nextButton);
        setLongLatEditable(false, latitude, longitude);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }
        TextView patientNameTextView = (TextView) findViewById(R.id.patientNameTV);
        patientNameTextView.setText(key.getPtName());
        TextView dateOfInspection = (TextView) findViewById(R.id.dateOfInspection);
        changeLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setLongLatEditable(true, latitude, longitude);
                } else {
                    latitude.setText("");
                    longitude.setText("");
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    new MaterialDialog.Builder(PatientVisit.this)
                            .title("Enter Occupation")
                            .content("Please specify the job title")
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                    Toast.makeText(PatientVisit.this, input, Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR); // get the current year
        int month = cal.get(Calendar.MONTH); // month...
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String mn, dy;
        if (String.valueOf(month + 1).length() != 2) {
            mn = "0" + String.valueOf(month + 1);
        } else {
            mn = String.valueOf(month + 1);
        }
        if (String.valueOf(day).length() != 2) {
            dy = "0" + String.valueOf(day);
        } else {
            dy = String.valueOf(day);
        }
        final String simpledate = year + "-" + mn + "-" + dy;
        dateOfInspection.setText(simpledate);
        Button getLocationButton = (Button) findViewById(R.id.getLocationButton);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationTracker gps;
                gps = new LocationTracker(PatientVisit.this);
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gps);
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    setLatText(gps.getLatitude());
                    setLongText(gps.getLongitude());
                    // \n is for new line
                    latitude.setText(String.valueOf(getLatText()));
                    longitude.setText(String.valueOf(getLongText()));
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });
        Button viewOnMapButton = (Button) findViewById(R.id.viewOnMapButton);
        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PatientVisit.this, MapsActivity.class);
                intent.putExtra("latitute", String.valueOf(latText));
                intent.putExtra("longitude", String.valueOf(longText));
                startActivity(intent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientVisit.this, BreedingPlaces.class);
                intent.putExtra("patientNo", key.getRecid());
                intent.putExtra("pTypeFever", String.valueOf(spinner.getSelectedItemPosition()));
                intent.putExtra("pDateInspect", simpledate);
                intent.putExtra("pOccupation", String.valueOf(occupationSpinner.getSelectedItemPosition()));
                intent.putExtra("pComment", comment.getText().toString());
                intent.putExtra("pLatitude", String.valueOf(latText));
                intent.putExtra("pLongitude", String.valueOf(longText));
                intent.putExtra("recSavedLatitude", String.valueOf(getLatText()));
                intent.putExtra("recSavedLongitude", String.valueOf(getLongText()));
                intent.putExtra("recSavedDateTime", simpledate);
                intent.putExtra("changeLoc", String.valueOf(changeLoc.isChecked()));
                intent.putExtra("type", key.getType());
                startActivity(intent);
            }
        });
        Button history = (Button) findViewById(R.id.viewHistory);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientVisit.this, PatientHistory.class);
                intent.putExtra("KEY_PATIENT_NO", key.getRecid());
                intent.putExtra("KEY_PATIENT_TYPE", key.getType());
                startActivity(intent);
            }
        });
    }

    private void setLongLatEditable(Boolean cond, EditText latd, EditText longt) {
        latd.setFocusable(cond);
        longt.setFocusable(cond);
    }
}
