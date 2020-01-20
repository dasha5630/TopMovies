package com.avalon.topmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avalon.topmovies.model.Movie;
import com.bumptech.glide.Glide;

import java.util.List;

public class MoviesAdapter extends ArrayAdapter{

    private Context context;
    private int resource;
    private List<Movie> movies;

    public MoviesAdapter(@NonNull Context context, int resource, @NonNull List<Movie> movies) {
        super(context, resource, movies);
        this.context = context;
        this.resource = resource;
        this.movies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);

        Movie movie = movies.get(position);
        TextView movieName = (TextView) view.findViewById(R.id.textView);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);

        movieName.setText(movie.getOriginalTitle());

        Glide.with(context).load("https://image.tmdb.org/t/p/w500/" + movie.getPosterPath()).into(image);
        return  view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
}
