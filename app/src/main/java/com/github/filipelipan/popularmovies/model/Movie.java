package com.github.filipelipan.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lispa on 08/01/2017.
 */

public class Movie implements Parcelable {

    public static final String PASSING_MOVIE = "passing_movie";

    public int id;
    public String original_title;
    public String poster_path;
    public String overview;
    public double vote_average;
    public String release_date;


    public String getOriginalTitle() {
        return original_title;
    }

    public String getPosterPath() {
        return poster_path;
    }


    public String getOverview() {
        return overview;
    }


    public double getVoteAverage() {
        return vote_average;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getYear(){
        String[] stringArray = new String[3];
        int i = 0;
        for(String text : release_date.split("-")){
            stringArray[i] = text;
            i++;
        }

        return stringArray[0];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.release_date);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.original_title = in.readString();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
