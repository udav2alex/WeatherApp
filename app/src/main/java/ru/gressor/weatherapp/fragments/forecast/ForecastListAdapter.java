package ru.gressor.weatherapp.fragments.forecast;

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

import ru.gressor.weatherapp.data_types.getDailyForecast;
import ru.gressor.weatherapp.R;

public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ViewHolder> {
    private getDailyForecast forecastData;

    public ForecastListAdapter(getDailyForecast forecastData) {
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

        Calendar actualAt = forecastData.getForecastFor(i).getActualAt();

        viewHolder.getTextViewDate().setText(dateDF.format(actualAt.getTimeInMillis()));
        viewHolder.getTextViewDay().setText(sdf.format(actualAt.getTimeInMillis()));
        viewHolder.getTextViewTempHi().setText(
                forecastData.getForecastFor(i).getTemperatureScaled(errorMessage));
        viewHolder.getTextViewTempLow().setText(
                forecastData.getForecastFor(i).getTempFeelsLikeScaled(errorMessage));
    }

    @Override
    public int getItemCount() {
        return forecastData.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewDay;
        private ImageView weatherConditions;
        private TextView textViewTempHi;
        private TextView textViewTempLow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDay  = itemView.findViewById(R.id.textViewDay);
            weatherConditions = itemView.findViewById(R.id.weatherConditions);
            textViewTempHi = itemView.findViewById(R.id.textViewTempHi);
            textViewTempLow = itemView.findViewById(R.id.textViewTempLow);
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public TextView getTextViewDay() {
            return textViewDay;
        }

        public ImageView getWeatherConditions() {
            return weatherConditions;
        }

        public TextView getTextViewTempHi() {
            return textViewTempHi;
        }

        public TextView getTextViewTempLow() {
            return textViewTempLow;
        }
    }
}
