package ca.mobile.restaurantguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    List<RestaurantDatabase> restaurantList;
    SQLiteDatabase mDatabase;
    ListView listViewRestaurant;
    RestaurantAdapter adapter;
    public static final String TABLE_NAME = "restaurants";
    EditText editTextSearch;
    List<RestaurantDatabase> filtered = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

//        get user current location
        getLocation(savedInstanceState);

        listViewRestaurant = findViewById(R.id.listViewRestaurants);
        restaurantList = new ArrayList<>();

        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        editTextSearch = (EditText) findViewById(R.id.search_box);

        //this method will display the Restaurants in the list
        showRestaurantsFromDatabase();

        editTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    adapter.searchRest(s.toString(), filtered);
                } else {
                    adapter.showOriginal(filtered);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });


    }

    private void showRestaurantsFromDatabase() {

        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorRestaurants = mDatabase.rawQuery("SELECT * FROM TABLE_NAME", null);

        //if the cursor has some data
        if (cursorRestaurants.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                restaurantList.add(new RestaurantDatabase(
                        cursorRestaurants.getInt(0),
                        cursorRestaurants.getString(1),
                        cursorRestaurants.getString(2),
                        cursorRestaurants.getString(3),
                        cursorRestaurants.getString(4),
                        cursorRestaurants.getDouble(5)
                ));
            } while (cursorRestaurants.moveToNext());
        }
        //closing the cursor
        cursorRestaurants.close();

        //creating the adapter object

        filtered.addAll(restaurantList);
        adapter = new RestaurantAdapter(this, R.layout.list_layout_restaurant, restaurantList, restaurantList, mDatabase);

        //adding the adapter to listview
        listViewRestaurant.setAdapter(adapter);
    }

//    start location code here below

    // location last updated time
    private String mLastUpdateTime = null;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient = null;
    private SettingsClient mSettingsClient = null;
    private LocationRequest mLocationRequest = null;
    private LocationSettingsRequest mLocationSettingsRequest = null;
    private LocationCallback mLocationCallback = null;
    private Location mCurrentLocation = null;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates = null;

    final int REQUEST_PERMISSION_KEY = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    String TAG = "theLocation";

    public static double Latitude = 0.0;
    public static double Longitude = 0.0;


    private static long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static int REQUEST_CHECK_SETTINGS = 100;


    void getLocation(Bundle outState) {

        init();

        restoreValuesFromBundle(outState);

        startLocationButtonClick();

        if (mCurrentLocation != null) {
            Latitude = mCurrentLocation.getLatitude();
            Longitude = mCurrentLocation.getLongitude();
            Log.d(TAG, "onCreate: Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude());
        } else {
            Log.d(TAG, "onCreate: Lat and Lng is null");
        }
    }


    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                if (mCurrentLocation != null) {
                    Latitude = mCurrentLocation.getLatitude();
                    Longitude = mCurrentLocation.getLongitude();
                    Log.d(TAG, "init: Lat: " + Latitude + ", " + "Lng: " + Longitude);
                } else {
                    Log.d(TAG, "init: Lat and Lng is null");
                }
            }
        };


        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        showLastKnownLocation();
    }

    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        if (mCurrentLocation != null) {
            Latitude = mCurrentLocation.getLatitude();
            Longitude = mCurrentLocation.getLongitude();
            Log.d(TAG, "restoreValuesFromBundle: Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude());
        } else {
            Log.d(TAG, "restoreValuesFromBundle: Lat and Lng is null");
        }
    }


    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        if (hasPermissions(this, PERMISSIONS)) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        } else {
//            openSettings();
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }

        if (mCurrentLocation != null) {
            Latitude = mCurrentLocation.getLatitude();
            Longitude = mCurrentLocation.getLongitude();
            Log.d(TAG, "startLocationButtonClick: Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude());
        } else {
            Log.d(TAG, "startLocationButtonClick: Lat and Lng is null");
        }
    }

    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.d(TAG, "All location settings are satisfied.");
                        Log.d(TAG, "onSuccess: Started location updates!");
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.d(TAG, "Location settings are not satisfied. Attempting to upgrade location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(RestaurantActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.d(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings.";
                                Log.d(TAG, "errorMessage: " + errorMessage);

                        }

                    }
                });
    }


    public void showLastKnownLocation() {
        if (mCurrentLocation != null) {
            Latitude = mCurrentLocation.getLatitude();
            Longitude = mCurrentLocation.getLongitude();
            Log.d(TAG, "showLastKnownLocation: Lat: " + mCurrentLocation.getLatitude()
                    + ", Lng: " + mCurrentLocation.getLongitude());
        } else {
            Log.d(TAG, "showLastKnownLocation: Last known location is not available!");
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: Location updates stopped!");
                    }
                });
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationButtonClick();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_KEY: {

                for (int i = 0; i < grantResults.length; i++) {
                    Log.d(TAG, "onRequestPermissionsResult: Perm " + i + (grantResults[i] == PackageManager.PERMISSION_GRANTED));
                }

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: Granted");
                    mRequestingLocationUpdates = true;
                    startLocationUpdates();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: Not granted");
                }

            }
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {

        Log.d("theH", "hasPermissions: permission size= " + permissions.length);

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                continue;
            } else {
                Log.d("theH", "hasPermissions: returning false");
                return false;
            }
        }
        Log.d("theH", "hasPermissions: returning true");
        return true;
    }

}