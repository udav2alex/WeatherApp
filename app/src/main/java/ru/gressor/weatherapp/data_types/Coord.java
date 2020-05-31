package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

public class Coord implements Parcelable {
    private float lon;
    private float lat;

    public Coord(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }

    protected Coord(Parcel in) {
        lat = in.readFloat();
        lon = in.readFloat();
    }

    public static final Creator<Coord> CREATOR = new Creator<Coord>() {
        @Override
        public Coord createFromParcel(Parcel in) {
            return new Coord(in);
        }

        @Override
        public Coord[] newArray(int size) {
            return new Coord[size];
        }
    };

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(lat);
        parcel.writeFloat(lon);
    }
}
