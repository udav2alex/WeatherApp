package ru.gressor.weatherapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCity(City city);

    @Update
    void updateCity(City city);

    @Delete
    void deleteCity(City city);

    @Query("DELETE FROM cities WHERE id = :id")
    void deleteCity(long id);

    @Query("DELETE FROM cities WHERE city_name = :name")
    void deleteCity(String name);

    @Query("DELETE FROM cities WHERE city_name IS NULL")
    void deleteEmpty();

    @Query("SELECT * FROM cities")
    List<City> getAllCities();

    @Query("SELECT COUNT(*) FROM cities")
    int countAllCities();

    @Query("SELECT * FROM cities WHERE id = :id")
    City getCity(long id);

    @Query("SELECT * FROM cities WHERE city_name = :name")
    City getCity(String name);
}
