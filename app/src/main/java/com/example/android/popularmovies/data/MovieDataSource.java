/*
 *  Copyright 2018 Soojeong Shin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.popularmovies.data;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieResponse;
import com.example.android.popularmovies.utilities.Constant;
import com.example.android.popularmovies.utilities.Controller;
import com.example.android.popularmovies.utilities.TheMovieApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The MovieDataSource is the base class for loading snapshots of movie data into a given PagedList,
 * which is backed by the network. Since the TMDb API includes a key with each page load, extend
 * from PageKeyedDataSource.
 *
 * Reference: @see "https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe"
 * "https://www.youtube.com/watch?v=Ts-uxYiBEQ8" "https://www.youtube.com/watch?v=QVMqCRs0BNA"
 */
public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    /** Tag for logging */
    private static final String TAG = MovieDataSource.class.getSimpleName();

    /** Member variable for TheMovieApi interface */
    private TheMovieApi mTheMovieApi;

    /** String for the sort order of the movies */
    private String mSortCriteria;

    public MovieDataSource(String sortCriteria) {
        mTheMovieApi = Controller.getClient().create(TheMovieApi.class);
        mSortCriteria = sortCriteria;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        mTheMovieApi.getMovies(mSortCriteria, Constant.API_KEY, Constant.LANGUAGE, 1)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body().getMovieResults(), 1, 2);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, Movie> callback) {

        final int currentPage = params.key;

        mTheMovieApi.getMovies(mSortCriteria, Constant.API_KEY, Constant.LANGUAGE, currentPage)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            int nextKey = currentPage + 1;
                            callback.onResult(response.body().getMovieResults(), nextKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });

    }
}
