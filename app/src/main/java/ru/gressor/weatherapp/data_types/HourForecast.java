package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class HourForecast implements Parcelable {
    private Calendar actualAt;
    private int temperature = -1000;
    private String iconFileName;

    public HourForecast(Calendar actualAt, int temperature, String iconFileName) {
        this.actualAt = actualAt;
        this.temperature = temperature;
        this.iconFileName = iconFileName;
    }

    protected HourForecast(Parcel parcel) {
        temperature = parcel.readInt();
        iconFileName = parcel.readString();
        actualAt = (Calendar) parcel.readSerializable();
    }

    public static final Creator<HourForecast> CREATOR = new Creator<HourForecast>() {
        @Override
        public HourForecast createFromParcel(Parcel parcel) {
            return new HourForecast(parcel);
        }

        @Override
        public HourForecast[] newArray(int size) {
            return new HourForecast[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(temperature);
        parcel.writeString(iconFileName);
        parcel.writeSerializable(actualAt);
    }

    public Calendar getActualAt() {
        return actualAt;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getIconFileName() {
        return iconFileName;
    }
}
