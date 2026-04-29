package com.doritech.CustomerService.Service;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
@Service
public class DistanceService {

    private static final String API_KEY = "AIzaSyDMKndzFcfiysofJH9zTVJtOCeIH70nKcc";

    private final Map<String, Double> cache = new HashMap<>();

    public double getDistanceInKm(String origin, String destination) {

        String key = origin + "-" + destination;

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        try {
            String urlStr = "https://maps.googleapis.com/maps/api/distancematrix/json"
                    + "?origins=" + URLEncoder.encode(origin, "UTF-8")
                    + "&destinations=" + URLEncoder.encode(destination, "UTF-8")
                    + "&key=" + API_KEY;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();

            JSONObject json = new JSONObject(response.toString());

            JSONArray rows = json.getJSONArray("rows");
            JSONObject element = rows.getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0);

            if (!"OK".equals(element.getString("status"))) {
                return 0;
            }

            double meters = element.getJSONObject("distance").getDouble("value");
            double km = meters / 1000.0;

            cache.put(key, km);

            return km;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}