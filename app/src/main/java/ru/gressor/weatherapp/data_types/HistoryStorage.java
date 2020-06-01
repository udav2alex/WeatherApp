package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ru.gressor.weatherapp.db.City;
import ru.gressor.weatherapp.db.WeatherDao;
import ru.gressor.weatherapp.ui.App;

public class HistoryStorage implements Parcelable {
    public static final int MAX_SIZE = 10;
    public static final int MAX_FAVORITES = 5;
    public static final String HISTORY_STORAGE = "HistoryStorage";

    private LinkedList<HistoryItem> historyItems;
    private WeatherDao weatherDao;

    public HistoryStorage() {
        initDao();
        historyItems = loadHistory();
    }

    private LinkedList<HistoryItem> loadHistory() {
        List<City> cities = weatherDao.getAllCities();
        LinkedList<HistoryItem> historyItems = new LinkedList<>();

        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            PositionPoint positionPoint = new PositionPoint(city.getCityName(), null);
            positionPoint.setCoord(city.getCoord());

            WeatherState weatherState = new WeatherState(
                    new ActualWeather(city.getTemperature(), city.getActualAt()),
                    null, null);

            HistoryItem item = new HistoryItem(weatherState, positionPoint);
            item.setFavorite(city.isFavorite());

            historyItems.push(item);
        }

        return historyItems;
    }

    private void initDao() {
        if (weatherDao == null) {
            weatherDao = App.getInstance().getWeatherDao();
        }
    }

    public void push(HistoryItem historyItem) {
        boolean isFavorite = removeSimilar(historyItem);
        if (isFavorite) historyItem.setFavorite(true);

        historyItems.push(historyItem);
        weatherDao.insertCity(createCity(historyItem));

        LinkedList<HistoryItem> favorites = getSublistFirstExcluded(true);
        int favoritesCount = favorites.size() + (historyItem.isFavorite() ? 1 : 0);
        if (favoritesCount > MAX_FAVORITES) {
            favorites.get(favorites.size() - 1).setFavorite(false);
        }

        if (historyItems.size() > MAX_SIZE) {
            LinkedList<HistoryItem> nonFavorites = getSublistFirstExcluded(false);
            removeSimilar(nonFavorites.get(nonFavorites.size() - 1));
        }
    }

    public City createCity(HistoryItem item) {
        Coord coord = item.getPositionPoint().getCoord();
        WeatherState weatherState = item.getWeatherState();

        Calendar calendar = null;
        int temperature = 0;
        if (weatherState != null) {
            calendar = weatherState.getActualWeather().getActualAt();
            temperature = weatherState.getActualWeather().getTemperature();
        }

        return new City(
                item.getPositionPoint().getTown(), coord, item.isFavorite(), calendar, temperature);
    }

    public void invalidate() {
        push(historyItems.get(0));
    }

    public List<HistoryItem> getItemsList() {
        List<HistoryItem> result = getSublistFirstExcluded(true);
        result.addAll(getSublistFirstExcluded(false));
        result.add(0, historyItems.get(0));
        return result;
    }

    public PositionPoint getCurrentPosition() {
        return historyItems.get(0).getPositionPoint();
    }

    public WeatherState getCurrentWeather() {
        return historyItems.get(0).getWeatherState();
    }

    private boolean removeSimilar(HistoryItem historyItem) {
        return removeSimilar(historyItem.getPositionPoint().getTown());
    }

    private boolean removeSimilar(String townName) {
        weatherDao.deleteCity(townName);

        for (int i = 0; i < historyItems.size(); i++) {
            HistoryItem item = historyItems.get(i);
            if (item.getPositionPoint().getTown().equals(townName)) {
                boolean isFavorite = item.isFavorite();
                historyItems.remove(item);
                return isFavorite;
            }
        }
        return false;
    }

    public void clear(String[] townList) {
        for (String townName : townList) {
            if (townName != null) removeSimilar(townName);
        }
    }

    public int size() {
        return historyItems.size();
    }

    private LinkedList<HistoryItem> getSublistFirstExcluded(boolean isFavorite) {
        LinkedList<HistoryItem> result = new LinkedList<>();

        for (int i = 1; i < historyItems.size(); i++) {
            if (historyItems.get(i).isFavorite() == isFavorite) {
                result.offer(historyItems.get(i));
            }
        }
        return result;
    }

    public static final Creator<HistoryStorage> CREATOR = new Creator<HistoryStorage>() {
        @Override
        public HistoryStorage createFromParcel(Parcel parcel) {
            return new HistoryStorage(parcel);
        }

        @Override
        public HistoryStorage[] newArray(int size) {
            return new HistoryStorage[size];
        }
    };

    protected HistoryStorage(Parcel parcel) {
        HistoryItem[] array = (HistoryItem[]) parcel.readArray(HistoryItem.class.getClassLoader());
        historyItems = new LinkedList<>(Arrays.asList(array));
        initDao();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        HistoryItem[] array = new HistoryItem[historyItems.size()];
        parcel.writeParcelableArray(historyItems.toArray(array), flags);
    }
}
