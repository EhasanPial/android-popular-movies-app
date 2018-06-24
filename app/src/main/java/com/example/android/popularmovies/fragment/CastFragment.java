package com.example.android.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.CastAdapter;
import com.example.android.popularmovies.DetailActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Cast;
import com.example.android.popularmovies.model.Credits;
import com.example.android.popularmovies.model.MovieDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CastFragment extends Fragment {

    /** Tag for a log message */
    public static final String TAG = CastFragment.class.getSimpleName();

    private List<Cast> mCastList;

    private CastAdapter mCastAdapter;

    private Unbinder mUnbinder;

    @BindView(R.id.rv_cast) RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public CastFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cast, container, false);

        // Bind the view using ButterKnife
        mUnbinder = ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mCastList = new ArrayList<>();

        mCastAdapter = new CastAdapter(mCastList);
        mRecyclerView.setAdapter(mCastAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            MovieDetails movieDetails = args.getParcelable(DetailActivity.EXTRA_MOVIE_DETAILS);
            if (movieDetails != null) {
                Credits credits = movieDetails.getCredits();
                mCastList = credits.getCast();
                credits.setCast(mCastList);
            }
        }
        mCastAdapter.addAll(mCastList);
    }

    /**
     * When binding a fragment in onCreateView, set the views to null in onDestroyView.
     * Butter Knife returns an Unbinder instance when calling bind
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
