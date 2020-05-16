package ru.gressor.weatherapp.data_types;

public enum TemperatureScale {
    CELSIUS, FAHRENHEIT;

    private static TemperatureScale temperatureScale = TemperatureScale.CELSIUS;

    public static TemperatureScale getScale() {
        return temperatureScale;
    }

    public String fromCelsius(int value, String errorMessage) {
        if (this == CELSIUS) {
            return (value < 0 ? "–" : "+") + Math.abs(value) + " ℃";
        }

        if (this == FAHRENHEIT) {
            int converted = (int)((value - 32)/1.8);
            return  (converted < 0 ? "–" : "+") + Math.abs(converted) + " ℉";
        }

        return errorMessage;
    }

    public static String getTemperatureScaled(int temperature, String errorMessage) {
        return temperatureScale.fromCelsius(temperature, errorMessage);
    }
}
