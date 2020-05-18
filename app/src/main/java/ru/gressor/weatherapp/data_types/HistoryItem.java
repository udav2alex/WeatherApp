package ru.gressor.weatherapp.data_types;

public class HistoryItem {
    private WeatherState weatherState;
    private PositionPoint positionPoint;
    private boolean isFavorite;

    public HistoryItem(WeatherState weatherState, PositionPoint positionPoint) {
        this.weatherState = weatherState;
        this.positionPoint = positionPoint;
        this.isFavorite = false;
    }

    public WeatherState getWeatherState() {
        return weatherState;
    }

    public PositionPoint getPositionPoint() {
        return positionPoint;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
