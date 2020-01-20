package com.avalon.topmovies.model;

import java.io.Serializable;

public class Movie implements Serializable {

    private double popularity;
    private String posterPath;
    private String originalTitle;
    private String overview;
    private String releaseDate;

    public Movie(){};

    public Movie(String originalTitle, double popularity, String posterPath, String overview, String releaseDate) {
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}