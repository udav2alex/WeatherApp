package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Objects;

public class WeatherState implements Parcelable {
    public static final String WEATHER_STATE = "weatherState";

    ActualWeather actualWeather;
    List<HourForecast> hourlyForecast;
    List<DayForecast> dailyForecast;

    public WeatherState(ActualWeather actualWeather,
                        List<HourForecast> hourlyForecast, List<DayForecast> dailyForecast) {
        this.actualWeather = actualWeather;
        this.hourlyForecast = hourlyForecast;
        this.dailyForecast = dailyForecast;
    }

    protected WeatherState(Parcel parcel) {
        actualWeather = parcel.readParcelable(ActualWeather.class.getClassLoader());
        hourlyForecast = parcel.createTypedArrayList(HourForecast.CREATOR);
        dailyForecast = parcel.createTypedArrayList(DayForecast.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(actualWeather, i);
        parcel.writeTypedList(hourlyForecast);
        parcel.writeTypedList(dailyForecast);
    }

    public ActualWeather getActualWeather() {
        return actualWeather;
    }

    public void setActualWeather(ActualWeather actualWeather) {
        this.actualWeather = actualWeather;
    }

    public List<HourForecast> getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(List<HourForecast> hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }

    public List<DayForecast> getDailyForecast() {
        return dailyForecast;
    }

    public void setDailyForecast(List<DayForecast> dailyForecast) {
        this.dailyForecast = dailyForecast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherState that = (WeatherState) o;
        return Objects.equals(actualWeather, that.actualWeather) &&
                Objects.equals(hourlyForecast, that.hourlyForecast) &&
                Objects.equals(dailyForecast, that.dailyForecast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actualWeather, hourlyForecast, dailyForecast);
    }
}
