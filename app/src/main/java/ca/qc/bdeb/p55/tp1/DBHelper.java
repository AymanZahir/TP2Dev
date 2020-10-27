package ca.qc.bdeb.p55.tp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "app.db";
    public static final int DBVERSION = 1;

    private static DBHelper instance = null;
    private Context context;

    /***** Table Lieux *****/
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

    /***** Table USER *****/
    private static final String TABLE_USER = "user";

    private static final String USER_ID = "_id";
    private static final String USER_NOM = "nom";
    private static final String USER_DISTANCE_MAX = "distance_max";
    private static final String USER_DISTANCE_MIN = "distance_min";
    private static final String USER_DISTANCE_PARCOURUE = "distance_parcourue";


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
                + LIEUX_LONGITUDE + " REAL NOT NULL,"
                + LIEUX_TYPE + " INTEGER NOT NULL,"
                + LIEUX_TELEPHONE + " TEXT,"
                + LIEUX_PHOTO + " BLOB NOT NULL,"
                + LIEUX_FAVORI + " INTEGER NOT NULL,"
                + LIEUX_NOMBRE_VISITE + " INTEGER NOT NULL)";
        sqLiteDatabase.execSQL(sqlLieux);

        String sqlUser = "CREATE TABLE " + TABLE_USER + "("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_NOM + " TEXT NOT NULL,"
                + USER_DISTANCE_MAX + " INTEGER NOT NULL,"
                + USER_DISTANCE_MIN + " INTEGER NOT NULL,"
                + USER_DISTANCE_PARCOURUE + " INTEGER NOT NULL)";
        sqLiteDatabase.execSQL(sqlUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LIEUX);

        onCreate(sqLiteDatabase);
    }

    public void ajouterLieux(Lieux Lieux) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (Lieux.getId() != -1) {
            values.put(LIEUX_ID, Lieux.getId());
        }
        values.put(LIEUX_NOM, Lieux.getNom());
        values.put(LIEUX_LATTITUDE, Lieux.getLatitude());
        values.put(LIEUX_LONGITUDE, Lieux.getLongitude());
        values.put(LIEUX_TYPE, Lieux.getType());
        if (Lieux.getTelephone() != null) {
            values.put(LIEUX_TELEPHONE, Lieux.getTelephone());
        }
        values.put(LIEUX_PHOTO, Lieux.getImageResId());
        values.put(LIEUX_FAVORI, Lieux.getFavori());
        values.put(LIEUX_NOMBRE_VISITE, Lieux.getNombreVisites());


        long id = db.insert(TABLE_LIEUX, null, values);
        if (id != -1) {
            Lieux.setId(id);
        }
        db.close();
    }

    public int supprimerLieux(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int nbDelete = db.delete(TABLE_LIEUX, LIEUX_ID + " = ?", new String[]{"" + id});

        db.close();
        return nbDelete;
    }

    //À REVOIR
    public void modifierLieux(Lieux Lieux) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LIEUX_NOM, Lieux.getNom());
        values.put(LIEUX_LATTITUDE, Lieux.getLatitude());
        values.put(LIEUX_LONGITUDE, Lieux.getLongitude());
        if (Lieux.getTelephone() != null) {
            values.put(LIEUX_TELEPHONE, Lieux.getTelephone());
        }
        values.put(LIEUX_PHOTO, "" + Lieux.getImageResId());
        values.put(LIEUX_FAVORI, Lieux.getFavori());
        values.put(LIEUX_NOMBRE_VISITE, Lieux.getNombreVisites());

        db.update(TABLE_LIEUX, values, LIEUX_ID + " = ?", new String[]{"" + Lieux.getId()});

        db.close();
    }

    public Lieux getLieux(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LIEUX, new String[]{LIEUX_ID, LIEUX_NOM, LIEUX_LATTITUDE, LIEUX_LONGITUDE, LIEUX_TELEPHONE, LIEUX_PHOTO, LIEUX_FAVORI, LIEUX_NOMBRE_VISITE}, LIEUX_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        Lieux lieux = new Lieux(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getInt(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7), cursor.getInt(8));
        cursor.close();
        db.close();

        return lieux;
    }

    public ArrayList<Lieux> getToutLesLieux() {

        ArrayList<Lieux> listePersonne = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LIEUX, null, null, null, null, null, null, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                listePersonne.add(new Lieux(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getInt(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7), cursor.getInt(8)));
            }
            cursor.close();
        }

        db.close();

        return listePersonne;
    }

    public void ajouterUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        if (user.getId() != -1) {
            values.put(USER_ID, user.getId());
        }
        values.put(USER_NOM, user.getNom());
        values.put(USER_DISTANCE_MAX, user.getDistance_max());
        values.put(USER_DISTANCE_MIN, user.getDistance_min());
        values.put(USER_DISTANCE_PARCOURUE, user.getDistance_parcourue());

        long id = db.insert(TABLE_USER, null, values);
        if (id != -1) {
            user.setId(id);
        }
        db.close();
    }
}
