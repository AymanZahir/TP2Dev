package ca.qc.bdeb.p55.tp1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;


public class EntreesUtilisateurs extends AppCompatActivity {

    private EditText editTextNom;
    private EditText editTextTelephone;
    private RadioGroup radioGroup;
    private int imageLieuID;

    private LatLng latLng;

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


        latLng = getIntent().getParcelableExtra(MapsActivity.EXTRA_LatLng);

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


        if (editTextTelephone.getText().toString().trim().isEmpty()) {
            editTextTelephone.setError("Vous devez saisir un Prenom");
            continuer = false;
        } else {
            editTextTelephone.setError(null); // Enlève l’erreur du champ de saisie
            continuer = true;
        }

        if (continuer) {
            transmettreInfos();
        }
    }

    private void transmettreInfos() {
      //  Lieux lieu = new Lieux(editTextNom.getText().toString(), latLng.latitude, latLng.longitude, radioGroup.getCheckedRadioButtonId(), editTextTelephone.getText().toString(), 0, 0, 2);
        Lieux lieu = new Lieux("test", 46.5380, -73.6760, 2, null, 1, 0, 4);
        Intent intentMessage = new Intent();
        intentMessage.putExtra(MapsActivity.EXTRA_RESULTAT_LIEUX, lieu);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

}
