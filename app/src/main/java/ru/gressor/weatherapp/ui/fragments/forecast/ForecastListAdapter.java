package ru.gressor.weatherapp.ui.fragments.forecast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ru.gressor.weatherapp.data_types.DayForecast;
import ru.gressor.weatherapp.data_types.TemperatureScale;
import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.ui.fragments.BaseFragment;

public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ViewHolder> {
    private List<DayForecast> forecastData;
    private BaseFragment fragment;

    public ForecastListAdapter(BaseFragment fragment, List<DayForecast> forecastData) {
        this.fragment = fragment;
        this.forecastData = forecastData;
    }

    @NonNull
    @Override
    public ForecastListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.forecast_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastListAdapter.ViewHolder viewHolder, int i) {
        String errorMessage = viewHolder.getTextViewTempHi()
                .getResources().getString(R.string.error_unknown_scale);
        DateFormat dateDF = DateFormat.getDateInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

        Calendar actualAt = forecastData.get(i).getActualAt();

        viewHolder.getTextViewDate().setText(dateDF.format(actualAt.getTimeInMillis()));
        viewHolder.getTextViewDay().setText(sdf.format(actualAt.getTimeInMillis()));

        viewHolder.getTextViewTempHi().setText(
                TemperatureScale.getTemperatureScaled(
                forecastData.get(i).getMaxTemperature(), errorMessage));

        viewHolder.getTextViewTempLow().setText(
                TemperatureScale.getTemperatureScaled(
                forecastData.get(i).getMinTemperature(),errorMessage));

        fragment.setDrawableByFileName(viewHolder.getWeatherIcon(),
                fragment.getContext(), forecastData.get(i).getIconFileName());
    }

    @Override
    public int getItemCount() {
        if (forecastData == null) return 0;
        return forecastData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewDay;
        private ImageView weatherIcon;
        private TextView textViewTempHi;
        private TextView textViewTempLow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDay  = itemView.findViewById(R.id.textViewDay);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
            textViewTempHi = itemView.findViewById(R.id.textViewTempHi);
            textViewTempLow = itemView.findViewById(R.id.textViewTempLow);
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public TextView getTextViewDay() {
            return textViewDay;
        }

        public ImageView getWeatherIcon() {
            return weatherIcon;
        }

        public TextView getTextViewTempHi() {
            return textViewTempHi;
        }

        public TextView getTextViewTempLow() {
            return textViewTempLow;
        }
    }
}
