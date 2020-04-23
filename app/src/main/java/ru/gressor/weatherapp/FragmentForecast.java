package ru.gressor.weatherapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentForecast extends Fragment {
    public static final String CURRENT_DESTINATION = "currentDestination";

    public FragmentForecast() {
    }

    public static FragmentForecast create(DestinationPoint currentDestination) {
        FragmentForecast fragmentForecast = new FragmentForecast();

        Bundle args = new Bundle();
        args.putParcelable(CURRENT_DESTINATION, currentDestination);
        fragmentForecast.setArguments(args);

        return fragmentForecast;
    }

    public DestinationPoint getCurrentDestination() {
        if (getArguments() != null) {
            return getArguments().getParcelable(CURRENT_DESTINATION);
        } else {
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_forecast, container, false);

        result.findViewById(R.id.linkToSite).setOnClickListener(textLinkToSiteOnClickListener);

        return result;
    }

    private View.OnClickListener textLinkToSiteOnClickListener = (v) -> {
        DestinationPoint currentDestination = getCurrentDestination();

        if (currentDestination != null) {
            String query = String.format("%s %s %s",
                    getResources().getString(R.string.weather),
                    currentDestination.getTown(),
                    currentDestination.getSite());
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, query);

            startActivity(intent);
        }
    };
}
