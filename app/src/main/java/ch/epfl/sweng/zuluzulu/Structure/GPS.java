package ch.epfl.sweng.zuluzulu.Structure;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Static class that represents the GPS
 */
public final class GPS {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final long MIN_DISTANCE_TO_REQUEST_LOCATION = 5; // In meters
    private static final long MIN_TIME_FOR_UPDATES = 3000; // 1 sec
    private static final int TWO_MINUTES = 1000 * 60 * 2; // 2 min
    private static Context mcontext;
    private static Location location;
    private static LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location newLocation) {
            if (newLocation != null && isBetterLocation(newLocation, location)) {
                location = newLocation;
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
    };
    private static LocationManager locationManager;

    private static boolean isActivated = false;

    private GPS() {
    }

    /**
     * Start requesting for location updates
     *
     * @return Whether the user has given permission or not
     */
    public static boolean start(Context context) {
        mcontext = context;
        if (ContextCompat.checkSelfPermission(mcontext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mcontext, "Permission au GPS non donnée", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            locationManager = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_TO_REQUEST_LOCATION, locationListener);
                    Location tempLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (tempLocation != null && isBetterLocation(tempLocation, location)) {
                        location = tempLocation;
                    }
                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_TO_REQUEST_LOCATION, locationListener);
                    Location tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (tempLocation != null && isBetterLocation(tempLocation, location)) {
                        location = tempLocation;
                    }
                }
                isActivated = isGPSEnabled || isNetworkEnabled;
                if (!isActivated) {
                    Toast.makeText(mcontext, "Active le GPS pour avoir accès à toutes les options", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Location manager", "Cannot getAndAddOnSuccessListener location manager");
            }
        }
        return true;
    }

    /**
     * Stop asking for location updates
     */
    public static void stop() {
        isActivated = false;
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    /**
     * Return whether the location tracker is activated or not
     *
     * @return whether the location tracker is activated or not
     */
    public static boolean isActivated() {
        return isActivated;
    }

    /**
     * Return the last known location
     *
     * @return The last known location
     */
    public static Location getLocation() {
        return location;
    }

    /**
     * Check whether a location is better than the current one
     *
     * @param location            The new location to test
     * @param currentBestLocation The current location
     * @return whether the new location is better or not
     */
    private static boolean isBetterLocation(Location location, Location currentBestLocation) {
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
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /**
     * Return the location listener
     *
     * @return The location listener
     */
    public static LocationListener getListener() {
        return locationListener;
    }
}
