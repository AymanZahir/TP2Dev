package ca.qc.bdeb.p55.tp1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lieux.db";    // Votre nom de fichier de BD
    public static final int DBVERSION = 1;    // Votre numéro actuel de version de BD

    private static DBHelper instance = null; //L’unique instance de DbHelper possible
    private Context context;

    private static final String TABLE_LIEUX = "lieux";

    private static final String LIEUX_ID = "_id";
    private static final String LIEUX_NOM = "nom";
    private static final String LIEUX_LATTITUDE = "lattitude";
    private static final String LIEUX_LONGITUDE = "longitude";
    private static final String LIEUX_TYPE = "type";
    private static final String LIEUX_TELEPHONE = "telephone";
    private static final String LIEUX_PHOTO = "photo";
    private static final String LIEUX_FAVORI = "favori";
    private static final String LIEUX_NOMBRE_VISITE = "nombreVisite";


    private DBHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DBVERSION);
        this.context = context;
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) instance = new DBHelper(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlLieux = "CREATE TABLE " + TABLE_LIEUX + "("
                + LIEUX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LIEUX_NOM + " TEXT NOT NULL,"
                + LIEUX_LATTITUDE + " REAL NOT NULL,"
                + LIEUX_LONGITUDE + ")";
        sqLiteDatabase.execSQL(sqlLieux);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
