package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class PositionPoint implements Parcelable {
    public static final String CURRENT_POSITION = "currentPosition";
    private String town;
    private String site;

    public PositionPoint(String town, String site) {
        this.town = town;
        this.site = site;
    }

    public PositionPoint(Parcel parcel) {
        this.town = parcel.readString();
        this.site = parcel.readString();
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionPoint)) return false;
        PositionPoint that = (PositionPoint) o;
        return getTown().equals(that.getTown()) &&
                getSite().equals(that.getSite());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTown(), getSite());
    }
}