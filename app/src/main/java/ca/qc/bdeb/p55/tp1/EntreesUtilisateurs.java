package ca.qc.bdeb.p55.tp1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EntreesUtilisateurs extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;

    private EditText editTextNom;
    private EditText editTextTelephone;
    private EditText editVisites;
    private RadioGroup radioGroup;
    private RadioButton radioButtonType;
    private ImageView imageLieu;

    private LatLng latLng;

    private String currentPhotoPath;
    private Bitmap bitmapPhoto;

    private int nombreVisites = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entree_infos_lieux);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editTextNom = findViewById(R.id.editTextNom);
        editTextTelephone = findViewById(R.id.editTextTelephone);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonType = findViewById(radioGroup.getCheckedRadioButtonId());
        imageLieu = findViewById(R.id.imageLieu);
        editVisites = findViewById(R.id.editVisites);

        latLng = getIntent().getParcelableExtra(MapsActivity.EXTRA_LATLNG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    public void onClickAjouter(View view) {

        boolean continuer;

        if (editTextNom.getText().toString().trim().isEmpty()) {
            editTextNom.setError("Vous devez saisir un nom");
            continuer = false;
        } else {
            editTextNom.setError(null); // Enlève l’erreur du champ de saisie
            continuer = true;
        }

        if (continuer) {
            transmettreInfos();
        }
    }

    private void transmettreInfos() {
        int type = 0;
        radioButtonType = findViewById(radioGroup.getCheckedRadioButtonId());

        switch (radioButtonType.getText().toString()) {
            case "Point eau potable":
                type = 1;
                break;
            case "Aire de repos":
                type = 2;
                break;
            case "Point observation":
                type = 3;
                break;
            default:
                type = 0;
                break;
        }

       if(!editVisites.getText().toString().isEmpty()) {
            nombreVisites = Integer.parseInt(editVisites.getText().toString());
        }


        Lieux lieu = new Lieux(editTextNom.getText().toString(), latLng.latitude, latLng.longitude, type,
                editTextTelephone.getText().toString(), DbBitmapUtility.getBytes(bitmapPhoto), 0, nombreVisites);
        Intent intentMessage = new Intent();
        intentMessage.putExtra(MapsActivity.EXTRA_RESULTAT_LIEUX, lieu);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    public void ajoutPhoto(View view) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName(),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }*/
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageLieu.getWidth();
        int targetH = imageLieu.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        bitmapPhoto = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageLieu.setImageBitmap(bitmapPhoto);
        //   DBHelper dbHelper = DBHelper.getInstance(this);
        //  dbHelper.addImageData("image", DbBitmapUtility.getBytes(bitmap) );
    }


}
