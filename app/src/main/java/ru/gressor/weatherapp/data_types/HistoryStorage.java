package ru.gressor.weatherapp.data_types;

import java.util.LinkedList;
import java.util.List;

public class HistoryStorage {
    public final int MAX_SIZE = 10;
    public final int MAX_FAVORITES = 5;

    private LinkedList<HistoryItem> historyItems = new LinkedList<>();

    public void push(HistoryItem historyItem) {
        boolean isFavorite = removeSimilar(historyItem);
        if (isFavorite) historyItem.setFavorite(true);

        historyItems.push(historyItem);

        LinkedList<HistoryItem> favorites = getFavoritesFrom1(true);
        if (favorites.size() > MAX_SIZE || favorites.size() > MAX_FAVORITES) {
            favorites.get(favorites.size() - 1).setFavorite(false);
        }

        if (historyItems.size() > MAX_SIZE) {
            LinkedList<HistoryItem> nonFavorites = getFavoritesFrom1(false);
            removeSimilar(nonFavorites.get(nonFavorites.size() - 1));
        }
    }

    public List<HistoryItem> getItemsList() {
        List<HistoryItem> result = getFavoritesFrom1(true);
        result.addAll(getFavoritesFrom1(false));
        result.add(0, historyItems.get(0));
        return result;
    }

    private boolean removeSimilar(HistoryItem historyItem) {
        for (HistoryItem item : historyItems) {
            if (item.getPositionPoint().getTown().equals(historyItem.getPositionPoint().getTown())) {
                boolean isFavorite = item.isFavorite();
                historyItems.remove(item);
                return isFavorite;
            }
        }
        return false;
    }

    private LinkedList<HistoryItem> getFavoritesFrom1(boolean isFavorite) {
        LinkedList<HistoryItem> result = new LinkedList<>();

        for (int i = 1; i < historyItems.size(); i++) {
            if (historyItems.get(i).isFavorite() == isFavorite) {
                result.offer(historyItems.get(i));
            }
        }
        return result;
    }
}
