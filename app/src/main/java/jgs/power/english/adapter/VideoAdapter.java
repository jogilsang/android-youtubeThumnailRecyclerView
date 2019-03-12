/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package jgs.power.english.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.Timestamp;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import jgs.power.english.R;
import jgs.power.english.activity.MainActivity;
import jgs.power.english.model.Video;


/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class VideoAdapter extends FirestoreAdapter<VideoAdapter.ViewHolder>  {

    public interface OnItemSelectedListener {
        void onItemSelected(DocumentSnapshot item);
    }


    private OnItemSelectedListener mListener;

    public VideoAdapter(Query query, OnItemSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements YouTubeThumbnailView.OnInitializedListener{

        private static final String TAG = "ViewHolder";
        YouTubeThumbnailView thumbnailView;
        public String urlYouTube ="";

        public ViewHolder(View itemView) {
            super(itemView);

            initView(itemView);

        }

        @Override
        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
            youTubeThumbnailLoader.setVideo(urlYouTube);

            youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    Log.d(TAG, "onThumbnailLoaded");
                    youTubeThumbnailLoader.release();
                }

                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                    Log.d(TAG, "onThumbnailfail");
                }
            });
        }

        @Override
        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            if (youTubeInitializationResult.isUserRecoverableError()) {
//                if (errorDialog == null || !errorDialog.isShowing()) {
//                    errorDialog = youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST);
//                    errorDialog.show();
//                }
                Log.d(TAG, "error Dialog");
            } else {
                String errorMessage = String.format("에러", youTubeInitializationResult.toString());
                Log.d(TAG,errorMessage);
            }

        }


        public void initView(View v){

            thumbnailView = (YouTubeThumbnailView)v.findViewById(R.id.item_video);

        }

        public void setYoutubeThumnail(YouTubeThumbnailView thumbnailView, final String urlYouTube){

            // 만약 firebase에서 값을 가져오지못한다면...

            if (!"".equals(urlYouTube)) {

                thumbnailView.initialize("AIzaSyCxg5_n9_Xz4rs8hwLyLvgmcwHvuApnnN4", this);

            }
        }


        public void bind(final DocumentSnapshot snapshot,
                         final OnItemSelectedListener listener) {

            Video video = snapshot.toObject(Video.class);
            Log.d(TAG, video.toString());

            setYoutubeThumnail(thumbnailView , video.getUrl());

            // TODO : Glide
            // 중간해상도 썸네일(640 x 480)
            String thumbnail_form = "https://img.youtube.com/vi/";
            String resolution = "/sddefault.jpg";
            String thumnail_url = thumbnail_form + video.getUrl() + resolution;

//            urlYouTube = thumnail_url;
            urlYouTube = video.getUrl();

            // Click listener
            // text를 넘기자
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemSelected(snapshot);
                    }
                }
            });
        }

    }
}
