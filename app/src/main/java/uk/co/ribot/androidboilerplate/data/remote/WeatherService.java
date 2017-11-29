package uk.co.ribot.androidboilerplate.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uk.co.ribot.androidboilerplate.data.model.Weather;
import uk.co.ribot.androidboilerplate.util.MyGsonTypeAdapterFactory;

/**
 * Created by Oromil on 29.11.2017.
 */

public interface WeatherService {

    String BASE_URL = "http://api.openweathermap.org/";
    String API_KEY = "c4b8f6f5ae94a0c7b188159ddda7c288";
    String UNITS = "metric";

    @GET("data/2.5/weather?appid="+API_KEY+ "&units=" + UNITS)
    Observable<Weather> getWeatrher(@Query("lat") float latitude, @Query("lon") float longitude, @Query("lang") String language);

    @GET("data/2.5/weather?appid="+API_KEY+ "&units=" + UNITS)
    Observable<Weather> getWeatrher(@Query("lat") double latitude, @Query("lon") double longitude);

    class Creator {
        public static WeatherService createWeatherServise() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WeatherService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(WeatherService.class);
        }
    }
}
