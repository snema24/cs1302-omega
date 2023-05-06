package cs1302.api;

/**
 * Represents a result from the Unsplash API. This is
 * used by Gson to create an object from the JSON response body.
 */
public class WeatherResult {
    protected double temp;

    /**
     * Gets the temperature.
     * @return double temp
     */
    public double getTemperature() {
        return temp;
    }

    /**
     * Sets the temperature.
     * @param temperature
     */
    public void setTemperature(double temperature) {
        this.temp = temperature;
    }


} //WeatherResult
