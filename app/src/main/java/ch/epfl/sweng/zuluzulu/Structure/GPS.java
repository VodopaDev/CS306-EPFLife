package ch.epfl.sweng.zuluzulu.Structure;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

public final class GPS implements LocationListener {

    private static volatile GPS instance = null;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final long MIN_DISTANCE_TO_REQUEST_LOCATION = 1; // In meters
    private static final long MIN_TIME_FOR_UPDATES = 1000; // 1 sec
    private static final int TWO_MINUTES = 1000 * 60 * 2; // 2 min

    private Context context;
    Location location;
    LocationManager locationManager;

    private boolean isWorking = false;

    private GPS(Context context) {
        super();
        this.context = context;
    }

    public final static GPS getInstance(Context context) {
        if (instance == null) {
            synchronized (GPS.class) {
                if (instance == null) {
                    instance = new GPS(context);
                }
            }
        }
        instance.setContext(context);
        return instance;
    }

    public void start() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission to GPS not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                isWorking = isGPSEnabled || isNetworkEnabled;
                if (isGPSEnabled) {
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_TO_REQUEST_LOCATION, this);
                    Location tempLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (tempLocation != null && isBetterLocation(tempLocation, location)) {
                        location = tempLocation;
                    }
                } else if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_TO_REQUEST_LOCATION, this);
                    Location tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (tempLocation != null && isBetterLocation(tempLocation, location)) {
                        location = tempLocation;
                    }
                } else {
                    Toast.makeText(context, "Please activate your GPS to have access to all features", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Location manager", "Cannot get location manager");
            }
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
        if (location != null && isBetterLocation(location, this.location)) {
            this.location = location;
        }
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

    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        return isMoreAccurate || (isNewer && !isLessAccurate) || (isNewer && !isSignificantlyLessAccurate && isFromSameProvider);
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void setContext(Context context) {
        this.context = context;
    }
}
