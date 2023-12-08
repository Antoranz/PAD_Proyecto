package com.example.pad_proyecto.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JokeApiTask {
    String result;
    private static String API_URL = "https://v2.jokeapi.dev/joke/Any?format=txt&blacklistFlags=racist,sexist,religious,nsfw&lang=";

    public JokeApiTask(){
        String languageCode = Locale.getDefault().getLanguage();
        API_URL += languageCode;
    }
    public void fetchJoke() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            result = fetchDataFromApi();
            if (result != null) {
                Log.d("JokeApiTask", result);
            }
        });
    }
    public String getJoke(){
        return result;
    }
    private String fetchDataFromApi() {
        try {

            URL url = new URL(API_URL);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                urlConnection.setRequestMethod("GET");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();

                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e("JokeApiTask", "Error al obtener el chiste", e);
            return null;
        }
    }
}
