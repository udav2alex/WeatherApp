package ru.gressor.weatherapp.data_types;

public enum TemperatureScale {
    CELSIUS, FAHRENHEIT;

    public String fromCelsius(int value, String errorMessage) {
        if (this == CELSIUS) {
            return (value < 0 ? "–" : "+") + value + " ℃";
        }

        if (this == FAHRENHEIT) {
            int converted = (int)((value - 32)/1.8);
            return  (converted < 0 ? "–" : "+") + converted + " ℉";
        }

        return errorMessage;
    }
}
