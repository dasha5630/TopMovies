package com.avalon.topmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ListView listView;
    private Calendar calendar;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        datePickerDialog = new DatePickerDialog(this, this, 2020,1, 1);
        timePickerDialog = new TimePickerDialog(this,this,12,12,true);

        listView = findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        new ConnectToDb().execute("https://api.themoviedb.org/3/movie/popular?api_key=d1bd0a1a2ca017e55c2ef6c6c35cf934");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        Intent intent = new Intent(this, DatePicker.class);
//        intent.putExtra("MOVIE", (Movie)parent.getItemAtPosition(position));
//        startActivity(intent);
        currentMovie = (Movie)parent.getItemAtPosition(position);
        calendar = Calendar.getInstance();
        datePickerDialog.show();

    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("notificationId", 1);
        intent.putExtra("todo", currentMovie.getOriginalTitle());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        long alarmStartTime = calendar.getTimeInMillis();

        // Set alarm.
        // set(type, milliseconds, intent)
        if(Build.VERSION.SDK_INT >= 19){
            alarm.setExact(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
        } else {
            alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
        }
/*        Intent intent = new Intent(this, DatePicker.class);
        intent.putExtra("todo", currentMovie.getOriginalTitle());
        startActivity(intent);*/

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
                            object.getDouble("vote_average"),
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
