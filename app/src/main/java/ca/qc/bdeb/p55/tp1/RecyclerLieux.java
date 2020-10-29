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
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class RecyclerLieux extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private ArrayList<Lieux> itemList = new ArrayList<>();
    private LieuxAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int croissant = -1;
    NavigationView navigationView;

    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String langage  = "en";
        Locale locale = new Locale(langage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_recycler_lieux);

        toolbar = findViewById(R.id.toolbar_main);
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

    public void onClickRapports(MenuItem item) {

    }

    public void onClickMap(MenuItem item) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public void notifyItemSelected(int position) {
    }


    //Ajoute le contact au favori et change l'étoile de couleur
    public void ajouterFavori(int position, ImageView imageViewFavori) {
        DBHelper dbHelper = DBHelper.getInstance(this);
        if (itemList.get(position).getFavori() == 1) {
            itemList.get(position).setFavori(0);
            imageViewFavori.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            itemList.get(position).setFavori(1);
            imageViewFavori.setImageResource(R.drawable.ic_baseline_star_border_24);
        }
        dbHelper.modifierLieux(itemList.get(position));
        Toast.makeText(getApplicationContext(),"Ce lieu a été ajouté au favoris",Toast.LENGTH_LONG).show();
        adapter.notifyItemChanged(position);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trierItemCroissant:
                trierOrdreCroissant();
                return true;
            case R.id.trierItemDecroissant:
                trierOrdreDecroissant();
                return true;
            case R.id.trierItemFavoris:
                trierFavoris();
                return true;
            case R.id.changerLangueItem:
                //TODO Changer vers la langue et update interface
                return true;
            case R.id.trierListe:
                if(croissant== -1 || croissant == 0) {
                    trierOrdreCroissant();
                    croissant = 1;
                }
                else if (croissant ==1 ) {
                    trierOrdreDecroissant();
                    croissant = 0;
                }
                return true;

            default :
                return super.onOptionsItemSelected(item);
        }

    }
    public void trierOrdreCroissant (){
        Collections.sort(itemList, new Comparator<Lieux>() {
            @Override
            public int compare(Lieux u1, Lieux u2) {
                return u1.getNom().toLowerCase().compareTo(u2.getNom().toLowerCase());
            }
        });
        adapter.notifyDataSetChanged();
    }
    public void trierOrdreDecroissant(){
        trierOrdreCroissant();
        Collections.reverse(itemList);

        adapter.notifyDataSetChanged();
    }

    public void trierFavoris(){
        Collections.sort(itemList, new Comparator<Lieux>() {
            @Override
            public int compare(Lieux u1, Lieux u2) {
                if (u1.getFavori() < u2.getFavori())
                    return 1;
                if (u1.getFavori() > u2.getFavori())
                    return -1;
                return 0;
            }

        });

        while(nonFavoriResteEncore()) {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).getFavori() == 0) {
                    itemList.remove(i);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private boolean nonFavoriResteEncore() {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getFavori() == 0) {
               return true;
            }
        }
        return false;
    }


}