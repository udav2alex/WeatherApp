package ru.gressor.weatherapp.ui.fragments.forecast;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.ui.fragments.BaseFragment;

public class FragmentForecast extends BaseFragment {

    public FragmentForecast() {
    }

    public static FragmentForecast create(WeatherState weatherState,
                                          PositionPoint positionPoint) {
        FragmentForecast fragmentForecast = new FragmentForecast();

        Bundle args = new Bundle();
        args.putParcelable(WeatherState.WEATHER_STATE, weatherState);
        args.putParcelable(PositionPoint.CURRENT_POSITION, positionPoint);
        fragmentForecast.setArguments(args);

        return fragmentForecast;
    }

    public WeatherState getWeatherState() {
        if (getArguments() != null) {
            return getArguments().getParcelable(WeatherState.WEATHER_STATE);
        } else {
            return null;
        }
    }

    public PositionPoint getPositionPoint() {
        if (getArguments() != null) {
            return getArguments().getParcelable(PositionPoint.CURRENT_POSITION);
        } else {
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        view.findViewById(R.id.linkToSite).setOnClickListener(textLinkToSiteOnClickListener);

        RecyclerView recyclerViewForecast = view.findViewById(R.id.recyclerViewForecast);
        recyclerViewForecast.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewForecast.setLayoutManager(layoutManager);

        if (getWeatherState() != null) {
            recyclerViewForecast.setAdapter(
                    new ForecastListAdapter(this, getWeatherState().getDailyForecast()));
        }

        return view;
    }

    private View.OnClickListener textLinkToSiteOnClickListener = (v) -> {
        PositionPoint positionPoint = getPositionPoint();

        if (positionPoint != null) {
            String query = String.format("%s %s %s",
                    getResources().getString(R.string.weather),
                    positionPoint.getTown(),
                    positionPoint.getSite());
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, query);

            startActivity(intent);
        }
    };
}
