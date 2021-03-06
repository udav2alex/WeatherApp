package ru.gressor.weatherapp.ui.fragments.today;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ru.gressor.weatherapp.tools.battery.BatteryObserver;
import ru.gressor.weatherapp.tools.battery.BatteryPublisherProvider;
import ru.gressor.weatherapp.data_types.PositionPoint;
import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.tools.connectivity.ConnectivityObserver;
import ru.gressor.weatherapp.tools.connectivity.ConnectivityPublisherProvider;
import ru.gressor.weatherapp.ui.App;
import ru.gressor.weatherapp.ui.MainActivity;
import ru.gressor.weatherapp.ui.SelectTownActivity;
import ru.gressor.weatherapp.data_types.TemperatureScale;
import ru.gressor.weatherapp.data_types.ActualWeather;
import ru.gressor.weatherapp.data_types.WeatherState;
import ru.gressor.weatherapp.ui.fragments.BaseFragment;
import ru.gressor.weatherapp.weather_providers.DataController;

public class FragmentWeatherToday extends BaseFragment
        implements BatteryObserver, ConnectivityObserver {
    private View fragmentView;

    private TextView textViewTown;
    private TextView textViewSite;
    private TextView textViewCurrentTemperature;
    private ImageView imageViewConditionsImage;
    private TextView textViewConditions;
    private TextView textViewFeelsLike;
    private TextView textViewBatteryState;
    private TextView textViewConnectivityState;

    public FragmentWeatherToday() {
    }

    public static FragmentWeatherToday create(WeatherState weatherState,
                                              PositionPoint positionPoint) {
        FragmentWeatherToday fragmentWeatherToday = new FragmentWeatherToday();

        Bundle args = new Bundle();
        args.putParcelable(WeatherState.WEATHER_STATE, weatherState);
        args.putParcelable(PositionPoint.CURRENT_POSITION, positionPoint);
        fragmentWeatherToday.setArguments(args);

        ((BatteryPublisherProvider) App.getInstance())
                .getBatteryPublisher().registerBatteryObserver(fragmentWeatherToday);

        ((ConnectivityPublisherProvider) App.getInstance())
                .getConnectivityPublisher().registerConnectivityObserver(fragmentWeatherToday);

        return fragmentWeatherToday;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_weather_today,
                container, false);

        init(fragmentView);
        initRecyclerView(fragmentView);

        updateConnectivityIndicator();

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
        textViewBatteryState = view.findViewById(R.id.batteryState);
        textViewConnectivityState = view.findViewById(R.id.connectivityState);

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
                    getWeatherState().getActualWeather(), getWeatherState().getHourlyForecast()));
        }
    }

    private void populate() {
        Context context = fragmentView.getContext();

        PositionPoint currentPosition = getPositionPoint();
        if (currentPosition.getTown() == null) {
            textViewTown.setText(Objects.requireNonNull(getActivity())
                    .getResources().getString(R.string.current_location));
            textViewSite.setText(String.format(Objects.requireNonNull(getActivity())
                    .getResources().getString(R.string.lon_lat_description),
                    currentPosition.getCoord().getLon(),
                    currentPosition.getCoord().getLat()));
        } else {
            textViewTown.setText(currentPosition.getTown());
            textViewSite.setText(currentPosition.getSite());
        }

        updateBatteryIndicator();

        if (getWeatherState() == null) return;
        ActualWeather actualWeather = getWeatherState().getActualWeather();
        if (actualWeather == null) return;

        TemperatureScale tScale = TemperatureScale.getScale();
        String errorMessage = context.getResources().getString(R.string.error_unknown_scale);

        textViewCurrentTemperature.setText(
                tScale.fromCelsius(actualWeather.getTemperature(), errorMessage));

        setDrawableByFileName(imageViewConditionsImage, context, actualWeather.getIconFileName());

        textViewConditions.setText(actualWeather.getConditionsDescription());
        textViewFeelsLike.setText(context.getString(R.string.feels_like,
                tScale.fromCelsius(actualWeather.getTempFeelsLike(), errorMessage)));
    }

    private void showBatteryState(String state, int drawableRes) {
        textViewBatteryState.setText(state);
        textViewBatteryState.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawableRes, 0, 0, 0);
    }

    public void updateBatteryIndicator() {
        updateBatteryIndicator(null);
    }

    public void updateBatteryIndicator(String state) {
        Context context = getActivity();
        if (context == null) return;

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);

        assert batteryStatus != null;
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging =
                status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        if (isCharging) {
            state = context.getString(R.string.battery_connected);
        } else if (state == null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level / (float)scale;

            if (batteryPct < 0.1f) {
                state = context.getString(R.string.battery_low);
            } else {
                state = context.getString(R.string.battery_ok);
            }
        }

        showBatteryState(state,
                batteryStatus.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0));
    }

    public void batteryUpdated(Intent intent) {
        Context context = getActivity();
        if (context == null) return;

        String action = intent.getAction();
        String state = null;
        if (    "android.intent.action.ACTION_POWER_DISCONNECTED".equals(action) ||
                "android.intent.action.BATTERY_OKAY".equals(action)) {
            state = context.getString(R.string.battery_ok);
        } else if ("android.intent.action.BATTERY_LOW".equals(action)) {
            state = context.getString(R.string.battery_low);
        }

        updateBatteryIndicator(state);
    }

    @Override
    public void connectivityUpdated(Intent intent) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) return;
        
        if (DataController.isOnline(activity)) {
            showConnectivityState(activity.getString(R.string.network_online), android.R.drawable.presence_online);
        } else {
            showConnectivityState(activity.getString(R.string.network_offline), android.R.drawable.presence_offline);
            if (intent != null) {
                activity.showMessage(
                        getActivity().getResources().getString(R.string.provider_message_no_internet),
                        getActivity().getResources().getString(R.string.provider_message_internet_check));
            }
        }
    }

    private void showConnectivityState(String string, int drawableRes) {
        textViewConnectivityState.setText(string);
        textViewConnectivityState.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,0 ,drawableRes,0);
    }

    private void updateConnectivityIndicator() {
        connectivityUpdated(null);
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

    public PositionPoint getPositionPoint() {
        if (getArguments() != null) {
            return getArguments().getParcelable(PositionPoint.CURRENT_POSITION);
        } else {
            return null;
        }
    }
}
