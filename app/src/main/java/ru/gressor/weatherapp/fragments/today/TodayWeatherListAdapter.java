package ru.gressor.weatherapp.fragments.today;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.CurrentWeather;
import ru.gressor.weatherapp.data_types.HourForecast;
import ru.gressor.weatherapp.data_types.TemperatureScale;
import ru.gressor.weatherapp.fragments.BaseFragment;

public class TodayWeatherListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<HourForecast> forecastData;
    private CurrentWeather currentWeather;
    private BaseFragment fragment;

    public TodayWeatherListAdapter(BaseFragment fragment,
            CurrentWeather currentWeather, List<HourForecast> forecastData) {
        this.fragment = fragment;
        this.currentWeather = currentWeather;
        this.forecastData = forecastData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.today_weather_list_header, viewGroup, false);
            return new ViewHolderHeader(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.today_weather_list_item, viewGroup, false);
            return new ViewHolderItem(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolderHeader) {
            ViewHolderHeader viewHolderHeader = (ViewHolderHeader) viewHolder;

            viewHolderHeader.getTextViewWindSpeed()
                    .setText(fragment.getContext().getString(R.string.windSpeed, currentWeather.getWindSpeed()));
            viewHolderHeader.getTextViewPressure().setText(
                    fragment.getContext().getString(R.string.pressureValue, currentWeather.getPressure()));
            viewHolderHeader.getTextViewHumidity().setText(
                    fragment.getContext().getString(R.string.humidityValue, currentWeather.getHumidity()));

        } else if (viewHolder instanceof ViewHolderItem) {
            ViewHolderItem viewHolderItem = (ViewHolderItem) viewHolder;
            String errorMessage = viewHolderItem.getTextViewTime()
                    .getResources().getString(R.string.error_unknown_scale);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            int index = position - 1;

            Calendar actualAt = forecastData.get(index).getActualAt();

            viewHolderItem.getTextViewTime().setText(sdf.format(actualAt.getTimeInMillis()));
            viewHolderItem.getTextViewTemperature().setText(
                    TemperatureScale.getTemperatureScaled(
                            forecastData.get(index).getTemperature(), errorMessage));

            fragment.setDrawableByFileName(viewHolderItem.getWeatherIcon(),
                    fragment.getContext(), forecastData.get(index).getIconFileName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return forecastData.size() + 1;
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView textViewTime;
        private ImageView weatherIcon;
        private TextView textViewTemperature;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);

            textViewTime = itemView.findViewById(R.id.today_weather_time);
            weatherIcon = itemView.findViewById(R.id.today_weather_icon);
            textViewTemperature = itemView.findViewById(R.id.today_weather_temperature);
        }

        public TextView getTextViewTime() {
            return textViewTime;
        }

        public ImageView getWeatherIcon() {
            return weatherIcon;
        }

        public TextView getTextViewTemperature() {
            return textViewTemperature;
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        private TextView textViewWindSpeed;
        private TextView textViewPressure;
        private TextView textViewHumidity;

        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);

            textViewWindSpeed = itemView.findViewById(R.id.today_weather_wind_speed);
            textViewPressure = itemView.findViewById(R.id.today_weather_pressure);
            textViewHumidity = itemView.findViewById(R.id.today_weather_humidity);
        }

        public TextView getTextViewWindSpeed() {
            return textViewWindSpeed;
        }

        public TextView getTextViewPressure() {
            return textViewPressure;
        }

        public TextView getTextViewHumidity() {
            return textViewHumidity;
        }
    }
}
