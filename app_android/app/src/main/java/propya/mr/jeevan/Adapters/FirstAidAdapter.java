package propya.mr.jeevan.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import propya.mr.jeevan.Activities.ConfirmedInfo;
import propya.mr.jeevan.R;

public class FirstAidAdapter extends RecyclerView.Adapter<FirstAidAdapter.FirstAidViewHolder> {
    private String[] first_aid_urls;
    private String first_aid_title;
    private String first_aid_text;
    private Context parentActivityContext;

    public FirstAidAdapter(String first_aid_title, String first_aid_text,String [] first_aid_urls,Context parentActivityContext)
    {
        this.first_aid_title=first_aid_title;
        this.first_aid_urls=first_aid_urls;
        this.first_aid_text=first_aid_text;
        this.parentActivityContext=parentActivityContext;
    }
    @NonNull
    @Override
    public FirstAidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.first_aid_viewholder, parent, false);
        return new FirstAidViewHolder(rl);
    }

    @Override
    public void onBindViewHolder(@NonNull FirstAidViewHolder holder, int position) {

        holder.first_aid_title.setText(first_aid_title);
        if(position==0)
        {
            holder.first_aid_text.setVisibility(View.VISIBLE);
            holder.first_aid_video_playerView.setVisibility(View.GONE);
            holder.first_aid_text.setText(first_aid_text);

        }
        else if (position==1 && first_aid_urls!=null)
        {
               //TODO PLAYER STUFF
            holder.first_aid_text.setVisibility(View.GONE);
            holder.player = ExoPlayerFactory.newSimpleInstance(parentActivityContext, new DefaultTrackSelector());
            PlayerView playerView = holder.first_aid_video_playerView;
            playerView.setShutterBackgroundColor(Color.BLACK);
            playerView.setUseController(true);
            playerView.setPlayer(holder.player);
            holder.first_aid_video_playerView.setVisibility(View.VISIBLE);



            try {

                Log.i("EventVid", "VIDEO LINK :"+ first_aid_urls[0]);
                MediaSource videoSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("Jeevan")).createMediaSource(Uri.parse(first_aid_urls[0]));
                holder.player.prepare(videoSource);
                holder.player.setPlayWhenReady(true);
                holder.player.addListener(new Player.EventListener() {
                    @Override
                    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                        Log.i("EventVid", "" + holder.player.getContentPosition());
                    }

                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                    }

                    @Override
                    public void onLoadingChanged(boolean isLoading) {

                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


                    }

                    @Override
                    public void onRepeatModeChanged(int repeatMode) {

                    }

                    @Override
                    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {
                        Log.i("FirstAidVideo","Player error"+error.toString());
                    }

                    @Override
                    public void onPositionDiscontinuity(int reason) {

                    }

                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                    }

                    @Override
                    public void onSeekProcessed() {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(position==1)
        {
            holder.first_aid_text.setVisibility(View.VISIBLE);
            holder.first_aid_video_playerView.setVisibility(View.GONE);
            holder.first_aid_text.setText("No video available");
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    static class FirstAidViewHolder  extends RecyclerView.ViewHolder {

        private TextView first_aid_title;
        private TextView first_aid_text;
        private PlayerView first_aid_video_playerView;
        private SimpleExoPlayer player;

        FirstAidViewHolder(@NonNull View itemView) {
            super(itemView);

            first_aid_text = itemView.findViewById(R.id.first_aid_text_view);
            first_aid_title = itemView.findViewById(R.id.first_aid_title);
            first_aid_video_playerView = itemView.findViewById(R.id.first_aid_player_view);

        }
    }
}
