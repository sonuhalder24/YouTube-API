package com.example.youtubeapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.w3c.dom.Text;

public class MainActivity2 extends YouTubeBaseActivity {
    String title,des,like,view,videoId;
    TextView titleTxt,desTxt,likeTxt,viewTxt;
    ImageView share;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        titleTxt=findViewById(R.id.title);
        desTxt=findViewById(R.id.description);
        likeTxt=findViewById(R.id.likesCount);
        viewTxt=findViewById(R.id.viewsCount);
        share=findViewById(R.id.shareImg);
        youTubePlayerView=findViewById(R.id.you_player);

        title=getIntent().getStringExtra("title");
        des=getIntent().getStringExtra("des");
        view=getIntent().getStringExtra("views");
        like=getIntent().getStringExtra("likes");
        videoId=getIntent().getStringExtra("videoId");

        titleTxt.setText(title);
        desTxt.setText(des);
        viewTxt.setText(view);
        likeTxt.setText(like);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"https://youtu.be/"+videoId);
                startActivity(Intent.createChooser(intent,"share link"));
            }
        });

        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize("AIzaSyCJnoIZlgFgI9xJ63hIJLzEqBaSkPXF6gg",onInitializedListener);
    }
}