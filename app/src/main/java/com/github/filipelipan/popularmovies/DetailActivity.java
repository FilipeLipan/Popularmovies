package com.github.filipelipan.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.filipelipan.popularmovies.adapter.TrailerAdapter;
import com.github.filipelipan.popularmovies.fragments.GridMoviesFragment;
import com.github.filipelipan.popularmovies.model.Movie;
import com.github.filipelipan.popularmovies.model.Result;
import com.github.filipelipan.popularmovies.model.Review;
import com.github.filipelipan.popularmovies.model.Reviews;
import com.github.filipelipan.popularmovies.model.Trailer;
import com.github.filipelipan.popularmovies.moviedb.MoviesDbService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;
    private Result mResult;

    private TrailerAdapter mTrailerAdapter;

    public static final String TAG = DetailActivity.class.getSimpleName();

    //@BindView(R.id.tv_title) TextView mTextViewTitle;
    @BindView(R.id.tv_year) TextView mTextViewDate;
    @BindView(R.id.tv_overview) TextView mTextViewOverView;
    @BindView(R.id.tv_vote_average) TextView mTextViewVoteAverage;
    @BindView(R.id.iv_poster) ImageView mImageViewPoster;
    @BindView(R.id.rv_detail_trailers) RecyclerView mRecyclerView;
    @BindView(R.id.ct_detail_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.image) ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mTrailers = new ArrayList<>();
        mReviews = new ArrayList<>();

        Intent intent = getIntent();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(intent.hasExtra(Movie.PASSING_MOVIE)){
            mMovie = intent.getParcelableExtra(Movie.PASSING_MOVIE);

            //mTextViewTitle.setText(mMovie.getOriginalTitle());
            mTextViewDate.setText(mMovie.getYear());
            mTextViewOverView.setText(mMovie.getOverview());
            mTextViewVoteAverage.setText(mMovie.getVoteAverage() + "/10");

            //CollapsingToolbar
            mCollapsingToolbarLayout.setTitle(mMovie.getOriginalTitle());

            String imgUrl = MoviesDbService.BASE_POSTER_URL_HD + mMovie.getPosterPath();
            Picasso.with(this)
                    .load(imgUrl)
                    .placeholder(R.drawable.load)
                    .error(R.drawable.error)
                    .into(mImageView);

            Picasso.with(this)
                    .load(imgUrl)
                    .placeholder(R.drawable.load)
                    .error(R.drawable.error)
                    .into(mImageViewPoster);


            mTrailerAdapter = new TrailerAdapter(mTrailers, this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mTrailerAdapter);

            receiveTraillers(mMovie.id);
            receiveReviews(mMovie.id);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    //if option equals 1 return the top rated
    //else the popular movies
    private void receiveTraillers(int id) {
        if (GridMoviesFragment.isConnectionAvailable(this)) {
            //TODO: fix this
            //toggleProgressBar();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MoviesDbService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MoviesDbService service = retrofit.create(MoviesDbService.class);
            Call<Result> makeTrailersRequest;
            makeTrailersRequest = service.getTrailers(id,MoviesDbService.API_KEY);

            makeTrailersRequest.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (!response.isSuccessful() || response == null) {
                        Toast.makeText(getBaseContext(), R.string.coudntRecoverMovies,
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, getString(R.string.retrofitError));
                    } else {
                        if(response.body() != null) {
                            Result result = response.body();
                            mTrailers = result.youtube;
                            //TODO: fix this
                            mTrailerAdapter.setTrailers(mTrailers);
                            mTrailerAdapter.notifyDataSetChanged();
                            //mGridAdapter.setMovies(mMovies);
                            //mGridAdapter.notifyDataSetChanged();
                        }
                    }
                    //TODO: fix this
                    //toggleProgressBar();
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Toast.makeText(getBaseContext(), R.string.somethingWentWrong,
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Something went wrong on retrofit, coming from retrofit on failure");
                    //TODO: fix this
                    //toggleProgressBar();
                }
            });
        }else {
            Toast.makeText(getBaseContext(), R.string.noConnectionAvailable, Toast.LENGTH_SHORT).show();
        }
    }

    private void receiveReviews(int id) {
        if (GridMoviesFragment.isConnectionAvailable(this)) {
            //TODO: fix this
            //toggleProgressBar();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MoviesDbService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MoviesDbService service = retrofit.create(MoviesDbService.class);
            Call<Reviews> makeTrailersRequest;
            makeTrailersRequest = service.getReviews(id,MoviesDbService.API_KEY);

            makeTrailersRequest.enqueue(new Callback<Reviews>() {
                @Override
                public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                    if (!response.isSuccessful() || response == null) {
                        Toast.makeText(getBaseContext(), R.string.coudntRecoverMovies,
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, getString(R.string.retrofitError));
                    } else {
                        if(response.body() != null) {
                            Reviews reviews = response.body();
                            mReviews = reviews.results;
                            //TODO: fix this
                            //mGridAdapter.setMovies(mMovies);
                            //mGridAdapter.notifyDataSetChanged();
                        }
                    }
                    //TODO: fix this
                    //toggleProgressBar();
                }

                @Override
                public void onFailure(Call<Reviews> call, Throwable t) {
                    Toast.makeText(getBaseContext(), R.string.somethingWentWrong,
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Something went wrong on retrofit, coming from retrofit on failure");
                    //TODO: fix this
                    //toggleProgressBar();
                }
            });
        }
    }

}