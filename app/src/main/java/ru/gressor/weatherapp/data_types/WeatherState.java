package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Objects;

public class WeatherState implements Parcelable {
    public static final String CURRENT_WEATHER = "currentWeather";

    private static TemperatureScale temperatureScale = TemperatureScale.CELSIUS;

    private Calendar actualAt;
    private int temperature = -1000;
    private int tempFeelsLike = -1000;
    private int clouds = -1;
    private int windSpeed = -1;
    private int windDirection = -1;
    private int pressure = -1;
    private int humidity = -1;
    private int conditions = -1;

    private WeatherState() {
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
        tempFeelsLike = parcel.readInt();
        clouds = parcel.readInt();
        windSpeed = parcel.readInt();
        windDirection = parcel.readInt();
        pressure = parcel.readInt();
        humidity = parcel.readInt();
        conditions = parcel.readInt();

        boolean actualAtIsPresent = parcel.readInt() == 1;
        long millis = parcel.readLong();
        if (actualAtIsPresent) {
            actualAt = Calendar.getInstance();
            actualAt.setTimeInMillis(millis);
        }
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

        if (actualAt == null) {
            parcel.writeInt(0);
            parcel.writeLong(0);
        } else {
            parcel.writeInt(1);
            parcel.writeLong(actualAt.getTimeInMillis());
        }
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

    public Calendar getActualAt() {
        return actualAt;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getTemperatureScaled(String errorMessage) {
        return temperatureScale.fromCelsius(temperature, errorMessage);
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
