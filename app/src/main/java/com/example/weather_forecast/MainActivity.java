package com.example.weather_forecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
   EditText etCity, etCountry;
   TextView tvResults;
   private  final String url="http://api.openweathermap.org/data/2.5/weather";
   private  final String appid = " ";
   DecimalFormat df= new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity=findViewById(R.id.etCity);
        etCountry=findViewById(R.id.etCountry);
        tvResults =findViewById(R.id.tvResults);
    }

    public void getWeatherDetails(View view) {
        String tempurl= "";
        String city =etCity.getText().toString().trim();
        String country =etCountry.getText().toString().trim();
        if (city.equals("")){
            tvResults.setText("city field cannot be empty");
        }else{
            if (!country.equals("")){
                tempurl= url + "?q="+ city + "," + country + "&appid=" + appid;
            }else{
                tempurl= url + "?q="+ city  + "&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.d("response",response);
                 String output= "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather =jsonArray.getJSONObject(0);
                        String description =jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp =jsonObjectMain.getDouble("temp")-273.15;
                        double feelsLike=jsonObjectMain.getDouble("feels_like")-273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity =jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind= jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds =jsonResponse.getJSONObject("clouds");
                        String Clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
//                        tvResults.setTextColor(Color.rgb(68,134,199));
                        output +="Current weather of " + cityName + "(" +countryName +")"
                                +"\n\n Temp: " + df.format(temp) + "°C"
                                +"\n\n FeelsLike: " + df.format(feelsLike) + "°C"
                                +"\n\n Humidity: " + humidity + "%"
                                +"\n\n Description: " + description
                                +"\n\n Wind Speed: " + wind + "m/s "
                                +"\n\n Cloudiness: " + Clouds + "%"
                                +"\n\n Pressure: " + pressure + "hPa";
                        tvResults.setText(output);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                    error.printStackTrace();

                }
            });
            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }
    }
}