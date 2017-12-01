package uk.co.ribot.androidboilerplate.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oromil on 29.11.2017.
 */

public class Weather {

    public static final String TEMP = "temp";
    public static final String DESCRIPTION = "description";
    public static final String CITY = "city";

    @SerializedName("weather")
    @Expose
    public List<WeatherType> weather;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("name")
    @Expose
    public String name;

    public Map<String, String> getWeather() {
        Map<String, String> weatherMap = new HashMap<>();
        weatherMap.put(TEMP, main.temp.toString());
        weatherMap.put(DESCRIPTION, getWeatherDescription());
        weatherMap.put(CITY, name);
        return weatherMap;
    }

    private String getWeatherDescription() {
        String description = "";
        for (int i = 0; i < weather.size(); i++) {
            if (i != weather.size() - 1)
                description += weather.get(i).description + ", ";
            else
                description += weather.get(i).description;
        }
        return description;
    }

    public class Main {

        @SerializedName("temp")
        @Expose
        public Double temp;

    }

    public class WeatherType {

        @SerializedName("main")
        @Expose
        public String main;
        @SerializedName("description")
        @Expose
        public String description;

    }
}
