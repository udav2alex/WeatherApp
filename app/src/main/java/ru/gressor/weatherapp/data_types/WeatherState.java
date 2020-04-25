package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class WeatherState implements Parcelable {
    public static final String CURRENT_WEATHER = "currentWeather";

    private static TemperatureScale temperatureScale = TemperatureScale.CELSIUS;

    private int temperature;
    private int tempFeelsLike;
    private int clouds;
    private int windSpeed;
    private int windDirection;
    private int pressure = -1;
    private int humidity = -1;
    private int conditions = -1;

    public WeatherState() {
    }

    public WeatherState(int temperature) {
        this.temperature = temperature;
    }

    private WeatherState(Parcel parcel) {
        temperature = parcel.readInt();
        tempFeelsLike = parcel.readInt();
        clouds = parcel.readInt();
        windSpeed = parcel.readInt();
        windDirection = parcel.readInt();
        pressure = parcel.readInt();
        humidity = parcel.readInt();
        conditions = parcel.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(temperature);
        parcel.writeInt(tempFeelsLike);
        parcel.writeInt(clouds);
        parcel.writeInt(windSpeed);
        parcel.writeInt(windDirection);
        parcel.writeInt(pressure);
        parcel.writeInt(humidity);
        parcel.writeInt(conditions);
    }

    public static final Creator<WeatherState> CREATOR = new Creator<WeatherState>() {
        @Override
        public WeatherState createFromParcel(Parcel parcel) {
            return new WeatherState(parcel);
        }

        @Override
        public WeatherState[] newArray(int size) {
            return new WeatherState[size];
        }
    };

    public static WeatherState generateRandom() {
        WeatherState weatherState = new WeatherState();

        weatherState.temperature = (int)(8 + Math.random() * 10);
        weatherState.tempFeelsLike = (int)(8 + Math.random() * 10);
        weatherState.clouds = 0;
        weatherState.windSpeed = (int)(Math.random() * 7);
        weatherState.windDirection = 0;
        weatherState.pressure = (int)(730 + Math.random() * 50);
        weatherState.humidity = (int)(40 + Math.random() * 61);
        weatherState.conditions = 0;

        return weatherState;
    }

    public static TemperatureScale getTemperatureScale() {
        return temperatureScale;
    }

    public static void setTemperatureScale(TemperatureScale temperatureScale) {
        WeatherState.temperatureScale = temperatureScale;
    }

    public int getTemperature() {
        return temperature;
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
        if (!(o instanceof WeatherState)) return false;
        WeatherState that = (WeatherState) o;
        return temperature == that.temperature &&
                tempFeelsLike == that.tempFeelsLike &&
                clouds == that.clouds &&
                windSpeed == that.windSpeed &&
                windDirection == that.windDirection &&
                pressure == that.pressure &&
                humidity == that.humidity &&
                conditions == that.conditions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, tempFeelsLike, clouds, windSpeed, windDirection, pressure, humidity, conditions);
    }
}
