package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class PositionPoint implements Parcelable {
    public static final String CURRENT_POSITION = "currentPosition";
    private String town;
    private String site;
    private String serviceTown;
    private String serviceSite;
    private float lat = -1f;
    private float lon = -1f;

    public PositionPoint(String town, String site) {
        this.town = town;
        this.site = site;
    }

    public PositionPoint(Parcel parcel) {
        this.town = parcel.readString();
        this.site = parcel.readString();
        this.serviceTown = parcel.readString();
        this.serviceSite = parcel.readString();
        this.lat = parcel.readFloat();
        this.lon = parcel.readFloat();
    }

    public String getTown() {
        return town;
    }

    public String getSite() {
        return site;
    }

    public static final Parcelable.Creator<PositionPoint> CREATOR
            = new Parcelable.Creator<PositionPoint>() {
        public PositionPoint createFromParcel(Parcel parcel) {
            return new PositionPoint(parcel);
        }

        public PositionPoint[] newArray(int size) {
            return new PositionPoint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(town);
        parcel.writeString(site);
        parcel.writeString(serviceTown);
        parcel.writeString(serviceSite);
        parcel.writeFloat(lat);
        parcel.writeFloat(lon);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionPoint that = (PositionPoint) o;
        return Float.compare(that.lat, lat) == 0 &&
                Float.compare(that.lon, lon) == 0 &&
                Objects.equals(town, that.town) &&
                Objects.equals(site, that.site) &&
                Objects.equals(serviceTown, that.serviceTown) &&
                Objects.equals(serviceSite, that.serviceSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(town, site, serviceTown, serviceSite, lat, lon);
    }
}