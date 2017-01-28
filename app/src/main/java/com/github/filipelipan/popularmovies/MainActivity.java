package com.github.filipelipan.popularmovies;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.filipelipan.popularmovies.fragments.GridMoviesFragment;
import com.github.filipelipan.popularmovies.util.OperationType;

public class MainActivity extends AppCompatActivity {


    public static final String GRID_MOVIE_FRAGMENT = "grid_movie_fragment";

    private GridMoviesFragment mGridMoviesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_main_activity);
        setSupportActionBar(toolbar);

        //if the fragment already exist, i got him with savedFragment, but if don't i create a new one
        mGridMoviesFragment = (GridMoviesFragment) getSupportFragmentManager()
                .findFragmentByTag(GRID_MOVIE_FRAGMENT);
        if(mGridMoviesFragment == null){
            mGridMoviesFragment = new GridMoviesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.ph_main_activity, mGridMoviesFragment, GRID_MOVIE_FRAGMENT);
            fragmentTransaction.commit();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.it_most_popular:
                if(mGridMoviesFragment != null){
                                    mGridMoviesFragment.receiveMovies(OperationType.MOST_POPULAR);
                }
                return true;
            case R.id.it_top_rated:
                if(mGridMoviesFragment != null){
                    mGridMoviesFragment.receiveMovies(OperationType.TOP_RATED);
                }
                return true;
        }
        return false;
    }

}
