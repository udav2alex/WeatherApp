package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryItem implements Parcelable {
    private WeatherState weatherState;
    private PositionPoint positionPoint;
    private boolean isFavorite;

    public HistoryItem(WeatherState weatherState, PositionPoint positionPoint) {
        this.weatherState = weatherState;
        this.positionPoint = positionPoint;
        this.isFavorite = false;
    }

    protected HistoryItem(Parcel parcel) {
        positionPoint = parcel.readParcelable(PositionPoint.class.getClassLoader());
        isFavorite = parcel.readByte() != 0;
    }

    public static final Creator<HistoryItem> CREATOR = new Creator<HistoryItem>() {
        @Override
        public HistoryItem createFromParcel(Parcel parcel) {
            return new HistoryItem(parcel);
        }

        @Override
        public HistoryItem[] newArray(int size) {
            return new HistoryItem[size];
        }
    };

    public WeatherState getWeatherState() {
        return weatherState;
    }

    public PositionPoint getPositionPoint() {
        return positionPoint;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(positionPoint, flags);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
    }
}