package ru.gressor.weatherapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {City.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao getWeatherDao();
}
