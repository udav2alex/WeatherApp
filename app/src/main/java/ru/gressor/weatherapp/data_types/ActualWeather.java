package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Objects;

public class ActualWeather implements Parcelable {

    private Calendar actualAt;
    private int temperature;
    private String iconFileName;
    private String conditionsDescription;
    private int tempFeelsLike;
    private int clouds = -1;
    private int windSpeed = -1;
    private int windDirection = -1;
    private int pressure = -1;
    private int humidity = -1;

    public ActualWeather(Calendar actualAt, int temperature, String iconFileName,
                         String conditionsDescription, int tempFeelsLike, int clouds,
                         int windSpeed, int windDirection, int pressure, int humidity) {
        this.actualAt = actualAt;
        this.temperature = temperature;
        this.iconFileName = iconFileName;
        this.conditionsDescription = conditionsDescription;
        this.tempFeelsLike = tempFeelsLike;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public ActualWeather(int temperature, int tempFeelsLike, Calendar actualAt) {
        this.temperature = temperature;
        this.tempFeelsLike = tempFeelsLike;
        this.actualAt = actualAt;
    }

    private ActualWeather(Parcel parcel) {
        temperature = parcel.readInt();
        iconFileName = parcel.readString();
        conditionsDescription = parcel.readString();
        tempFeelsLike = parcel.readInt();
        clouds = parcel.readInt();
        windSpeed = parcel.readInt();
        windDirection = parcel.readInt();
        pressure = parcel.readInt();
        humidity = parcel.readInt();
        actualAt = (Calendar) parcel.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(temperature);
        parcel.writeString(iconFileName);
        parcel.writeString(conditionsDescription);
        parcel.writeInt(tempFeelsLike);
        parcel.writeInt(clouds);
        parcel.writeInt(windSpeed);
        parcel.writeInt(windDirection);
        parcel.writeInt(pressure);
        parcel.writeInt(humidity);
        parcel.writeSerializable(actualAt);
    }

    public static final Creator<ActualWeather> CREATOR = new Creator<ActualWeather>() {
        @Override
        public ActualWeather createFromParcel(Parcel parcel) {
            return new ActualWeather(parcel);
        }

        @Override
        public ActualWeather[] newArray(int size) {
            return new ActualWeather[size];
        }
    };

    public Calendar getActualAt() {
        return actualAt;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getIconFileName() {
        return iconFileName;
    }

    public String getConditionsDescription() {
        return conditionsDescription;
    }

    public int getTempFeelsLike() {
        return tempFeelsLike;
    }

    public int getClouds() {
        return clouds;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActualWeather)) return false;
        ActualWeather that = (ActualWeather) o;
        return temperature == that.temperature &&
                tempFeelsLike == that.tempFeelsLike &&
                clouds == that.clouds &&
                windSpeed == that.windSpeed &&
                windDirection == that.windDirection &&
                pressure == that.pressure &&
                humidity == that.humidity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, tempFeelsLike, clouds, windSpeed, windDirection, pressure, humidity);
    }
}
