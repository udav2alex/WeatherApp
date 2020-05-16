package ru.gressor.weatherapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.activities.SelectTownActivity;
import ru.gressor.weatherapp.data_types.TemperatureScale;
import ru.gressor.weatherapp.data_types.CurrentWeather;
import ru.gressor.weatherapp.data_types.WeatherState;

public class FragmentWeatherToday extends Fragment {
    private View fragmentView;

    private TextView textViewTown;
    private TextView textViewSite;
    private TextView textViewCurrentTemperature;
    private ImageView imageViewConditionsImage;
    private TextView textViewConditions;
    private TextView textViewFeelsLike;
    private TextView textViewWindSpeed;
    private TextView textViewPressureValue;
    private TextView textViewHumidityValue;
    private TextView textViewTempNow;
    private TextView textViewTempNext3H;
    private TextView textViewTempNext6H;

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
        imageViewConditionsImage = fragmentView.findViewById(R.id.conditionsImage);
        textViewConditions = fragmentView.findViewById(R.id.conditions);
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

        PositionPoint currentDestination = getCurrentPosition();
        textViewTown.setText(currentDestination.getTown());
        textViewSite.setText(currentDestination.getSite());

        WeatherState weatherState = getWeatherState();
        if (weatherState == null) return;

        CurrentWeather currentWeather = getWeatherState().getCurrentWeather();
        if (currentWeather == null) return;

        TemperatureScale tScale = CurrentWeather.getTemperatureScale();
        String errorMessage = context.getResources().getString(R.string.error_unknown_scale);

        textViewCurrentTemperature.setText(
                tScale.fromCelsius(currentWeather.getTemperature(), errorMessage));

        setWeatherIcon(imageViewConditionsImage, context, currentWeather);

        textViewConditions.setText(currentWeather.getConditionsDescription());
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

    private void setWeatherIcon(ImageView imageView, Context context, CurrentWeather currentWeather) {
        String iconFileName = currentWeather.getIconFileName();
        if (iconFileName != null) {
            String drawableName = context.getPackageName() + ":drawable/w" + iconFileName;
            int iconId = context.getResources()
                    .getIdentifier(drawableName, null, null);

            if (iconId != 0) {
                imageView.setImageDrawable(context.getDrawable(iconId));
                imageView.setContentDescription(currentWeather.getConditionsDescription());
            }
        }
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
