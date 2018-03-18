package com.densysMobile.android.dengue_phi_client;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.densysMobile.android.dengue_phi_client.Network.NetworkCore;
import com.densysMobile.android.dengue_phi_client.Util.Util;


public class Login extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        setContentView(R.layout.activity_login);
        Button button = (Button) findViewById(R.id.loginButton);
        final TextInputEditText usernameText = (TextInputEditText) findViewById(R.id.usernameEditText);
        final TextInputEditText passwordText = (TextInputEditText) findViewById(R.id.passwordEditText);

        NetworkCore.checkLoginStatus(this, MainActivity.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameText.getText().toString();
                final String password = passwordText.getText().toString();
                String hashedPassword = Util.md5(password);
                Log.i(TAG, "Attempting login with Username " + username);
                NetworkCore.login(username, hashedPassword, usernameText, passwordText, Login.this);

            }
        });
    }


}
