package ru.gressor.weatherapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.activities.SelectTownActivity;
import ru.gressor.weatherapp.data_types.TemperatureScale;
import ru.gressor.weatherapp.data_types.WeatherState;

public class FragmentWeatherToday extends Fragment {
    private View fragmentView;

    private TextView textViewTown;
    private TextView textViewSite;
    private TextView textViewCurrentTemperature;
    private TextView textViewFeelsLike;
    private TextView textViewWindSpeed;
    private TextView textViewPressureValue;
    private TextView textViewHumidityValue;
    private TextView textViewTempNow;
    private TextView textViewTempNext3H;
    private TextView textViewTempNext6H;

    public FragmentWeatherToday() {
    }

    public static FragmentWeatherToday create(WeatherState currentWeather,
                                              PositionPoint currentDestination) {
        FragmentWeatherToday fragmentWeatherToday = new FragmentWeatherToday();

        Bundle args = new Bundle();
        args.putParcelable(WeatherState.CURRENT_WEATHER, currentWeather);
        args.putParcelable(PositionPoint.CURRENT_POSITION, currentDestination);
        fragmentWeatherToday.setArguments(args);

        return fragmentWeatherToday;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_weather_today,
                container, false);

        init();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        populate();
    }

    private void init() {
        textViewTown = fragmentView.findViewById(R.id.textViewTown);
        textViewSite = fragmentView.findViewById(R.id.textViewSite);
        textViewCurrentTemperature = fragmentView.findViewById(R.id.currentTemperature);
        textViewFeelsLike = fragmentView.findViewById(R.id.feelsLike);
        textViewWindSpeed = fragmentView.findViewById(R.id.windSpeed);
        textViewPressureValue = fragmentView.findViewById(R.id.pressureValue);
        textViewHumidityValue = fragmentView.findViewById(R.id.humidityValue);
        textViewTempNow = fragmentView.findViewById(R.id.tempNow);
        textViewTempNext3H = fragmentView.findViewById(R.id.tempNext3H);
        textViewTempNext6H = fragmentView.findViewById(R.id.tempNext6H);

        textViewTown.setOnClickListener(textViewTownOnClickListener);
        textViewSite.setOnClickListener(textViewTownOnClickListener);
    }

    private void populate() {
        Context context = fragmentView.getContext();

        TemperatureScale tScale = WeatherState.getTemperatureScale();
        PositionPoint currentDestination = getCurrentPosition();
        WeatherState currentWeather = getCurrentWeather();
        String errorMessage = context.getResources().getString(R.string.error_unknown_scale);

        textViewTown.setText(currentDestination.getTown());
        textViewSite.setText(currentDestination.getSite());

        textViewCurrentTemperature.setText(
                tScale.fromCelsius(currentWeather.getTemperature(), errorMessage));
        textViewFeelsLike.setText(context.getString(R.string.feels_like,
                tScale.fromCelsius(currentWeather.getTempFeelsLike(), errorMessage)));
        textViewWindSpeed.setText(context.getString(R.string.windSpeed,
                currentWeather.getWindSpeed()));
        textViewPressureValue.setText(context.getString(R.string.pressureValue,
                currentWeather.getPressure()));
        textViewHumidityValue.setText(context.getString(R.string.humidityValue,
                currentWeather.getHumidity()));
        textViewTempNow.setText(
                tScale.fromCelsius(currentWeather.getTemperature(), errorMessage));

        textViewTempNext3H.setText(tScale.fromCelsius(7, errorMessage));
        textViewTempNext6H.setText(tScale.fromCelsius(5, errorMessage));
    }

    private View.OnClickListener textViewTownOnClickListener = (v) -> {
        Intent intent = new Intent(fragmentView.getContext(), SelectTownActivity.class);
        if (getActivity() != null) {
            getActivity().startActivityForResult(intent, SelectTownActivity.GET_TOWN);
        }
    };

    public WeatherState getCurrentWeather() {
        if (getArguments() != null) {
            return getArguments().getParcelable(WeatherState.CURRENT_WEATHER);
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
