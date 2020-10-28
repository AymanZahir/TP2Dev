package ca.qc.bdeb.p55.tp1;

import android.os.Parcel;
import android.os.Parcelable;

public class Lieux implements Parcelable {
    private long id;
    private String nom;
    private double latitude;
    private double longitude;
    private int type;
    private String telephone;
    private byte[] imageResId;
    private int favori;
    private int nombreVisites;

    public Lieux(long id, String nom, double latitude, double longitude, int type, String telephone, byte[] imageResId, int favori, int nombreVisites) {
        this.id = id;
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.telephone = telephone;
        this.imageResId = imageResId;
        this.favori = favori;
        this.nombreVisites = nombreVisites;
    }

    public Lieux(String nom, double latitude, double longitude, int type, String telephone, byte[] imageResId, int favori, int nombreVisites) {
        this.id = -1;
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.telephone = telephone;
        this.imageResId = imageResId;
        this.favori = favori;
        this.nombreVisites = nombreVisites;
    }

    protected Lieux(Parcel in) {
        id = in.readLong();
        nom = in.readString();
        latitude = in.readDouble();
        longitude =  in.readDouble();;
        type = in.readInt();
        telephone = in.readString();
       // imageResId = in.readByte();
        favori = in.readInt();
        nombreVisites = in.readInt();
    }

    public static final Creator<Lieux> CREATOR = new Creator<Lieux>() {
        @Override
        public Lieux createFromParcel(Parcel in) {
            return new Lieux(in);
        }

        @Override
        public Lieux[] newArray(int size) {
            return new Lieux[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(nom);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(type);
        parcel.writeString(telephone);
        parcel.writeByteArray(imageResId);
        parcel.writeInt(favori);
        parcel.writeInt(nombreVisites);
    }

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public byte[] getImageResId() {
        return imageResId;
    }

    public void setImageResId(byte[] imageResId) {
        this.imageResId = imageResId;
    }

    public int getFavori() {
        return favori;
    }

    public void setFavori(int favori) {
        this.favori = favori;
    }

    public int getNombreVisites() {
        return nombreVisites;
    }

    public void setNombreVisites(int nombreVisites) {
        this.nombreVisites = nombreVisites;
    }

}
