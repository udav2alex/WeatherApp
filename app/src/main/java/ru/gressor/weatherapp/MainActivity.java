package ru.gressor.weatherapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TScale tScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        tScale = TScale.CELSIUS;

        TextView textViewTown = findViewById(R.id.textViewTown);
        textViewTown.setOnClickListener(textViewTownOnClickListener);

        TextView textViewCurrentTemperature = findViewById(R.id.currentTemperature);
        textViewCurrentTemperature.setText(tScale.fromCelsius(8));

        TextView textViewFeelsLike = findViewById(R.id.feelsLike);
        textViewFeelsLike.setText(
                getApplicationContext().getString(R.string.feels_like,
                tScale.fromCelsius(6)));

        TextView textViewWindSpeed = findViewById(R.id.windSpeed);
        textViewWindSpeed.setText(
                getApplicationContext().getString(R.string.windSpeed, 7));

        TextView textViewPressureValue = findViewById(R.id.pressureValue);
        textViewPressureValue.setText(
                getApplicationContext().getString(R.string.pressureValue, 755));

        TextView textViewHumidityValue = findViewById(R.id.humidityValue);
        textViewHumidityValue.setText(
                getApplicationContext().getString(R.string.humidityValue, 55));

        TextView textViewTempNow = findViewById(R.id.tempNow);
        textViewTempNow.setText(tScale.fromCelsius(8));

        TextView textViewTempNext3H = findViewById(R.id.tempNext3H);
        textViewTempNext3H.setText(tScale.fromCelsius(7));

        TextView textViewTempNext6H = findViewById(R.id.tempNext6H);
        textViewTempNext6H.setText(tScale.fromCelsius(5));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SelectTown.GET_TOWN && resultCode == RESULT_OK && data != null) {
            townChanged(data.getStringExtra("town"));
        }
    }

    private View.OnClickListener textViewTownOnClickListener = (v) -> {
        Intent intent = new Intent(this, SelectTown.class);
        startActivityForResult(intent, SelectTown.GET_TOWN);
    };

    private void townChanged(String town) {
        TextView textViewTown = findViewById(R.id.textViewTown);
        textViewTown.setText(town);
    }

    private String getTemp() {
        return null;
    }

    public enum TScale {
        CELSIUS, FAHRENHEIT;

        public String fromCelsius(int value) {
            if (this == CELSIUS) {
                return (value < 0 ? "–" : "+") + value + " ℃";
            }

            if (this == FAHRENHEIT) {
                int converted = (int)((value - 32)/1.8);
                return  (converted < 0 ? "–" : "+") + converted + " ℉";
            }

            return "Error! Unknown scale!";
        }
    }
}
