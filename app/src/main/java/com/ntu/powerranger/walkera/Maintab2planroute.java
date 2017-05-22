package com.ntu.powerranger.walkera;

/**
 * Created by nicho on 3/17/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class Maintab2planroute extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback, View.OnClickListener, DirectionCallback {

    private String serverKey = "AIzaSyAHmWAR156B5NTS7I9xOjByayNkGWWXl70";
    private LatLng camera = new LatLng(1.3521, 103.8198);
    private LatLng startPoint ;
    private LatLng destination = new LatLng(1.3725, 103.8938);
    private View rootView;

    private MapView mMapView;
    private GoogleMap googleMap;

    private boolean walking;

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    //Buttons
    private Button startBtn;
    private Button destinationBtn;
    private int ClickbtnIndex;

    //Steps wanted to walk
    //Seek Bar
    private DiscreteSeekBar stepSeekBar;
    private TextView stepWantToWalk;
    private int stepsWanted = 10000;


    public static String method;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.maintab2planroute, container, false);

        startBtn = (Button) rootView.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
                ClickbtnIndex = startBtn.getId();
            }
        });
        destinationBtn = (Button) rootView.findViewById(R.id.destinationBtn);
        destinationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
                ClickbtnIndex = destinationBtn.getId();
            }
        });

        stepWantToWalk = (TextView) rootView.findViewById(R.id.stepWantToWalk);

        stepSeekBar = (DiscreteSeekBar) rootView.findViewById(R.id.stepSeekBar);

        stepSeekBar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                stepsWanted = value;
                stepWantToWalk.setText("Step Want to Walk: "+ String.format("%.2f", value*0.0008)+"KM");
                return value;
            }

        });

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        return rootView;
    }




    //Google Map API Functions
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setOnMyLocationButtonClickListener(this);
        //googleMap.getUiSettings().setZoomControlsEnabled(true);
        enableMyLocation();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, (float)10.5));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    //
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(Maintab1landing.stepCounts < 12500)
            {
                stepsWanted = 12500 - Maintab1landing.stepCounts;
                stepSeekBar.setProgress(stepsWanted);
            }

            if(method!=null)
            {
                walking = true;
                if(method.equals("Walk Now"))
                    GoogleDirection.withServerKey(serverKey)
                        .from(new LatLng(googleMap.getMyLocation().getLatitude()
                                , googleMap.getMyLocation().getLongitude()))
                        .to(destination)
                        .transportMode(TransportMode.WALKING)
                        .execute(this);

                //Focus to current location
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(googleMap.getMyLocation().getLatitude()-0.001
                                , googleMap.getMyLocation().getLongitude()), 18));
            }
        }
        else {

        }
    }


    //Direction Functions
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Log.e("onDirectionSuccess",direction.getStatus());
        int count = -1;
        int tempCount;
        int sumDistance = 0;

        if (direction.isOK()) {
                List<Step> sectionPositionList = direction.getRouteList().get(0).getLegList().get(0).getStepList();

            if(walking)
            {
                count = 0;

                for (Step step : sectionPositionList) {

                    sumDistance += Integer.parseInt(step.getDistance().getValue());

                    count++;
                    if(sumDistance > stepsWanted*0.8)
                        break;
                }
            }


            tempCount = count;

            //Drawing the polyline
            List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();
            ArrayList<PolylineOptions> polylineOptionList;

            if(!walking)
                polylineOptionList = DirectionConverter.createTransitPolyline(rootView.getContext(), stepList, 5, Color.RED, 3, Color.BLUE);
            else
                polylineOptionList = DirectionConverter.createTransitPolyline(rootView.getContext(), stepList, 3, Color.BLUE, 5, Color.RED);

            for (PolylineOptions polylineOption : polylineOptionList) {

                if(walking)
                {
                    tempCount--;
                    if(tempCount<1) {
                        break;
                    }
                }

                googleMap.addPolyline(polylineOption);
            }


            tempCount = count;

            //Adding the "dot"
            for (Step step : sectionPositionList) {

                if(walking)
                {
                    tempCount--;
                    if(tempCount<1) {
                        walking = false;
                        GoogleDirection.withServerKey(serverKey)
                                .from(new LatLng(step.getStartLocation().getLatitude(), step.getStartLocation().getLongitude()))
                                .to(destination)
                                .transportMode(TransportMode.TRANSIT)
                                .execute(this);
                        break;
                    }
                }

                MarkerOptions MO = new MarkerOptions()
                        .position(new LatLng(step.getStartLocation().getLatitude(), step.getStartLocation().getLongitude()))
                        .title(step.getHtmlInstruction());

                if(step.getTransitDetail()!= null)
                    MO.snippet(step.getTransitDetail().getLine().getShortName());

                googleMap.addMarker(MO);

            }
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Log.e("Map Direction Failure",t.getMessage());
    }


    //Current Direction
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            googleMap.setMyLocationEnabled(false);
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);
            googleMap.setPadding(0,1300,20,0);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(rootView.getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();


        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(googleMap.getMyLocation().getLatitude()-0.001
                , googleMap.getMyLocation().getLongitude()), 18));
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return true;
    }


    //Location Auto Complete
    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e("openAutoComplete", message);
            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e)
        {
            Log.e("openAutoComplete", e.getMessage());
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i("AutoComplete Resuly", "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                Spanned placeName = formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri());
                ((Button)rootView.findViewById(ClickbtnIndex)).setText(placeName.toString());
                ((Button)rootView.findViewById(ClickbtnIndex)).setTextColor(Color.BLACK);
                ((Button)rootView.findViewById(ClickbtnIndex)).setTextSize(14);

                if(startBtn == rootView.findViewById(ClickbtnIndex))
                {
                    startPoint = place.getLatLng();
                    if(destination != null)
                    {
                        walking = true;
                        GoogleDirection.withServerKey(serverKey)
                                .from(startPoint)
                                .to(destination)
                                .transportMode(TransportMode.WALKING)
                                .execute(this);

                        //Focus to current location
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startPoint.latitude-0.001
                                , startPoint.longitude), 14));
                    }
                }

                if(destinationBtn == rootView.findViewById(ClickbtnIndex) )
                {
                    destination = place.getLatLng();
                    if(startPoint == null)
                        startPoint = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()) ;
                    if(startPoint != null)
                    {
                        walking = true;
                        GoogleDirection.withServerKey(serverKey)
                                .from(startPoint)
                                .to(destination)
                                .transportMode(TransportMode.WALKING)
                                .execute(this);

                        //Focus to current location
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startPoint.latitude-0.001
                                , startPoint.longitude), 14));
                    }

                }

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e("return",status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e("Output", res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_name, name));

    }
}
