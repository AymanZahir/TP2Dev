package ca.qc.bdeb.p55.tp1;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private long id;
    private String nom;
    private int distance_max;
    private int distance_min;
    private int distance_parcourue;

    public User(long id, String nom, int distance_max, int distance_min, int distance_parcourue) {
        this.id = id;
        this.nom = nom;
        this.distance_max = distance_max;
        this.distance_min = distance_min;
        this.distance_parcourue = distance_parcourue;
    }

    public User(String nom, int distance_max, int distance_min, int distance_parcourue) {
        this.nom = nom;
        this.distance_max = distance_max;
        this.distance_min = distance_min;
        this.distance_parcourue = distance_parcourue;
    }

    protected User(Parcel in) {
        id = in.readLong();
        nom = in.readString();
        distance_max = in.readInt();
        distance_min = in.readInt();
        distance_parcourue = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getDistance_max() {
        return distance_max;
    }

    public void setDistance_max(int distance_max) {
        this.distance_max = distance_max;
    }

    public int getDistance_min() {
        return distance_min;
    }

    public void setDistance_min(int distance_min) {
        this.distance_min = distance_min;
    }

    public int getDistance_parcourue() {
        return distance_parcourue;
    }

    public void setDistance_parcourue(int distance_parcourue) {
        this.distance_parcourue = distance_parcourue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(nom);
        parcel.writeInt(distance_max);
        parcel.writeInt(distance_min);
        parcel.writeInt(distance_parcourue);
    }
}
