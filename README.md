# YoutubeThumnailRecyclerView
⭐️ Star us on GitHub — it helps!

## Motivation
This project was saved as a template for Android app creation to perform basic functions.

## Features
This project is a project composed of recyclerview using YouTube API and thumbnail address.

## Build status
- Android Studio 3.4.1  
- Build #AI-183.6156.11.34.5522156, built on May 2, 2019  
- JRE: 1.8.0_152-release-1343-b01 amd64  
- JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o  
- Windows 10 10.0-  

## Screenshots
"![](/이미지 5.png)"

## Installation
If you use the GitHub website
1. Open in Desktop or Download ZIP about this project

If you use git bash
1. git clone https://github.com/jogilsang/android-youtubeThumnailRecyclerView.git

## How to use?
To open the project in Android Studio:  

1. Launch Android Studio   
2. select File-New-Import Project from the top menu bar  
3. Just run  

if it doesn't work, check build status at the top  

4. Check your manifes file, and add the contents below.   
   Implement getPermission () separately, or check the app's permissions manually after build  
```xml
    <uses-permission android:name="android.permission.INTERNET"/>
```

5. 
You can play the video by clicking directly through the YouTube API, but you can play the video based on the thumbnail view.  
If you created a YouTube API key in Google Developer and implemented YouTube related implemenstation in lib  
You can do the following:  

The core code is shown below. 
```xml
        <com.google.android.youtube.player.YouTubeThumbnailView
            android:layout_below="@id/item_title"
            android:id="@+id/item_video"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:scaleType="fitXY"
            android:adjustViewBounds="true"
            />
```

Declare the variable urlYoutube above to put the YouTube ID that will be shown in the thumbnail and thumbnail views.  
The YouTube ID is the _P6_9uwox90 value at www.youtube.com/watch?v=_P6_9uwox90 at the end of the YouTube address.  
You can change the variable name as you wish  
```java
public class MainActivity extends BaseActivity implements YouTubeThumbnailView.OnInitializedListener {

     public String urlYouTube =""; 

     public static final int REQ_START_STANDALONE_PLAYER = 101;
     public static final int REQ_RESOLVE_SERVICE_MISSING = 2;
     public static final int RECOVERY_DIALOG_REQUEST = 1;   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        YouTubeThumbnailView thumbnailView;       
        thumbnailView = (YouTubeThumbnailView)findViewById(R.id.item_video);

        setYoutubeThumnail(thumbnailView, videoID);

        setListener();

}



}
```
If you implement the interface, you must implement the following onInitializationSuccess and onInitializationFailure methods.  
```java
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

```
The onInitializationSuccess and onInitializationFailure methods above work by initializing the thumbnailView below.  
The code below works if the YouTube ID is valid. To be exact, it works if the String is not empty.  
```java
        public void setYoutubeThumnail(YouTubeThumbnailView thumbnailView, final String urlYouTube){

            // 만약 firebase에서 값을 가져오지못한다면...

            if (!"".equals(urlYouTube)) {

                thumbnailView.initialize(getString(R.string.youtube_key), this);

            }
        }
```

Click the thumnailView to set up the listener so that the video appears.  
Full screen becomes full screen.  
```java
public void setListener() {
            thumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, urlYouTube);

                    // Launch standalone YoutTube player
                    Intent intent = null;
                    intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), getString(R.string.youtube_key), urlYouTube, 0, true, false);
                    if (intent != null) {
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            if (canResolveIntent(intent)) {
                                canHideStatusBar = true;
                                startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                            } else {
                                // Could not resolve the intent - must need to install or update the YouTube API service.
                                YouTubeInitializationResult
                                        .SERVICE_MISSING
                                        .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
                            }
                        }
                    }

                }
            });
}

```

## Contribute
If you want to contribute to this project, it would be nice to add the following:
- UI / UX

## Credits
@jogilsang

## Reference
- if you want make YoutubeThumnailView app ???  
https://blog.naver.com/jogilsang/221511604329  

## Cotact
mail :
jogilsang@naver.com

kakao :
jogilsang

Instagram :
<https://www.instagram.com/jogilsang3>

Youtube :
<https://www.youtube.com/user/mrjogilsang>

more information : 
<https://blog.naver.com/jogilsang>

## Donate
Bitcoin : 351pQjDFFWW61HHKcSFQHcEMNYy4rP91ex

Etherium : 0xb2470124ac43a955c36d7a21e208fae5d0d5d2e0

## License
The MIT License © 2019 jogilsang
```
The MIT License

Copyright (c) 2019 jogilsang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
