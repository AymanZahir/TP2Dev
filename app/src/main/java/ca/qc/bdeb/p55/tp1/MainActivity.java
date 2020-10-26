package ca.qc.bdeb.p55.tp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LieuxCourseListener {

    ConstraintLayout constraintLayout;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    private Button buttonAddToFavoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        RecyclerView lieuxCourseRecyclerView = findViewById(R.id.coursesRecyclerView);
        buttonAddToFavoris = findViewById(R.id.btnAddFavoris);

        List<LieuxCourse> lieuxCourse = new ArrayList<>();


        //Example ajouté pour test
        LieuxCourse parc = new LieuxCourse();
        parc.image = R.drawable.ic_baseline_home_24;
        parc.name = "Outremont parc";
        parc.rating = 4.5f;
        parc.createdBy = "Ville de montréal";
        parc.isSelected = false;
        parc.story = "Très bon parc pour courrir le matin";
        lieuxCourse.add(parc);

        final LieuxCourseAdapter lieuxCourseAdapter = new LieuxCourseAdapter(lieuxCourse, this);
        lieuxCourseRecyclerView.setAdapter(lieuxCourseAdapter);


        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer);

//        DBHelper dbHelper = DBHelper.getInstance(this);


        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Liste des lieux sélect
        List<LieuxCourse> selectedLieuxCourse= lieuxCourseAdapter.getSelectedLieuxCourse();

        StringBuilder nomsLieuxCourse = new StringBuilder();

        for (int i=0; i < selectedLieuxCourse.size(); i++){
            if (i ==0){
                nomsLieuxCourse.append(selectedLieuxCourse.get(i).name);
            }
            else {
                nomsLieuxCourse.append("\n").append(selectedLieuxCourse.get(i).name);
            }
        }
        Toast.makeText(MainActivity.this, nomsLieuxCourse.toString(), Toast.LENGTH_SHORT).show();

    }

    public void openNewActivity() {
        //Intent intent = new Intent(this, MapsActivity.class);
        // startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return true;

    }


    @Override
    public void onLieuCourseAction(Boolean isSelected) {

    }
}