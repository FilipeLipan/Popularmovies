package com.github.filipelipan.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.filipelipan.popularmovies.DetailActivity;
import com.github.filipelipan.popularmovies.MainActivity;
import com.github.filipelipan.popularmovies.R;
import com.github.filipelipan.popularmovies.adapter.GridAdapter;
import com.github.filipelipan.popularmovies.model.Movie;
import com.github.filipelipan.popularmovies.model.Result;
import com.github.filipelipan.popularmovies.moviedb.MoviesDbService;
import com.github.filipelipan.popularmovies.util.OperationType;
import com.github.filipelipan.popularmovies.util.SharedPreferenceHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lispa on 27/01/2017.
 */

public class GridMoviesFragment extends Fragment{

    private static final String TAG = GridMoviesFragment.class.getSimpleName();

    @BindView(R.id.gv_main) GridView mGridView;
    @BindView(R.id.pb_main) ProgressBar mMainProgressBar;
    @BindView(R.id.tv_empty) TextView mEmptyTextView;

    private GridAdapter mGridAdapter;

    private ArrayList<Movie> mMovies;

    private SharedPreferenceHelper mSharedPreferenceHelper;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.grid_movies_fragment, container, false);
        ButterKnife.bind(this,view);


        mSharedPreferenceHelper = new SharedPreferenceHelper(mContext);
        mMovies = mSharedPreferenceHelper.getStoragePreference();

        mGridAdapter = new GridAdapter(mMovies ,mContext);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setEmptyView(mEmptyTextView);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie movie = mMovies.get(position);
                Intent intent =  new Intent(mContext, DetailActivity.class);
                intent.putExtra(Movie.PASSING_MOVIE ,movie);
                mContext.startActivity(intent);
            }
        });

        if(mMovies.size() <= 0){
            receiveMovies(OperationType.TOP_RATED);
        }

        return view;
    }

    //if option equals 1 return the top rated
    //else the popular movies
    public void receiveMovies(int option){
        if(isConnectionAvailable(mContext)) {
            toggleProgressBar();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MoviesDbService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MoviesDbService service = retrofit.create(MoviesDbService.class);
            Call<Result> makeMovieRequest;
            if (option == OperationType.TOP_RATED) {
                makeMovieRequest = service.getTopRatedMovies(MoviesDbService.API_KEY);
            } else {
                makeMovieRequest = service.getPopularMovies(MoviesDbService.API_KEY);
            }

            makeMovieRequest.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (!response.isSuccessful() || response == null) {
                        Toast.makeText(mContext, R.string.coudntRecoverMovies,
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, getString(R.string.retrofitError));
                    } else {
                        if(response.body() != null) {
                            Result result = response.body();
                            mMovies = result.results;
                            mGridAdapter.setMovies(mMovies);
                            mGridAdapter.notifyDataSetChanged();
                        }
                    }
                    toggleProgressBar();
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Toast.makeText(mContext, R.string.somethingWentWrong,
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Something went wrong on retrofit, coming from retrofit on failure");
                    toggleProgressBar();
                }
            });
        }else {
            Toast.makeText(mContext, R.string.noConnectionAvailable, Toast.LENGTH_SHORT).show();
        }
    }


    //verify connection
    public static boolean isConnectionAvailable(Context c){
        ConnectivityManager manager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    //toggle progressBar visibility
    public void toggleProgressBar(){
        if(mMainProgressBar.getVisibility() == View.VISIBLE){
            mMainProgressBar.setVisibility(View.INVISIBLE);
        }else {
            mMainProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSharedPreferenceHelper.setSharedPreferences(mMovies);
    }
}
