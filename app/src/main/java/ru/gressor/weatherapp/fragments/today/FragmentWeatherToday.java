package ru.gressor.weatherapp.fragments.today;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.activities.SelectTownActivity;
import ru.gressor.weatherapp.data_types.TemperatureScale;
import ru.gressor.weatherapp.data_types.CurrentWeather;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.fragments.BaseFragment;

public class FragmentWeatherToday extends BaseFragment {
    private View fragmentView;

    private TextView textViewTown;
    private TextView textViewSite;
    private TextView textViewCurrentTemperature;
    private ImageView imageViewConditionsImage;
    private TextView textViewConditions;
    private TextView textViewFeelsLike;

    public FragmentWeatherToday() {
    }

    public static FragmentWeatherToday create(WeatherState weatherState,
                                              PositionPoint currentDestination) {
        FragmentWeatherToday fragmentWeatherToday = new FragmentWeatherToday();

        Bundle args = new Bundle();
        args.putParcelable(WeatherState.WEATHER_STATE, weatherState);
        args.putParcelable(PositionPoint.CURRENT_POSITION, currentDestination);
        fragmentWeatherToday.setArguments(args);

        return fragmentWeatherToday;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_weather_today,
                container, false);

        init(fragmentView);
        initRecyclerView(fragmentView);

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        populate();
    }

    private void init(View view) {
        textViewTown = view.findViewById(R.id.textViewTown);
        textViewSite = view.findViewById(R.id.textViewSite);
        textViewCurrentTemperature = view.findViewById(R.id.currentTemperature);
        imageViewConditionsImage = view.findViewById(R.id.conditionsImage);
        textViewConditions = view.findViewById(R.id.conditions);
        textViewFeelsLike = view.findViewById(R.id.feelsLike);

        textViewTown.setOnClickListener(textViewTownOnClickListener);
        textViewSite.setOnClickListener(textViewTownOnClickListener);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerViewTodayWeather = view.findViewById(R.id.recycler_view_today);
        recyclerViewTodayWeather.setHasFixedSize(true);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerViewTodayWeather.setLayoutManager(layoutManager);

        if (getWeatherState() != null) {
            recyclerViewTodayWeather.setAdapter(new TodayWeatherListAdapter(this,
                    getWeatherState().getCurrentWeather(), getWeatherState().getHourlyForecast()));
        }
    }

    private void populate() {
        Context context = fragmentView.getContext();

        PositionPoint currentDestination = getCurrentPosition();
        textViewTown.setText(currentDestination.getTown());
        textViewSite.setText(currentDestination.getSite());

        WeatherState weatherState = getWeatherState();
        if (weatherState == null) return;

        CurrentWeather currentWeather = getWeatherState().getCurrentWeather();
        if (currentWeather == null) return;

        TemperatureScale tScale = TemperatureScale.getScale();
        String errorMessage = context.getResources().getString(R.string.error_unknown_scale);

        textViewCurrentTemperature.setText(
                tScale.fromCelsius(currentWeather.getTemperature(), errorMessage));

        setDrawableByFileName(imageViewConditionsImage, context, currentWeather.getIconFileName());

        textViewConditions.setText(currentWeather.getConditionsDescription());
        textViewFeelsLike.setText(context.getString(R.string.feels_like,
                tScale.fromCelsius(currentWeather.getTempFeelsLike(), errorMessage)));
    }

    private View.OnClickListener textViewTownOnClickListener = (v) -> {
        Intent intent = new Intent(fragmentView.getContext(), SelectTownActivity.class);
        if (getActivity() != null) {
            getActivity().startActivityForResult(intent, SelectTownActivity.GET_TOWN);
        }
    };

    public WeatherState getWeatherState() {
        if (getArguments() != null) {
            return getArguments().getParcelable(WeatherState.WEATHER_STATE);
        } else {
            return null;
        }
    }

    public PositionPoint getCurrentPosition() {
        if (getArguments() != null) {
            return getArguments().getParcelable(PositionPoint.CURRENT_POSITION);
        } else {
            return null;
        }
    }
}
