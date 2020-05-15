package ru.gressor.weatherapp.data_types.local_dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Objects;

public class CurrentWeather implements Parcelable {
    public static final String CURRENT_WEATHER = "currentWeather";

    private static TemperatureScale temperatureScale = TemperatureScale.CELSIUS;

    private Calendar actualAt;
    private int temperature = -1000;
    private String iconFileName;
    private String conditionsDescription;
    private int tempFeelsLike = -1000;
    private int clouds = -1;
    private int windSpeed = -1;
    private int windDirection = -1;
    private int pressure = -1;
    private int humidity = -1;

    private CurrentWeather() {
    }

    public static CurrentWeather create(ru.gressor.weatherapp.data_types.openweather_current_weather.CurrentWeather currentWeather) {
        CurrentWeather currentWeatherState = new CurrentWeather(
                Math.round(currentWeather.getMain().getTemp()),
                Math.round(currentWeather.getMain().getFeels_like()));
        currentWeatherState.humidity = Math.round(currentWeather.getMain().getHumidity());
        currentWeatherState.pressure = Math.round(0.750062f * currentWeather.getMain().getPressure());
        currentWeatherState.windSpeed = Math.round(currentWeather.getWind().getSpeed());
        currentWeatherState.conditionsDescription = currentWeather.getWeather()[0].getDescription();
        currentWeatherState.iconFileName = currentWeather.getWeather()[0].getIcon();
        return currentWeatherState;
    }

    public CurrentWeather(int temperature, int tempFeelsLike) {
        this.temperature = temperature;
        this.tempFeelsLike = tempFeelsLike;
    }

    public CurrentWeather(int temperature, int tempFeelsLike, Calendar actualAt) {
        this.temperature = temperature;
        this.tempFeelsLike = tempFeelsLike;
        this.actualAt = actualAt;
    }

    private CurrentWeather(Parcel parcel) {
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

    public static final Creator<CurrentWeather> CREATOR = new Creator<CurrentWeather>() {
        @Override
        public CurrentWeather createFromParcel(Parcel parcel) {
            return new CurrentWeather(parcel);
        }

        @Override
        public CurrentWeather[] newArray(int size) {
            return new CurrentWeather[size];
        }
    };

    public static CurrentWeather generateRandom() {
        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.temperature = (int)(8 + Math.random() * 10);
        currentWeather.conditionsDescription = "";
        currentWeather.tempFeelsLike = (int)(8 + Math.random() * 10);
        currentWeather.clouds = 0;
        currentWeather.windSpeed = (int)(Math.random() * 7);
        currentWeather.windDirection = 0;
        currentWeather.pressure = (int)(730 + Math.random() * 50);
        currentWeather.humidity = (int)(40 + Math.random() * 61);

        return currentWeather;
    }

    public static TemperatureScale getTemperatureScale() {
        return temperatureScale;
    }

    public static void setTemperatureScale(TemperatureScale temperatureScale) {
        CurrentWeather.temperatureScale = temperatureScale;
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

    public String getIconFileName() {
        return iconFileName;
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
        if (!(o instanceof CurrentWeather)) return false;
        CurrentWeather that = (CurrentWeather) o;
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
