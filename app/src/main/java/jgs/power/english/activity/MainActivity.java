package jgs.power.english.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;

import jgs.power.english.R;
import jgs.power.english.adapter.VideoAdapter;
import jgs.power.english.model.Video;

public class MainActivity extends AppCompatActivity implements
        VideoAdapter.OnItemSelectedListener{

    private static final String TAG = "Main Activity";
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private Dialog errorDialog;

    private VideoAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static final int REQ_START_STANDALONE_PLAYER = 101;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private boolean canHideStatusBar = false;

    private Context mContext;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        mContext = MainActivity.this;

        initView();
        
        initFirestore();
        
        initRecyclerView();

    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();

        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAdapter != null) {
            mAdapter.stopListening();
        }

    }


    private void initView() {
        Log.d(TAG, "initView");
        mRecyclerView = (RecyclerView)findViewById(R.id.main_recycler);
        
    }

    private void initRecyclerView() {

        Log.d(TAG, "initRecyclerView");

        if (mQuery == null) {
            Log.w(TAG, "mQuery No Query, not initializing RecyclerView");
        }

        mAdapter = new VideoAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    Log.d(TAG, "getItemCount() == 0");
                    mRecyclerView.setVisibility(View.GONE);
                    //mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "getItemCount() != 0");
                    mRecyclerView.setVisibility(View.VISIBLE);
                    //mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(MainActivity.this.findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);


    }
    

    @Override
    public void onItemSelected(DocumentSnapshot item) {

        Video video = item.toObject(Video.class);

         // Launch standalone YoutTube player
         Intent intent = null;
         intent = YouTubeStandalonePlayer.createVideoIntent(MainActivity.this, getString(R.string.youtube_key), video.getUrl(), 0, true, false);
         if (intent != null) {
             if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                 if (canResolveIntent(intent)) {
                     canHideStatusBar = true;
                     startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                 } else {
                     // Could not resolve the intent - must need to install or update the YouTube API service.
                     YouTubeInitializationResult
                             .SERVICE_MISSING
                             .getErrorDialog(MainActivity.this, REQ_RESOLVE_SERVICE_MISSING).show();
                 }
             }
         }

        
    }

//    @Override
//    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
//        //youTubeThumbnailLoader.setVideo(urlYouTube);
//        //youTubeThumbnailLoader.setPlaylist();
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//
//        if (youTubeInitializationResult.isUserRecoverableError()) {
//            if (errorDialog == null || !errorDialog.isShowing()) {
//                errorDialog = youTubeInitializationResult.getErrorDialog(MainActivity.this, RECOVERY_DIALOG_REQUEST);
//                errorDialog.show();
//            }
//        } else {
//            String errorMessage = String.format("에러", youTubeInitializationResult.toString());
//            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void setYoutubeThumnail(YouTubeThumbnailView thumbnailView, final String urlYouTube){
//
//        // 만약 firebase에서 값을 가져오지못한다면...
//
//        if (!"".equals(urlYouTube)) {
//
//            thumbnailView.initialize(getString(R.string.youtube_key), this);
//            thumbnailView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View arg0) {
//                    // Launch standalone YoutTube player
//                    Intent intent = null;
//                    intent = YouTubeStandalonePlayer.createVideoIntent(MainActivity.this, getString(R.string.youtube_key), urlYouTube, 0, true, false);
//                    if (intent != null) {
////                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
//                        if (canResolveIntent(intent)) {
//                            canHideStatusBar = true;
//                            startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
//                        } else {
//                            // Could not resolve the intent - must need to install or update the YouTube API service.
//                            YouTubeInitializationResult
//                                    .SERVICE_MISSING
//                                    .getErrorDialog(MainActivity.this, REQ_RESOLVE_SERVICE_MISSING).show();
//                        }
//                    }
//                }});
//        }
//    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = MainActivity.this.getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }


    private void initFirestore() {
        // TODO(developer): Implement
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        mFirestore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        mFirestore.setFirestoreSettings(settings);

        // TODO: 쿼리문이나 등등 넣기

        mQuery = mFirestore.collection("youtube").orderBy("order", Query.Direction.DESCENDING);

    }
}
