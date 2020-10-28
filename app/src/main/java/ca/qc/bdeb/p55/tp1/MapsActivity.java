package ca.qc.bdeb.p55.tp1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener {
    private static final String TAG = "ca.qc.bdeb.p55.tp1";

    public static final String EXTRA_LATLNG = "ca.qc.bdeb.p55.esteban.labo2.EXTRA_LatLng";
    public static final String EXTRA_RESULTAT_LIEUX = "ca.qc.bdeb.p55.intents.EXTRA_RESULTAT_LIEUX";
    public static final int ENTREE_INFO_RESULT = 1;


    private static final int LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMapClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Demande de permissions:
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            activeLocalisation();
        }
        loadLieux();
    }

    @SuppressLint("MissingPermission")
    private void activeLocalisation() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.i(TAG, "Ma localisation " + location);
                        addMarkerToPosition();
                    }
                }
            });
        }
    }

    private void addMarkerToPosition() {
        LatLng pos = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(pos)
                .title("Position Coureur")
                .snippet("Départ")
                .icon(vectorToBitmap(R.drawable.ic_baseline_arrow_drop_down_circle_24, Color.parseColor("#FF0000"))));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));
    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {

        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (isPermissionAuth(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    isPermissionAuth(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                activeLocalisation();
            }
        }
    }

    private boolean isPermissionAuth(String[] permissions, int[] grantResults, String permission) {
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].compareToIgnoreCase(permission) == 0) {
                return (grantResults[i] == PackageManager.PERMISSION_GRANTED);
            }
        }
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //  Toast.makeText(this, "onMyLocationButtonClick", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //      Toast.makeText(this, "onMyLocationClick \n" + location, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(this, EntreesUtilisateurs.class);
        intent.putExtra(EXTRA_LATLNG, latLng);
        startActivityForResult(intent, ENTREE_INFO_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENTREE_INFO_RESULT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                Lieux lieu = data.getParcelableExtra(MapsActivity.EXTRA_RESULTAT_LIEUX);


                ajouterMarqueur(lieu);

                DBHelper dbHelper = DBHelper.getInstance(this);
                dbHelper.ajouterLieux(lieu);
            }
        }
    }

    private void loadLieux() {

        DBHelper dbHelper = DBHelper.getInstance(this);

        for (Lieux lieu : dbHelper.getToutLesLieux()) {

            ajouterMarqueur(lieu);

        }

    }


    private void ajouterMarqueur(Lieux lieu) {
        LatLng pos = new LatLng(lieu.getLatitude(), lieu.getLongitude());

        switch (lieu.getType()) {
            case 1:
                mMap.addMarker(new MarkerOptions().position(pos)
                        .title(lieu.getNom())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));
                break;
            case 2:
                mMap.addMarker(new MarkerOptions().position(pos)
                        .title(lieu.getNom())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));
                break;
            case 3:
                mMap.addMarker(new MarkerOptions().position(pos)
                        .title(lieu.getNom())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));
                break;
        }

    }
}