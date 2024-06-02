package org.maziarz.org;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {
    private static final String TAG = "LocationService";
    private FusedLocationProviderClient fusedLocationClient;
    public static MutableLiveData<Location> locationLiveData = new MutableLiveData<>();

    private LocationApi locationApi;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startLocationUpdates();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://org.maziarz.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        locationApi = retrofit.create(LocationApi.class);
    }

    private void sendLocationToServer(LocationData locationData) {
        Call<Void> call = locationApi.sendLocation(locationData);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Location sent successfully");
                } else {
                    Log.e(TAG, "Failed to send location: " + response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error sending location: " + t.getMessage());
            }
        });
    }


    private void startLocationUpdates() {
        LocationRequest.Builder builder =
                new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000);
        builder.setMinUpdateIntervalMillis(5000);
        LocationRequest locationRequest = builder.build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("LocationService", "No access permissions");
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    Log.i(TAG, "loc: "+ location.toString());

                    locationLiveData.postValue(location);
                    LocationData locationData = new LocationData(location.getLatitude(), location.getLongitude());
                    Log.i(TAG, "loc: "+ locationData.toString());
                    sendLocationToServer(locationData);
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}

