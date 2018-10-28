package ch.epfl.sweng.zuluzulu.Structure;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

public class GPS implements LocationListener {

    private static final long MIN_DISTANCE_TO_REQUEST_LOCATION = 1; // In meters
    private static final long MIN_TIME_FOR_UPDATES = 1000; // 1 sec

    private Context context;
    Location location;
    LocationManager locationManager;

    private boolean isWorking = false;

    public GPS(Context context) {
        super();
        this.context = context;
    }

    public void start() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission to GPS not granted", Toast.LENGTH_SHORT).show();
        }
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                isWorking = true;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_TO_REQUEST_LOCATION, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_TO_REQUEST_LOCATION, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                Toast.makeText(context, "Please enable GPS to have access to all features", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            isWorking = false;
        }
    }

    public Location getLocation() {
        return location;
    }

    public boolean isWorking() {
        return isWorking;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
