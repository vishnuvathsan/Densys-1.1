package com.densysMobile.android.dengue_phi_client;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.densysMobile.android.dengue_phi_client.DB.DBUtil;
import com.densysMobile.android.dengue_phi_client.Model.NeighbourPatient;
import com.densysMobile.android.dengue_phi_client.Network.NetworkCore;
import com.densysMobile.android.dengue_phi_client.Util.ValidationModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewPatientForm extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 1;
    final int TAKE_PICTURE = 1;
    final int ACTIVITY_SELECT_IMAGE = 2;
    DBUtil dbUtil;
    HashMap<String, Integer> dpdhsmap = new HashMap<>();
    HashMap<String, Integer> mohmap = new HashMap<>();
    HashMap<String, Integer> phimap = new HashMap<>();
    HashMap<String, Integer> gnmap = new HashMap<>();
    private String TAG = getClass().getSimpleName();
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbUtil = new DBUtil(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_patient_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText patientName = (EditText) findViewById(R.id.comment);
        final Spinner gender = (android.support.v7.widget.AppCompatSpinner) findViewById(R.id.genderSelect);
        final Spinner dist = (android.support.v7.widget.AppCompatSpinner) findViewById(R.id.district);
        final Spinner moh = (android.support.v7.widget.AppCompatSpinner) findViewById(R.id.moh);
        final Spinner phi = (android.support.v7.widget.AppCompatSpinner) findViewById(R.id.phi);
        final Spinner gn = (android.support.v7.widget.AppCompatSpinner) findViewById(R.id.gnd);
        final EditText address = (EditText) findViewById(R.id.input_address);
        final EditText age = (EditText) findViewById(R.id.input_year_age);
        final EditText remarks = (EditText) findViewById(R.id.input_remarks);
        final EditText months = (EditText) findViewById(R.id.input_month_age);
        final Button sumit =(Button) findViewById(R.id.submitPatient);
//        final FloatingActionButton capturePhotoButton = (FloatingActionButton) findViewById(R.id.capturePhotoButton);
        getDistrictList(dpdhsmap, dist);
        gender.setBackgroundColor(Color.LTGRAY);
        dist.setBackgroundColor(Color.LTGRAY);
        moh.setBackgroundColor(Color.LTGRAY);
        phi.setBackgroundColor(Color.LTGRAY);
        gn.setBackgroundColor(Color.LTGRAY);

        dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = String.valueOf(dpdhsmap.get(adapterView.getItemAtPosition(i)));
                int dpdhsc = Integer.valueOf(id);
                getMOHList(mohmap, moh, dpdhsc);
                getGNList(gnmap, gn, dpdhsc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        moh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int id = mohmap.get(adapterView.getItemAtPosition(i));
                getPHIList(phimap, phi, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        final Button submitPatientButton = (Button) findViewById(R.id.submitPatient);
        submitPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitPatientButton.setEnabled(false);

                if (patientName.getText().toString().equals("")) {
                    patientName.setError("Enter a Valid Name");
                }
                if (address.getText().toString().equals("")) {
                    address.setError("Enter a Valid Name");
                }
                if (age.getText().toString().equals("")) {
                    age.setError("Enter Valid Number");
                }
                if (months.getText().toString().equals("")) {
                    months.setError("Enter Valid Number");
                } else {
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
                    String simpleDate = year + "-" + mn + "-" + dy;
                    NeighbourPatient neighbourPatient = new NeighbourPatient();
                    neighbourPatient.setPtName(patientName.getText().toString());
                    neighbourPatient.setAddress(address.getText().toString());
                    neighbourPatient.setDistrict(String.valueOf(dpdhsmap.get(dist.getSelectedItem().toString())));
                    neighbourPatient.setGnId(String.valueOf(gnmap.get(gn.getSelectedItem().toString())));
                    neighbourPatient.setMoh(String.valueOf(mohmap.get(moh.getSelectedItem().toString())));
                    neighbourPatient.setPhi(String.valueOf(phimap.get(phi.getSelectedItem().toString())));
                    neighbourPatient.setPtAge(String.valueOf(Integer.parseInt(age.getText().toString()) * 12 + Integer.parseInt(months.getText().toString())));
                    neighbourPatient.setPtDateAdd(simpleDate);
                    neighbourPatient.setPtRem(remarks.getText().toString());
                    neighbourPatient.setPtSex(String.valueOf(gender.getSelectedItemPosition()));
                    NetworkCore.submitPatient(AddNewPatientForm.this, neighbourPatient);

                }


            }

        });
//        capturePhotoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });

        age.addTextChangedListener(new ValidationModel(age) {
            @Override
            public void validate(TextView textView, String text) {
                try {
                    int ageValue = Integer.parseInt(text);
                    if (ageValue < 0 || ageValue > 120) {
                        textView.setError("Please Enter a valid number between 0 and 120");
                    }
                } catch (NumberFormatException e) {
                    textView.setError("Please Enter a valid age");
                }
            }
        });
        months.addTextChangedListener(new ValidationModel(months) {
            @Override
            public void validate(TextView textView, String text) {
                try {
                    int months = Integer.parseInt(text)-1;
                    if (months < 1 || months > 12) {
                        textView.setError("Please Enter a valid number between 1 and 12");
                    }
                } catch (NumberFormatException e) {
                    textView.setError("Please Enter a valid number");

                }
            }
        });


    }

    public void  enableSubmitButton(){
        final Button submit =(Button) findViewById(R.id.submitPatient);
        submit.setEnabled(true);


    }

    private void getGNList(HashMap<String, Integer> map, Spinner gn, int dpdhsc) {
        map.clear();
        NetworkCore.getGNList(map, gn, this, dpdhsc);
    }

    private void getPHIList(HashMap<String, Integer> map, Spinner phi, int id) {
        map.clear();
        NetworkCore.getPHIList(map, phi, this, id);
    }

    private void getMOHList(HashMap<String, Integer> map, Spinner moh, int dpdhsc) {
        map.clear();
        NetworkCore.getMOHList(map, moh, this, dpdhsc);
    }

    private void getDistrictList(HashMap<String, Integer> map, Spinner dist) {
        map.clear();
        NetworkCore.getAllDistrict(map, dist, this);
    }


    public void selectImage() {
        dialog = new MaterialDialog.Builder(this)
                .title("Capture Photo")
                .customView(R.layout.custom_image_select, true)
                .positiveText("OK")
                .build();
        dialog.show();
        Button takeNewPhotoButton = (Button) dialog.getCustomView().findViewById(R.id.takeNewPhotoButton);
        takeNewPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, TAKE_PICTURE);
            }
        });
        Button chooseFromGalleryButton = (Button) dialog.getCustomView().findViewById(R.id.chooseFromGalleryButton);
        chooseFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == TAKE_PICTURE) {
                Bitmap photo = (Bitmap) data.getExtras().get(getString(R.string.data));
                Drawable drawable = new BitmapDrawable(photo);
                ImageView imageView = (ImageView) dialog.getCustomView().findViewById(R.id.takePhotoImageView);
                imageView.setImageDrawable(drawable);

            } else if (requestCode == ACTIVITY_SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Captured Image Path " + path);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        Log.e(TAG, "Error occurred due to " + e.getMessage());
                    }
                    new ImageCompressor().execute(bitmap);
                }
            }
        }
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }

    private class ImageCompressor extends AsyncTask<Object, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Object... params) {
            Bitmap bitmap = (Bitmap) params[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) dialog.getCustomView().findViewById(R.id.takePhotoImageView);
            imageView.setImageBitmap(bitmap);
            Log.i(TAG, "Image Compression Complete");
        }
    }
}