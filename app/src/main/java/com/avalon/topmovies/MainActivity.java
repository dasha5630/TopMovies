package com.avalon.topmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.avalon.topmovies.model.Movie;

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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);

        new ConnectToDb().execute("https://api.themoviedb.org/3/movie/popular?api_key=d1bd0a1a2ca017e55c2ef6c6c35cf934");
    }

    private class ConnectToDb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(in));
                String s = bf.readLine();
                bf.close();
                return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {
                ArrayList<Movie> movies = new ArrayList<>();
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Movie movie = new Movie(
                            object.getString("original_title"),
                            object.getDouble("popularity"),
                            object.getString("poster_path"),
                            object.getString("overview"),
                            object.getString("release_date"));
                    movies.add(movie);
                    Log.i("add in list", movie.getOriginalTitle());
                }

                MoviesAdapter moviesAdapter = new MoviesAdapter(MainActivity.this, R.layout.movies_list, movies);
                listView.setAdapter(moviesAdapter);
            } catch (JSONException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
        }
    }
}
