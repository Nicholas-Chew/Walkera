package com.ntu.powerranger.walkera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import facade.API;
import facade.PermissionUtils;
import facade.stateControl;


public class SignupActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String postURL;
    private Context c;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, @StyleRes int resid, boolean first) {
        super.onApplyThemeResource(theme, resid, first);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        enableMyLocation();
        postURL = getResources().getString(R.string.apiURL) + "user/fblogin";

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.signup);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("email","user_birthday"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final HashMap<String,String> postParam = new HashMap<String, String>();
                postParam.put("fbAccessToken",loginResult.getAccessToken().getToken());


                StringRequest req = new StringRequest(Request.Method.POST, postURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Map<String,Object> arr = API.praseRespond(response);
                                stateControl.saveData(c, "token", (String)arr.get("token"));

                                Intent myIntent = new Intent(SignupActivity.this, BMIInputActivity.class);
                                SignupActivity.this.startActivity(myIntent);

                            }
                        }, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                 Log.e("Error: ",error.toString());
                             }
                         })
                {


                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }

                    // this is the relevant method
                    @Override
                    protected Map<String, String> getParams () throws AuthFailureError
                    {
                        return postParam;
                    }
                };

                ApplicationController.getInstance().addToRequestQueue(req);

            }

            @Override
            public void onCancel() {
                //display in short period of time
                Toast.makeText(getApplicationContext(), "Login attempt cancelled.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                //display in short period of time
                Toast.makeText(getApplicationContext(), "Login attempt failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void emailLoginIn(View view) {
        Intent myIntent = new Intent(SignupActivity.this, EmailLogin.class);
        SignupActivity.this.startActivity(myIntent);
    }

    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);

        }
        else
        {
            if(!stateControl.getStringData(c,"token").equals(""))
            {
                Intent myIntent = new Intent(SignupActivity.this, MainActivity.class);
                SignupActivity.this.startActivity(myIntent);
            }
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        }

    }
}