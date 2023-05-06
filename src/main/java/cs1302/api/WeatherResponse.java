package cs1302.api;

/**
 * Gets all of the weather responses.
 */
public class WeatherResponse {
    private WeatherResponse[] weather;

    /**`
     * Gets an array of all the results.
     * @return WeaterResponse[]
     */
    public WeatherResponse[] getWeather() {
        return weather;
    }

    /**
     * Sets weather instance variable.
     * @param weather
     */
    public void setWeather(WeatherResponse[] weather) {
        this.weather = weather;
    }
}
