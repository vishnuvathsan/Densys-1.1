package com.densysMobile.android.dengue_phi_client;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.densysMobile.android.dengue_phi_client.Model.Record;
import com.densysMobile.android.dengue_phi_client.Model.SearchParam;
import com.densysMobile.android.dengue_phi_client.Network.NetworkCore;
import com.densysMobile.android.dengue_phi_client.Prefs.AppPreferenceHelper;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class PatientDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static HashMap<String, Record> recordList = new HashMap<>();
    private String specificDate = null;
    private String specificYear = null;
    private String specificMonth = null;
    private HashMap<String, Integer> hospitalMap = new HashMap<>();
    private boolean reset = false;
    private TextView startDate;
    private TextView endDate;

    public static void setRecordList(List<Record> listRecord) {
        for (int rec = 0; rec < listRecord.size(); rec++) {
            Record record = listRecord.get(rec);
            recordList.put(record.getRecid(), record);
        }
    }

    public static Record getRecord(String redIc) {
        return recordList.get(redIc);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        final TextView minAge = (TextView) findViewById(R.id.minAgeTV);
        final TextView maxAge = (TextView) findViewById(R.id.maxAgeTv);
        startDate = (TextView) findViewById(R.id.startDateTV);
        endDate = (TextView) findViewById(R.id.endDateTV);
        final TextView startLabel = (TextView) findViewById(R.id.startLabel);
        final TextView endLabel = (TextView) findViewById(R.id.endLabel);
        final Button searchButton = (Button) findViewById(R.id.searchButton);
        final Button resetButton = (Button) findViewById(R.id.resetButton);
        final EditText bhtEditText = (EditText) findViewById(R.id.BHTEditText);
        final RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        final RadioGroup enteredDateRadioGroup = (RadioGroup) findViewById(R.id.enteredDateRadioGroup);
        final CrystalRangeSeekbar crystalRangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekBar);
        final Spinner hospitalListSpinner = (Spinner) findViewById(R.id.hospitalListSpinner);
        final RadioButton optionAny = (RadioButton) findViewById(R.id.optionAny);
        final RadioButton optionSM = (RadioButton) findViewById(R.id.optionSM);
        final RadioButton optionSD = (RadioButton) findViewById(R.id.optionSD);
        final RadioButton optionSY = (RadioButton) findViewById(R.id.optionSY);
        final RadioButton optionDR = (RadioButton) findViewById(R.id.optionDR);
        final AppPreferenceHelper userLevelPreferenceHelper = new AppPreferenceHelper(PatientDetails.this, AppPreferenceHelper.PREF_KEY_USER_LEVEL);
        String userLevel = userLevelPreferenceHelper.getUserLevel();
        optionAny.setEnabled(false);
        optionSM.setEnabled(false);
        optionSD.setEnabled(false);
        optionSY.setEnabled(false);
        startLabel.setVisibility(View.INVISIBLE);
        endLabel.setVisibility(View.INVISIBLE);
        endDate.setVisibility(View.INVISIBLE);
        startDate.setVisibility(View.INVISIBLE);


        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -14);
        Date fourteenDaysAgo = cal.getTime();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        String formattedTextTwoWeeksAgo = simpleDateFormat.format(fourteenDaysAgo);
        startLabel.setText("Start Date:");
        startLabel.setVisibility(View.VISIBLE);
        endLabel.setText("End Date:");
        endLabel.setVisibility(View.VISIBLE);
        startDate.setText(formattedTextTwoWeeksAgo);
        startDate.setVisibility(View.VISIBLE);

        final Calendar calendar = new GregorianCalendar();
        String todayDate = simpleDateFormat.format(calendar.getTime());
        endDate.setText(todayDate);
        endDate.setVisibility(View.VISIBLE);

        Snackbar.with(this, null)
                .type(Type.WARNING)
                .message("Only data from past 2 weeks are available")
                .duration(Duration.LONG)
                .show();
        getHospitalList(hospitalMap, hospitalListSpinner);
        crystalRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minAge.setText(minValue.toString());
                maxAge.setText(maxValue.toString());
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchParam searchParam = new SearchParam();
                String selectedHospital = String.valueOf(hospitalMap.get(hospitalListSpinner.getItemAtPosition(hospitalListSpinner.getSelectedItemPosition()).toString()));
                if (selectedHospital.equals("-1")) {
                    searchParam.setHosid("");
                } else {
                    searchParam.setHosid(selectedHospital);
                }
                searchParam.setBht(bhtEditText.getText().toString());
                int checkedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.optionMale) {
                    searchParam.setPtSex("0");
                } else if (checkedRadioButtonId == R.id.optionFemale) {
                    searchParam.setPtSex("1");
                } else {
                    searchParam.setPtSex(null);
                }
                if (enteredDateRadioGroup.getCheckedRadioButtonId() == R.id.optionAny) {
                    searchParam.setDateType("0");
                    searchParam.setDate1(null);
                    searchParam.setDate2(null);
                    searchParam.setMonth(null);
                    searchParam.setYear(null);
                    searchParam.setStartFrom(null);
                } else if (enteredDateRadioGroup.getCheckedRadioButtonId() == R.id.optionSD) {
                    searchParam.setDateType("1");
                    searchParam.setDate1(specificDate); // TODO: 2/7/2017 fill this
                    searchParam.setDate2(null);
                    searchParam.setMonth(null);
                    searchParam.setYear(null);
                    searchParam.setStartFrom(null);
                } else if (enteredDateRadioGroup.getCheckedRadioButtonId() == R.id.optionSM) {
                    searchParam.setDateType("2");
                    searchParam.setDate1(null);
                    searchParam.setYear(specificYear);    // TODO: 2/7/2017 this as well
                    searchParam.setDate2(null);
                    searchParam.setMonth(specificMonth);
                    searchParam.setStartFrom(null);
                } else if (enteredDateRadioGroup.getCheckedRadioButtonId() == R.id.optionSY) {
                    searchParam.setDateType("3");
                    searchParam.setYear(specificYear);    // TODO: 2/7/2017
                    searchParam.setDate1(null);
                    searchParam.setDate2(null);
                    searchParam.setMonth(null);
                    searchParam.setStartFrom(null);
                } else if (enteredDateRadioGroup.getCheckedRadioButtonId() == R.id.optionDR) {
                    searchParam.setDateType("4");
                    searchParam.setDate1(startDate.getText().toString());
                    searchParam.setDate2(endDate.getText().toString());
                    searchParam.setMonth(null);
                    searchParam.setYear(null);
                    searchParam.setStartFrom(null);
                }
                searchParam.setAge1(Integer.valueOf(minAge.getText().toString())*12);
                searchParam.setAge2(Integer.valueOf(maxAge.getText().toString())*12);
                Intent intent = new Intent(PatientDetails.this, ViewPatientList.class);
                intent.putExtra("KEY", searchParam);
                startActivity(intent);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = new GregorianCalendar();
                cal.add(Calendar.DAY_OF_MONTH, -14);
                Date fourteenDaysAgo = cal.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
                String formattedTextTwoWeeksAgo = simpleDateFormat.format(fourteenDaysAgo);
                startLabel.setText("Start Date:");
                startLabel.setVisibility(View.VISIBLE);
                endLabel.setText("End Date:");
                endLabel.setVisibility(View.VISIBLE);
                endDate.setText(formattedTextTwoWeeksAgo);
                endDate.setVisibility(View.VISIBLE);

                Calendar calendar = new GregorianCalendar();
                String todayDate = simpleDateFormat.format(calendar.getTime());
                startDate.setText(todayDate);
                startDate.setVisibility(View.VISIBLE);
                reset = true;
                bhtEditText.setText("");
                genderRadioGroup.clearCheck();
//                enteredDateRadioGroup.clearCheck();
                crystalRangeSeekbar.setMinValue(0).setMaxValue(110).apply();
//                optionDR.setChecked(true);

            }
        });
        enteredDateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (reset) {
                    reset = false;
                    return;
                }
                if (checkedId == R.id.optionSD) {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            PatientDetails.this, R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month += 1;
                            specificDate = "" + year + "-" + month + "-" + dayOfMonth;
                            startDate.setText(specificDate);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                    datePickerDialog.show();
                    startLabel.setText("Specific Date");
                    startDate.setVisibility(View.VISIBLE);
                    startLabel.setVisibility(View.VISIBLE);
                    endLabel.setVisibility(View.INVISIBLE);
                    endDate.setVisibility(View.INVISIBLE);
                } else if (checkedId == R.id.optionDR) {
                    SmoothDateRangePickerFragment smoothDateRangePickerFragment = SmoothDateRangePickerFragment.newInstance(
                            new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                                @Override
                                public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                                           int yearStart, int monthStart,
                                                           int dayStart, int yearEnd,
                                                           int monthEnd, int dayEnd) {
                                    startLabel.setText("From:");
                                    startLabel.setVisibility(View.VISIBLE);
                                    endLabel.setVisibility(View.VISIBLE);
                                    endLabel.setText("To:");
                                    startDate.setVisibility(View.VISIBLE);
                                    endDate.setVisibility(View.VISIBLE);
                                    Calendar startDateCalenderInstance = Calendar.getInstance();
                                    startDateCalenderInstance.clear();
                                    startDateCalenderInstance.set(yearStart, monthStart, dayStart);
                                    Date startDateInstance = startDateCalenderInstance.getTime();
                                    startDate.setText(simpleDateFormat.format(startDateInstance));

                                    Calendar endDateCalenderInstance = Calendar.getInstance();
                                    endDateCalenderInstance.clear();
                                    endDateCalenderInstance.set(yearEnd, monthEnd, dayEnd);
                                    Date endDateInstance = endDateCalenderInstance.getTime();
                                    endDate.setText(simpleDateFormat.format(endDateInstance));


                                }
                            });
                    smoothDateRangePickerFragment.show(getFragmentManager(), "smoothDateRangePicker");
                } else if (checkedId == R.id.optionSM) {
                    startLabel.setText("Specific Month");
                    endLabel.setVisibility(View.INVISIBLE);
                    endDate.setVisibility(View.INVISIBLE);
                    new MaterialDialog.Builder(PatientDetails.this)
                            .title(R.string.title_specific_month)
                            .items(R.array.month_array)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    specificMonth = String.valueOf(text);
                                    startDate.setText(specificMonth);
                                }
                            })
                            .show();
                    startDate.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.optionSY) {
                    startLabel.setText("Specific Year");
                    endLabel.setVisibility(View.INVISIBLE);
                    endDate.setVisibility(View.INVISIBLE);
                    new MaterialDialog.Builder(PatientDetails.this)
                            .title(R.string.input)
                            .content(R.string.input_content)
                            .inputType(InputType.TYPE_CLASS_NUMBER)
                            .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                    specificYear = String.valueOf(input);
                                    startLabel.setVisibility(View.VISIBLE);
                                    startDate.setText(specificYear);
                                    startDate.setVisibility(View.VISIBLE);
                                }
                            }).show();

                } else if (checkedId == R.id.optionAny) {
                    startLabel.setVisibility(View.INVISIBLE);
                    endLabel.setVisibility(View.INVISIBLE);
                    endDate.setVisibility(View.INVISIBLE);
                    startDate.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Toast.makeText(PatientDetails.this, "" + year + "-" + month + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
    }

    private void getHospitalList(HashMap<String, Integer> map, Spinner gn) {
        map.clear();
        NetworkCore.getSiteList(map, gn, this);
    }

    public void showDatePickerDialog(final View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

                Calendar startDateCalenderInstance = Calendar.getInstance();
                startDateCalenderInstance.clear();
                startDateCalenderInstance.set(year, month, dayOfMonth);
                Date startDateInstance = startDateCalenderInstance.getTime();
                startDate.setText(simpleDateFormat.format(startDateInstance));

                Calendar endDateCalenderInstance = Calendar.getInstance();
                endDateCalenderInstance.clear();
                endDateCalenderInstance.set(year, month, dayOfMonth);
                endDateCalenderInstance.add(Calendar.DAY_OF_MONTH, 14);
                Date endDateInstance = endDateCalenderInstance.getTime();
                endDate.setText(simpleDateFormat.format(endDateInstance));
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
