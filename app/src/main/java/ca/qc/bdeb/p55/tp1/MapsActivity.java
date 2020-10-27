package ca.qc.bdeb.p55.tp1;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener {
    private static final String TAG = "ca.qc.bdeb.p55.tp1";

    public static final String EXTRA_LatLng = "ca.qc.bdeb.p55.esteban.labo2.EXTRA_LatLng";
    public static final String EXTRA_RESULTAT_LIEUX = "ca.qc.bdeb.p55.intents.EXTRA_RESULTAT_LIEUX";
    public static final int ENTREE_INFO_RESULT = 1;


    private static final int LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private double latitude = 0;
    private double longitude = 0;

    public static final int REQUEST_TAKE_PHOTO = 1;

    private ImageView imageView;
    private Button photoButton;

    private String currentPhotoPath;

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
        //  Toast.makeText(this, "onMyLocationClick \n" + latLng, Toast.LENGTH_LONG).show();

        // Creating a marker
  /*      MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

        // Clears the previously touched position
        mMap.clear();

        // Animating to the touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);*/


        Intent intent = new Intent(this, EntreesUtilisateurs.class);
        intent.putExtra(EXTRA_LatLng, latLng);
        startActivityForResult(intent, ENTREE_INFO_RESULT);

    /*    mMap.addMarker(new MarkerOptions().position(latLng)
                .title("Position Coureur")
                .snippet("Départ"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        DBHelper dbHelper = DBHelper.getInstance(this);

        Lieux lieu = new Lieux("lieuxTest", latLng.latitude, latLng.longitude, 2, "156546", 1, 1, 4);

        dbHelper.ajouterLieux(lieu);*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENTREE_INFO_RESULT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                Lieux lieu = data.getParcelableExtra(MapsActivity.EXTRA_RESULTAT_LIEUX);


           //     LatLng pos = new LatLng(lieu.getLatitude(), lieu.getLongitude());
                LatLng pos = new LatLng(45.5,-73.72);

                mMap.addMarker(new MarkerOptions().position(pos)
                        .title(lieu.getNom()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));


                DBHelper dbHelper = DBHelper.getInstance(this);
                dbHelper.ajouterLieux(lieu);

            }
        }
    }

    private void loadLieux() {

        DBHelper dbHelper = DBHelper.getInstance(this);

        for (Lieux lieu : dbHelper.getToutLesLieux()) {
            LatLng pos = new LatLng(lieu.getLatitude(), lieu.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pos)
                    .title(lieu.getNom()));
        }

    }
}