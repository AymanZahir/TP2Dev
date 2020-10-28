package ca.qc.bdeb.p55.tp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class RecyclerLieux extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private ArrayList<Lieux> itemList = new ArrayList<>();
    private LieuxAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_lieux);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        DBHelper dbHelper = DBHelper.getInstance(this);

        itemList = dbHelper.getToutLesLieux();
        if (itemList.size() == 0) {
            for (Lieux l : itemList) {
                dbHelper.ajouterLieux(l);
            }
        }


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new LieuxAdapter(itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        configAdapter();
    }

    private void configAdapter() {

        adapter.setOnItemClickListener(new LieuxAdapter.OnItemClickListener() {

            @Override
            public void OnItemClick(int position) {
                notifyItemSelected(position);
            }

            @Override
            public void onFavoriClick(int position, ImageView imageViewFavori) {
                ajouterFavori(position, imageViewFavori);
            }

        /*    @Override
            public void onDeleteClick(int position) {
                DeleteItem(position);
            }*/
        });
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

    public void onClickRapports(MenuItem item) {
        Intent intent = new Intent(this, RecyclerLieux.class);
        startActivity(intent);
    }

    public void onClickMap(MenuItem item) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    public void notifyItemSelected(int position) {

        //ajouterContact(contactList.get(position));

        // Snackbar snackbar = Snackbar.make(recyclerView, contactList.get(position).getNom() + " a été sélectionné", Snackbar.LENGTH_SHORT);
        //snackbar.show();
    }


    //Ajoute le contact au favori et change l'étoile de couleur
    public void ajouterFavori(int position, ImageView imageViewFavori) {

        if (itemList.get(position).getFavori() == 1) {
            itemList.get(position).setFavori(0);
            imageViewFavori.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            itemList.get(position).setFavori(1);
            imageViewFavori.setImageResource(R.drawable.ic_baseline_star_border_24);
        }
        adapter.notifyItemChanged(position);
    }
}