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
import android.util.Log;

public class GPS {

    private static final int MY_PERMISSIONS_REQUEST_GPS = 1;
    private Context context;
    private Location mlocation;
    private LocationManager locationManager;
    private String provider = LocationManager.GPS_PROVIDER;

    public GPS(Context context) {

        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Check that we have all the required permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_GPS);
        } else {
            locationManager.requestLocationUpdates(provider, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("Location change", location.toString());
                    mlocation = location;
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
            });
        }
    }

    public Location getLocation() {
        if (mlocation == null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mlocation = locationManager.getLastKnownLocation(provider);
            }
        }

        if (mlocation == null) {
            mlocation = new Location(provider);
            mlocation.setLatitude(0);
            mlocation.setLongitude(0);
            mlocation.setAltitude(0);
        }

        return mlocation;
    }
}
