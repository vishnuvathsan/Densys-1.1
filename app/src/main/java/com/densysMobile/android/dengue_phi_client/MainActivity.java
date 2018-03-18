package com.densysMobile.android.dengue_phi_client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.densysMobile.android.dengue_phi_client.Network.NetworkCore;
import com.densysMobile.android.dengue_phi_client.Prefs.AppPreferenceHelper;
import com.densysMobile.android.dengue_phi_client.Util.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = getClass().getSimpleName();
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button searchPatientButton = (Button) findViewById(R.id.searchPatientButton);
        Button addNewPatientButton = (Button) findViewById(R.id.addNewPatient);
        Button myAccountButton = (Button) findViewById(R.id.myAccountButton);
//        Button aboutUsButton =(Button) findViewById(R.id.aboutUs);

        searchPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PatientDetails.class));


            }
        });

        addNewPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNewPatientForm.class));

            }
        });

        myAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == R.id.add_patient) {
            startActivity(new Intent(MainActivity.this, AddNewPatientForm.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this, PatientDetails.class));
        } else if (id == R.id.logout) {
            new MaterialDialog.Builder(MainActivity.this)
                    .title("Log out")
                    .content("Are you sure you want to logout?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            AppPreferenceHelper apiKeyPrefHelper = new AppPreferenceHelper(MainActivity.this, AppPreferenceHelper.PREF_KEY_API_KEY);
                            AppPreferenceHelper jwtPrefHelper = new AppPreferenceHelper(MainActivity.this, AppPreferenceHelper.PREF_KEY_JWT);
                            apiKeyPrefHelper.setApiKey("");
                            jwtPrefHelper.setJwt("");
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else if (id == R.id.changePass) {

            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title("Change Password")
                    .customView(R.layout.custom_change_password_layout, true)
                    .autoDismiss(false)
                    .positiveText("OK")
                    .negativeText("Cancel")
                    .neutralText("CLEAR")
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            EditText currentPassET = (EditText) dialog.getCustomView().findViewById(R.id.currentPasswordEditText);
                            EditText newPassAlphaET = (EditText) dialog.getCustomView().findViewById(R.id.newPasswordAlphaEditText);
                            EditText newPassBetaET = (EditText) dialog.getCustomView().findViewById(R.id.newPasswordBetaEditText);
                            currentPassET.setText("");
                            newPassAlphaET.setText("");
                            newPassBetaET.setText("");

                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Boolean isGoodToGo = true;
                            EditText currentPass = (EditText) dialog.getCustomView().findViewById(R.id.currentPasswordEditText);
                            EditText newPassAlpha = (EditText) dialog.getCustomView().findViewById(R.id.newPasswordAlphaEditText);
                            EditText newPassBeta = (EditText) dialog.getCustomView().findViewById(R.id.newPasswordBetaEditText);
                            String alphaPass = newPassAlpha.getText().toString();
                            String betaPass = newPassBeta.getText().toString();

                            if (!alphaPass.equals(betaPass)) {
                                isGoodToGo = false;
                                Toast.makeText(MainActivity.this, "New Password Mismatch", Toast.LENGTH_SHORT).show();
                            }
                            if (isGoodToGo) {
                                String hash = Util.md5(alphaPass);
                                String currentPassHash = Util.md5(currentPass.getText().toString());
                                NetworkCore.resetPassword(currentPassHash, hash, MainActivity.this);
                                dialog.dismiss();
                            }

                        }
                    })
                    .build();
            dialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
