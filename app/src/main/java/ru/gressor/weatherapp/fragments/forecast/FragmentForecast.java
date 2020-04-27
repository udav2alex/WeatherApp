package ru.gressor.weatherapp.fragments.forecast;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.weather.ForecastData;

public class FragmentForecast extends Fragment {

    public FragmentForecast() {
    }

    public static FragmentForecast create(PositionPoint currentDestination) {
        FragmentForecast fragmentForecast = new FragmentForecast();

        Bundle args = new Bundle();
        args.putParcelable(PositionPoint.CURRENT_POSITION, currentDestination);
        fragmentForecast.setArguments(args);

        return fragmentForecast;
    }

    public PositionPoint getCurrentPosition() {
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

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                Objects.requireNonNull(getActivity()),  LinearLayoutManager.VERTICAL);

        itemDecoration.setDrawable(
                Objects.requireNonNull(getActivity().getDrawable(R.drawable.forecast_separator)));
        recyclerViewForecast.addItemDecoration(itemDecoration);


        ForecastListAdapter adapter = new ForecastListAdapter(new ForecastData());
        recyclerViewForecast.setAdapter(adapter);

        return view;
    }

    private View.OnClickListener textLinkToSiteOnClickListener = (v) -> {
        PositionPoint currentDestination = getCurrentPosition();

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
