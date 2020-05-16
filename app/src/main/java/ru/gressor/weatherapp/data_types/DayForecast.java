package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class DayForecast implements Parcelable {
    private Calendar actualAt;
    private int minTemperature;
    private int maxTemperature;
    private String iconFileName;

    public DayForecast(Calendar actualAt, int minTemperature,
                       int maxTemperature, String iconFileName) {
        this.actualAt = actualAt;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.iconFileName = iconFileName;
    }

    protected DayForecast(Parcel parcel) {
        minTemperature = parcel.readInt();
        maxTemperature = parcel.readInt();
        iconFileName = parcel.readString();
        actualAt = (Calendar) parcel.readSerializable();
    }

    public static final Creator<DayForecast> CREATOR = new Creator<DayForecast>() {
        @Override
        public DayForecast createFromParcel(Parcel parcel) {
            return new DayForecast(parcel);
        }

        @Override
        public DayForecast[] newArray(int size) {
            return new DayForecast[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(minTemperature);
        parcel.writeInt(maxTemperature);
        parcel.writeString(iconFileName);
        parcel.writeSerializable(actualAt);
    }

    public Calendar getActualAt() {
        return actualAt;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public String getIconFileName() {
        return iconFileName;
    }
}
