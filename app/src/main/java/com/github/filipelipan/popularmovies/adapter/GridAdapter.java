package com.github.filipelipan.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.github.filipelipan.popularmovies.R;
import com.github.filipelipan.popularmovies.model.Movie;
import com.github.filipelipan.popularmovies.moviedb.MoviesDbService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private ArrayList<Movie> mMovies;
    private Context mContext;

    public GridAdapter(ArrayList<Movie> movies, Context context) {
        this.mMovies = movies;
        this.mContext = context;
    }

    public void setMovies(ArrayList<Movie> movies) {
        mMovies = movies;
    }

    @Override
    public int getCount() {
        if(mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mMovies != null)
            return mMovies.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item,null);
            holder = new ViewHolder();

            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_list_item_poster);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = mMovies.get(position);

        String imgUrl = MoviesDbService.BASE_POSTER_URL + movie.getPosterPath();
        Picasso.with(parent.getContext())
                .load(imgUrl)
                .placeholder(R.drawable.load)
                .error(R.drawable.error)
                .into(holder.mImageView);

        return convertView;
    }

    public static class ViewHolder{
        ImageView mImageView;
    }
}
