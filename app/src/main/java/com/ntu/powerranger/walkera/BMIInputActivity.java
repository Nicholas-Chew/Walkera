package com.ntu.powerranger.walkera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import facade.API;
import facade.stateControl;

/**
 * A login screen that offers login via weight/height.
 */
public class BMIInputActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private Context c;

    // UI references.
    private AutoCompleteTextView mWeightView;
    private EditText mHeightView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmiinput);
        c = this;
        // Set up the login form.
        mWeightView = (AutoCompleteTextView) findViewById(R.id.weight);
        mHeightView = (EditText) findViewById(R.id.height);


        mHeightView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mweightSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mweightSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mWeightView.setError(null);
        mHeightView.setError(null);

        // Store values at the time of the login attempt.
        String weight = mWeightView.getText().toString();
        String height = mHeightView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid height, if the user entered one.
        if (!TextUtils.isEmpty(height) && !isHeightValid(height)) {
            mHeightView.setError(getString(R.string.error_invalid_height));
            focusView = mHeightView;
            cancel = true;
        }

        // Check for a valid weight address.
        if (TextUtils.isEmpty(weight)) {
            mWeightView.setError(getString(R.string.error_field_required));
            focusView = mWeightView;
            cancel = true;
        } else if (!isWeightValid(weight)) {
            mWeightView.setError(getString(R.string.error_invalid_weight));
            focusView = mWeightView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(weight, height);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isHeightValid(String weight) {
        return weight.length() < 5;
    }

    private boolean isWeightValid(String height) {
        return height.length() < 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    private void addweightsToAutoComplete(List<String> weightAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(BMIInputActivity.this,
                        android.R.layout.simple_dropdown_item_1line, weightAddressCollection);

        mWeightView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mweight;
        private final String mheight;

        UserLoginTask(String weight, String height) {
            mweight = weight;
            mheight = height;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //HTTP Call TO API Server
            String postURL = getResources().getString(R.string.apiURL) + "user/BMI/updateBMI";

            final HashMap<String,String> postParam = new HashMap<String, String>();
            postParam.put("weight",mweight);
            postParam.put("height",mheight);
            postParam.put("token", stateControl.getStringData(c,"token"));


            StringRequest req = new StringRequest(Request.Method.POST, postURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Map<String,Object> arr = API.praseRespond(response);
                            Intent myIntent = new Intent(BMIInputActivity.this, MainActivity.class);
                            BMIInputActivity.this.startActivity(myIntent);

                            //Push to new View
                        }
                    }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    mAuthTask = null;
                    showProgress(false);
                    String errorS = new String(error.networkResponse.data);
                    Map<String,Object> arr = API.praseRespond(errorS);
                    mWeightView.setError((String)arr.get("message"));
                    mWeightView.requestFocus();
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


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            //showProgress(false);

            if (success) {
                //finish();
            } else {
                mHeightView.setError(getString(R.string.error_invalid_email));
                mHeightView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

