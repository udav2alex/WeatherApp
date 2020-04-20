package ru.gressor.weatherapp;

import android.os.Parcel;
import android.os.Parcelable;

public class DestinationPoint implements Parcelable {
    private String town;
    private String site;

    public DestinationPoint(String town, String site) {
        this.town = town;
        this.site = site;
    }

    public DestinationPoint(Parcel parcel) {
        this.town = parcel.readString();
        this.site = parcel.readString();
    }

    public String getTown() {
        return town;
    }

    public String getSite() {
        return site;
    }

    public static final Parcelable.Creator<DestinationPoint> CREATOR
            = new Parcelable.Creator<DestinationPoint>() {
        public DestinationPoint createFromParcel(Parcel parcel) {
            return new DestinationPoint(parcel);
        }

        public DestinationPoint[] newArray(int size) {
            return new DestinationPoint[size];
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
}