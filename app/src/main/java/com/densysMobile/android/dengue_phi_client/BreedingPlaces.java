package com.densysMobile.android.dengue_phi_client;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.densysMobile.android.dengue_phi_client.Prefs.AppPreferenceHelper;
import com.densysMobile.android.dengue_phi_client.Util.Constants;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class BreedingPlaces extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    ArrayList<String> added = new ArrayList<>();
    private int tableRows = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeding_places);

        final Button addToTable = (Button) findViewById(R.id.addToTable);
        final CheckBox cleaned = (CheckBox) findViewById(R.id.cleaned);
        final CheckBox hEducation = (CheckBox) findViewById(R.id.healthEducation);
        final CheckBox notice = (CheckBox) findViewById(R.id.notice);
        final CheckBox legal = (CheckBox) findViewById(R.id.legal);

        cleaned.setVisibility(View.INVISIBLE);
        hEducation.setVisibility(View.INVISIBLE);
        notice.setVisibility(View.INVISIBLE);
        legal.setVisibility(View.INVISIBLE);

        final String[] groupList = {"Water Storage", "Outside the House", "Inside the House", "Discarded Items", "Garden/Natural Items"};
        final String[] waterStorageTypes = {"WS Bucket", "WS Barrel", "WS Cement Tank"};
        final String[] outsideHouseTypes = {"Concrete Slab", "Roof Gutters", "Pet Feeding Trays", "Coverings", "Others"};
        final String[] insideHouseTypes = {"Refrigerator Trays", "A/C Trays", "Ant Traps", "Non-used Cisterns"};
        final String[] discardedItemTypes = {"Re-Usable Items", "Degradable Items", "Non-Degradable Items", "Tyres"};
        final String[] naturalTypes = {"Foliage", "Bamboo Stumps", "Tree Holes", "Ornaments", "Ponds"};

        final HashMap<String, Integer> subGroupList = new HashMap<>();
        subGroupList.put("11", 1);
        subGroupList.put("12", 2);
        subGroupList.put("13", 3);
        subGroupList.put("21", 4);
        subGroupList.put("22", 5);
        subGroupList.put("23", 6);
        subGroupList.put("24", 7);
        subGroupList.put("25", 8);
        subGroupList.put("31", 9);
        subGroupList.put("32", 10);
        subGroupList.put("33", 11);
        subGroupList.put("34", 12);
        subGroupList.put("41", 13);
        subGroupList.put("42", 14);
        subGroupList.put("43", 15);
        subGroupList.put("44", 16);
        subGroupList.put("51", 17);
        subGroupList.put("52", 18);
        subGroupList.put("53", 19);
        subGroupList.put("54", 20);
        subGroupList.put("55", 21);
        final TextView containerText = (TextView) findViewById(R.id.containersText);
        final TextView wetContainerText = (TextView) findViewById(R.id.wetContainerText);
        final TextView laveeContainerText = (TextView) findViewById(R.id.laveeContainerText);
        containerText.setVisibility(View.INVISIBLE);
        wetContainerText.setVisibility(View.INVISIBLE);
        laveeContainerText.setVisibility(View.INVISIBLE);
        final MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.group_spinner);
        spinner.setItems("--Select Group--", "Water Storage", "Outside the House", "Inside the House", "Discarded Items", "Garden/Natural Items");
        spinner.setBackgroundColor(Color.LTGRAY);
        final MaterialSpinner itemSpinner = (MaterialSpinner) findViewById(R.id.item_spinner);
        itemSpinner.setEnabled(false);
        itemSpinner.setBackgroundColor(Color.LTGRAY);
        final TableView<String[]> breedingPlacesTable = (TableView<String[]>) findViewById(R.id.breedingPlacesTable);
        breedingPlacesTable.setMinimumHeight(1000);
        breedingPlacesTable.setVisibility(View.INVISIBLE);
        final SeekBar containerNo = (SeekBar) findViewById(R.id.containerNo);
        final SeekBar wetContainerNo = (SeekBar) findViewById(R.id.wetContainerNo);
        final SeekBar laveeContainerNo = (SeekBar) findViewById(R.id.laveeContainerNo);
        containerNo.setVisibility(View.VISIBLE);
        containerNo.setVisibility(View.INVISIBLE);
        wetContainerNo.setVisibility(View.INVISIBLE);
        laveeContainerNo.setVisibility(View.INVISIBLE);

        breedingPlacesTable.setHeaderAdapter(new SimpleTableHeaderAdapter(BreedingPlaces.this, "Type", "Sub-Type", "Dry Containers"
                , "Potential Containers", "Larvee Positive Containers"));


        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (position == 1) {
                    itemSpinner.setItems("--Select Item--", "WS Bucket", "WS Barrel", "WS Cement Tank");
                    itemSpinner.setEnabled(true);
                } else if (position == 2) {
                    itemSpinner.setItems("--Select Item--", "Concrete Slab", "Roof Gutters", "Pet Feeding Trays", "Coverings", "Others");
                    itemSpinner.setEnabled(true);
                } else if (position == 3) {
                    itemSpinner.setItems("--Select Item--", "Refrigerator Trays", "A/C Trays", "Ant Traps", "Non-used Cisterns");
                    itemSpinner.setEnabled(true);
                } else if (position == 4) {
                    itemSpinner.setItems("--Select Item--", "Re-Usable Items", "Degradable Items", "Non-Degradable Items", "Tyres");
                    itemSpinner.setEnabled(true);
                } else if (position == 5) {
                    itemSpinner.setItems("--Select Item--", "Foliage", "Bamboo Stumps", "Tree Holes", "Ornaments", "Ponds");
                    itemSpinner.setEnabled(true);
                } else if (position == 0) {
                    itemSpinner.setEnabled(false);
                }
            }
        });
        itemSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position != 0) {
                    containerNo.setVisibility(View.VISIBLE);
                    containerText.setVisibility(View.VISIBLE);
                }
            }
        });
        containerNo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wetContainerText.setVisibility(View.VISIBLE);
                wetContainerNo.setVisibility(View.VISIBLE);
                wetContainerNo.setMax(containerNo.getProgress());
                containerText.setText("No of Containers : " + seekBar.getProgress());
            }
        });
        wetContainerNo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                laveeContainerText.setVisibility(View.VISIBLE);
                laveeContainerNo.setVisibility(View.VISIBLE);
                laveeContainerNo.setMax(wetContainerNo.getProgress());
                wetContainerText.setText("No of Wet Containers : " + seekBar.getProgress());
                wetContainerText.setVisibility(View.VISIBLE);
            }
        });
        laveeContainerNo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                laveeContainerText.setVisibility(View.VISIBLE);
                laveeContainerText.setText("No of larvae +ve Containers : " + seekBar.getProgress());
                addToTable.setVisibility(View.VISIBLE);

            }
        });
        final String[][] DATA_TO_SHOW = new String[21][5];
        addToTable.setVisibility(View.INVISIBLE);

        addToTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleaned.setVisibility(View.VISIBLE);
                hEducation.setVisibility(View.VISIBLE);
                notice.setVisibility(View.VISIBLE);
                legal.setVisibility(View.VISIBLE);
                if (!(spinner.getSelectedIndex() == 0 || itemSpinner.getSelectedIndex() == 0 || containerNo.getProgress() == 0)) {
                    String[] row = new String[5];
                    int index = spinner.getSelectedIndex();
                    row[1] = String.valueOf(itemSpinner.getSelectedIndex());
                    int wet = wetContainerNo.getProgress();
                    row[2] = String.valueOf(containerNo.getProgress() - wet);
                    row[3] = String.valueOf(wet - laveeContainerNo.getProgress());
                    row[4] = String.valueOf(laveeContainerNo.getProgress());
                    String word = String.valueOf(index) + row[1];
                    int subIndex =Integer.valueOf(row[1]);
                    if (index == 1) {
                        row[1] = waterStorageTypes[subIndex- 1];
                    } else if (index == 2) {
                        row[1] = outsideHouseTypes[subIndex - 1];
                    } else if (index == 3) {
                        row[1] = insideHouseTypes[subIndex - 1];
                    } else if (index == 4) {
                        row[1] = discardedItemTypes[subIndex - 1];
                    } else if (index == 5) {
                        row[1] = naturalTypes[subIndex - 1];
                    }
                    row[0] = groupList[index - 1];
                    if (added.indexOf(word) == -1) {
                        added.add(word);
                        DATA_TO_SHOW[tableRows] = row;
                        tableRows += 1;
                        breedingPlacesTable.setDataAdapter(new SimpleTableDataAdapter(BreedingPlaces.this, DATA_TO_SHOW));
                        breedingPlacesTable.setVisibility(View.VISIBLE);
                    } else {
                        DATA_TO_SHOW[added.indexOf(word)] = row;
                        breedingPlacesTable.setDataAdapter(new SimpleTableDataAdapter(BreedingPlaces.this, DATA_TO_SHOW));
                        breedingPlacesTable.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(BreedingPlaces.this, "Select Breeding places type or sub type correctly", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button nextButton = (Button) findViewById(R.id.breedingPlacesNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableDataAdapter breedingPlacesAdapter = breedingPlacesTable.getDataAdapter();
                List<String[]> breedingPlacedDataList = breedingPlacesAdapter.getData();
                JSONArray bpList = new JSONArray();
                for (int k = 0; k < added.size(); k++) {
                    JSONObject bp = new JSONObject();
                    String[] data = breedingPlacedDataList.get(k);
                    try {
                        bp.put("groupId", String.valueOf(added.get(k).charAt(0)));
                        bp.put("typeId", added.get(k).toString());
                        bp.put("dry", String.valueOf(Integer.parseInt(data[2])));
                        bp.put("potential", String.valueOf(Integer.parseInt(data[3])));
                        bp.put("laveePositive", String.valueOf(Integer.parseInt(data[4])));
                        bpList.put(bp);
                        //breedingPlacesTable.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                String pActionTaken = "";
                if (cleaned.isChecked()) {
                    pActionTaken = pActionTaken + "1,";
                }
                if (hEducation.isChecked()) {
                    pActionTaken = pActionTaken + "2,";
                }
                if (notice.isChecked()) {
                    pActionTaken = pActionTaken + "3,";
                }
                if (legal.isChecked()) {
                    pActionTaken = pActionTaken + "4,";
                }
                AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(BreedingPlaces.this, AppPreferenceHelper.PREF_KEY_API_KEY);
                AppPreferenceHelper jwtKeyPrefHelper = new AppPreferenceHelper(BreedingPlaces.this, AppPreferenceHelper.PREF_KEY_JWT);
                String apiKey = apiKeyPrefHelper.getApiKey();
                String jwt = jwtKeyPrefHelper.getJwt();
                AndroidNetworking.post(Constants.URL_BASE_PATH + "addPatientVisitDetails")
                        .addHeaders("X-AUTH-TOKEN", apiKey)
//                        .addHeaders("Authorization", "Bearer " + jwt)
                        .addHeaders("Auth", "Bearer " + jwt)
                        .addBodyParameter("patientNo", getIntent().getStringExtra("patientNo"))
                        .addBodyParameter("pTypeFever", getIntent().getStringExtra("pTypeFever"))
                        .addBodyParameter("pDateInspect", getIntent().getStringExtra("pDateInspect"))
                        .addBodyParameter("pOccupation", getIntent().getStringExtra("pOccupation"))
                        .addBodyParameter("pComment", getIntent().getStringExtra("pComment"))
                        .addBodyParameter("pLatitude", getIntent().getStringExtra("pLatitude"))
                        .addBodyParameter("pLongitude", getIntent().getStringExtra("pLongitude"))
                        .addBodyParameter("recSavedLatitude", getIntent().getStringExtra("recSavedLatitude"))
                        .addBodyParameter("recSavedLongitude", getIntent().getStringExtra("recSavedLongitude"))
                        .addBodyParameter("recSavedDatetime", getIntent().getStringExtra("recSavedDateTime"))
                        .addBodyParameter("pLocSavedType", getIntent().getStringExtra("changeLoc"))
                        .addBodyParameter("type", getIntent().getStringExtra("type"))
                        .addBodyParameter("pActionTaken", pActionTaken)
                        .addBodyParameter("pNoBreeding", String.valueOf(added.size()))
                        .addBodyParameter("distance", "23")
                        .addBodyParameter("accuracy", "0")
                        .addBodyParameter("breedingPlacesOb", bpList.toString())
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                finishActivity(0);
                                try {
                                    if (response.getString("status").equals("Added Successfully")) {
                                        new MaterialDialog.Builder(BreedingPlaces.this)
                                                .title("Record Added")
                                                .content("Record Added to the database")
                                                .positiveText("OK")
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        Intent intent = new Intent(BreedingPlaces.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).show();
                                    } else {
                                        Log.e(TAG, response.getString("Status").toString());
                                    }
                                } catch (JSONException jsonE) {
                                    Log.e(TAG, jsonE.getMessage());
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                Log.e(TAG, error.getErrorDetail());
                            }
                        });
            }
        });
    }
}
