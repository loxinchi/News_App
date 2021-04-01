package com.example.android.newsapp.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.example.android.newsapp.News;

/**
 * Helper methods related to requesting and receiving news data from The Guardian.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the The Guardian dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        // Test for slow AsyncLoader delay 2secs
        String jsonResponse = null;
        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> gardianNews = extractFeatureFromJson(jsonResponse);
        return gardianNews;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(Constants.READ_TIMEOUT);
            urlConnection.setConnectTimeout(Constants.CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(Constants.REQUEST_METHOD_GET);
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == Constants.SUCCESS_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the The Gardian news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            // Extract the JSONObject associated with the key called "response"
            JSONObject responseJsonObject = baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE);
            // Extract the JSONArray associated with the key called "results"
            JSONArray newsArray = responseJsonObject.getJSONArray(Constants.JSON_KEY_RESULTS);
            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                // Extract the value for the key called "webTitle"
                String webTitle = currentNews.getString(Constants.JSON_KEY_WEB_TITLE);
                // Extract the value for the key called "webUrl"
                String url = currentNews.getString(Constants.JSON_KEY_WEB_URL);
                // Extract the value for the key called "sectionName"
                String sectionName = currentNews.getString(Constants.JSON_KEY_SECTION_NAME);
                // Extract the value for the key called "webPublicationDate"
                String webPublicationDate = currentNews.getString(Constants.JSON_KEY_WEB_PUBLICATION_DATE);
                JSONObject fields = currentNews.getJSONObject(Constants.JSON_KEY_FIELDS);
                // Extract the value for the key called "byline"
                String author = fields.optString(Constants.JSON_KEY_BYLINE);
//                // Extract the value for the key called "webPublicationDate"
//                String webPublicationDate = fields.getString(Constants.JSON_KEY_WEB_PUBLICATION_DATE);
                // Extract the value for the key called "thumbnail"
                String thumbnail = fields.getString(Constants.JSON_KEY_THUMBNAIL);
                // Create a new {@link Earthquake} object
                News news = new News(webTitle, sectionName, author, webPublicationDate, url, thumbnail);
                // Add the new {@link Earthquake} to the list of earthquakes.
                newsList.add(news);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakes
        return newsList;
    }
}