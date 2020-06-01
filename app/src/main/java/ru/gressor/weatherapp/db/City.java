package ru.gressor.weatherapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;

import ru.gressor.weatherapp.data_types.Coord;

@Entity (
        indices = {@Index("id"), @Index("city_name")},
        tableName = "cities")
public class City {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "city_name")
    public String cityName;

    public float lon;

    public float lat;

    public boolean favorite;

    public long actual;

    public int temperature;

    public City(long id, String cityName, float lon, float lat, boolean favorite, long actual, int temperature) {
        this.id = id;
        this.cityName = cityName;
        this.lon = lon;
        this.lat = lat;
        this.favorite = favorite;
        this.actual = actual;
        this.temperature = temperature;
    }

    public City(String cityName, Coord coord, boolean favorite, Calendar actualAt, int temperature) {
        this.cityName = cityName;

        if (coord == null) {
            this.lon = -1000;
            this.lat = -1000;
        } else {
            this.lon = coord.getLon();
            this.lat = coord.getLat();
        }

        if (actualAt == null) {
            this.actual = -1;
        } else {
            this.actual = actualAt.getTimeInMillis();
        }

        this.favorite = favorite;
        this.temperature = temperature;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCoord(Coord coord) {
        if (coord == null) {
            lon = -1000;
            lat = -1000;
        } else {
            lon = coord.getLon();
            lat = coord.getLat();
        }
    }

    public Coord getCoord() {
        if (lon < -360) {
            return null;
        } else {
            return new Coord(lon, lat);
        }
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Calendar getActualAt() {
        if (actual < 0) return null;

        Calendar actualAt = Calendar.getInstance();
        actualAt.setTimeInMillis(actual);
        return actualAt;
    }

    public void setActualAt(Calendar actualAt) {
        if (actualAt == null) {
            this.actual = -1;
        } else {
            this.actual = actualAt.getTimeInMillis();
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
