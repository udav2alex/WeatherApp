package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Objects;

import ru.gressor.weatherapp.data_types.weather_today.WeatherToday;

public class WeatherState implements Parcelable {
    public static final String CURRENT_WEATHER = "currentWeather";

    private static TemperatureScale temperatureScale = TemperatureScale.CELSIUS;

    private Calendar actualAt;
    private int temperature = -1000;
    private String conditionsDescription;
    private int tempFeelsLike = -1000;
    private int clouds = -1;
    private int windSpeed = -1;
    private int windDirection = -1;
    private int pressure = -1;
    private int humidity = -1;
    private int conditions = -1;

    private WeatherState() {
    }

    public static WeatherState create(WeatherToday weatherToday) {
        WeatherState weatherState = new WeatherState(
                Math.round(weatherToday.getMain().getTemp()),
                Math.round(weatherToday.getMain().getFeels_like()));
        weatherState.humidity = Math.round(weatherToday.getMain().getHumidity());
        weatherState.pressure = Math.round(0.750062f * weatherToday.getMain().getPressure());
        weatherState.windSpeed = Math.round(weatherToday.getWind().getSpeed());
        weatherState.conditionsDescription = weatherToday.getWeather()[0].getDescription();
        return weatherState;
    }

    public WeatherState(int temperature, int tempFeelsLike) {
        this.temperature = temperature;
        this.tempFeelsLike = tempFeelsLike;
    }

    public WeatherState(int temperature, int tempFeelsLike, Calendar actualAt) {
        this.temperature = temperature;
        this.tempFeelsLike = tempFeelsLike;
        this.actualAt = actualAt;
    }

    private WeatherState(Parcel parcel) {
        temperature = parcel.readInt();
        conditionsDescription = parcel.readString();
        tempFeelsLike = parcel.readInt();
        clouds = parcel.readInt();
        windSpeed = parcel.readInt();
        windDirection = parcel.readInt();
        pressure = parcel.readInt();
        humidity = parcel.readInt();
        conditions = parcel.readInt();
        actualAt = (Calendar) parcel.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(temperature);
        parcel.writeString(conditionsDescription);
        parcel.writeInt(tempFeelsLike);
        parcel.writeInt(clouds);
        parcel.writeInt(windSpeed);
        parcel.writeInt(windDirection);
        parcel.writeInt(pressure);
        parcel.writeInt(humidity);
        parcel.writeInt(conditions);
        parcel.writeSerializable(actualAt);
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
        weatherState.conditionsDescription = "";
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

    public Calendar getActualAt() {
        return actualAt;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getTemperatureScaled(String errorMessage) {
        return temperatureScale.fromCelsius(temperature, errorMessage);
    }

    public String getConditionsDescription() {
        return conditionsDescription;
    }

    public int getTempFeelsLike() {
        return tempFeelsLike;
    }

    public String getTempFeelsLikeScaled(String errorMessage) {
        return temperatureScale.fromCelsius(temperature, errorMessage);
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
