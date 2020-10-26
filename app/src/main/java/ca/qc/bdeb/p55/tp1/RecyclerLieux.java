package ca.qc.bdeb.p55.tp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class RecyclerLieux extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Lieux> itemList = new ArrayList<>();
    private LieuxAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_lieux);

        DBHelper dbHelper = DBHelper.getInstance(this);

        itemList = dbHelper.getToutLesLieux();
        if (itemList.size() == 0) {
            itemList.add(new Lieux("College Bois De Boulogne", 45.5380, -73.6760, 2, null, 1, 0, 4));
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
    }


}