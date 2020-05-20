package ru.gressor.weatherapp.data_types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HistoryStorage implements Parcelable {
    public static final int MAX_SIZE = 10;
    public static final int MAX_FAVORITES = 5;
    public static final String HISTORY_STORAGE = "HistoryStorage";

    private LinkedList<HistoryItem> historyItems = new LinkedList<>();

    public HistoryStorage() {
    }

    protected HistoryStorage(Parcel parcel) {
        HistoryItem[] array = (HistoryItem[]) parcel.readArray(HistoryItem.class.getClassLoader());
        historyItems = new LinkedList<>(Arrays.asList(array));
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

    public void push(HistoryItem historyItem) {
        boolean isFavorite = removeSimilar(historyItem);
        if (isFavorite) historyItem.setFavorite(true);

        historyItems.push(historyItem);

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
        for (int i = 0; i < historyItems.size(); i++) {
            HistoryItem item = historyItems.get(i);
            if (item.getPositionPoint().getTown().equals(historyItem.getPositionPoint().getTown())) {
                boolean isFavorite = item.isFavorite();
                historyItems.remove(item);
                return isFavorite;
            }
        }
        return false;
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
