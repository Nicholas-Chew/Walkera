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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import facade.API;
import facade.stateControl;

/**
 * A login screen that offers login via email/password.
 */
public class EmailLoginDetail extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mNameView;
    private EditText mAgeView;
    private View mProgressView;
    private View mLoginFormView;

    private String height;
    private String weight;

    //User Detail
    private String gender;

    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emaillogindetail);
        c = this;
        // Set up the login form.
        mNameView = (AutoCompleteTextView) findViewById(R.id.name);

        mAgeView = (EditText) findViewById(R.id.age);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mNameView.setError(null);
        mAgeView.setError(null);


        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String age = mAgeView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(age) && !isAgeValid(age)) {
            mAgeView.setError(getString(R.string.error_invalid_password));
            focusView = mAgeView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
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
            mAuthTask = new UserLoginTask(age, name);
            mAuthTask.execute((Void) null);
        }
    }


    private boolean isAgeValid(String age) {
        return Integer.parseInt(age) < 120;
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


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mAge;

        UserLoginTask(String email, String password) {
            mName = password;
            mAge = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String postURL = getResources().getString(R.string.apiURL) + "user/updateDetail";


            final HashMap<String,String> postParam = new HashMap<String, String>();
            postParam.put("name",mName);
            postParam.put("age",mAge);
            postParam.put("gender", gender);
            postParam.put("token", stateControl.getStringData(c,"token"));


            StringRequest req = new StringRequest(Request.Method.POST, postURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mAuthTask = null;
                            showProgress(false);
                            Map<String,Object> arr = API.praseRespond(response);

                            Intent myIntent = new Intent(EmailLoginDetail.this, BMIInputActivity.class);
                            EmailLoginDetail.this.startActivity(myIntent);


                        }
                    }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    mAuthTask = null;
                    showProgress(false);
                    String errorS = new String(error.networkResponse.data);
                    Map<String,Object> arr = API.praseRespond(errorS);
                    mNameView.setError((String)arr.get("message"));
                    mNameView.requestFocus();
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
                mAgeView.setError(getString(R.string.error_incorrect_password));
                mAgeView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void maleBtnClicked (View view)
    {
        gender = "M";
    }

    public void femaleBtnClicked(View view)
    {
        gender = "F";
    }
}

