package ru.gressor.weatherapp.ui;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;

import ru.gressor.weatherapp.db.WeatherDao;
import ru.gressor.weatherapp.db.WeatherDatabase;

public class App extends Application {
    private static App instance;
    private WeatherDatabase db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        db = Room.databaseBuilder(getApplicationContext(),
                WeatherDatabase.class, "weatherDB")
                .allowMainThreadQueries()
                .build();
    }

    public WeatherDao getWeatherDao() {
        return db.getWeatherDao();
    }
}
