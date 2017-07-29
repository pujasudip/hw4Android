package com.sargent.mark.populararticles;

import android.net.Uri;
import android.util.Log;

import com.sargent.mark.populararticles.data.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mark on 6/12/17.
 */

public class NetworkUtils {
    public static final String TAG = "NetworkUtils";

    //this is the website api uri of the news source
    public static final String GITHUB_BASE_URL =
            "https://newsapi.org/v1/articles";;

    //these two constants are the words that matches the uri
    final static String PARAM_QUERY = "source";
    final static String THE_NEXT_WEB = "the-next-web";

    //added these constants after the creation of my account
    final static String PARAM_SORT = "sortBy";
    final static String sortBy = "latest";
    final static String apiKey = "apiKey";
    final static String API = "4eacd48df11f47208cd8e29599f6b0f9";

    public static URL makeURL() {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, THE_NEXT_WEB).appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(apiKey, API)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner input = new Scanner(in);

            input.useDelimiter("\\A");
            String result = (input.hasNext()) ? input.next() : null;
            return result;

        }catch (IOException e){
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    public static ArrayList<Article> parseJSON(String json) throws JSONException {
        ArrayList<Article> string = new ArrayList<>();
        JSONObject main = new JSONObject(json);

        //articles object is obtained
        JSONArray items = main.getJSONArray("articles");

        for(int i = 0; i < items.length(); i++) {
            //iterating through the object to get the string
            JSONObject item = items.getJSONObject(i);
            String title = item.getString("title");
            String description = item.getString("description");
            String published_date = item.getString("publishedAt");

            //this keyword is to get the image url - in the website I'm using they are using 'urlToImage' keyword
            String urlToImage = item.getString("urlToImage");
            String url = item.getString("url");
            Article article = new Article(title,  published_date, description, urlToImage, url);
            string.add(article);
        }

        return string;
    }


}
