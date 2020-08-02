package propya.mr.jeevan.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.BindView;
import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.R;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

import io.agora.rtc.video.VideoEncoderConfiguration;

public class TelemedicineVideo extends ActivityHelper {//Bind all layout views
    @BindView(R.id.activity_video_chat_view)
    RelativeLayout rootview;
    @BindView(R.id.remote_video_view_container) RelativeLayout remoteViewContainer;
    @BindView(R.id.icon_padding) RelativeLayout iconpadding;
    @BindView(R.id.local_video_view_container)
    FrameLayout localVideoviewcontainer;
    @BindView(R.id.control_panel) RelativeLayout controlPanel;
    @BindView(R.id.btn_call)
    ImageView callButton;
    @BindView(R.id.btn_switch_camera)
    ImageView switchCameraButton;
    @BindView(R.id.btn_mute)
    ImageView muteButton;

//To check if all permisiions granted
    private String teleToken;
    private String emergencyid;
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    // Java
    private RtcEngine mRtcEngine;
    private boolean mMuted=false;
    private boolean mCallEnd=false;



    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the onJoinChannelSuccess callback.
        // This callback occurs when the local user successfully joins the channel.
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                }

            }
            );

            TelemedicineVideo.super.stopProgress();
        }

        @Override
        // Listen for the onFirstRemoteVideoDecoded callback.
        // This callback occurs when the first video frame of a remote user is received and decoded after the remote user successfully joins the channel.
        // You can call the setupRemoteVideo method in this callback to set up the remote video view.
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        // Listen for the onUserOffline callback.
        // This callback occurs when the remote user leaves the channel or drops offline.
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","User offline, uid: " + (uid & 0xFFFFFFFFL));
//                    onRemoteUserLeft();
                }
            });
        }




    };
    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        if(mMuted)
        {
            muteButton.setImageResource(R.drawable.ic_mic_off_white_24dp);
        }
        else
        {
            muteButton.setImageResource(R.drawable.ic_mic_white_24dp);
        }
        mRtcEngine.muteLocalAudioStream(mMuted);
    }
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }
// Initialize the RtcEngine object.
    private void initializeEngine() {

        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
            setupLocalVideo();

        } catch (Exception e) {
            Log.e("Telemedicine", Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }
    private void setupLocalVideo() {

        // Enable the video module.
        mRtcEngine.enableVideo();

        // Create a SurfaceView object.
//        private FrameLayout mLocalContainer;
        SurfaceView mLocalView;

        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderMediaOverlay(true);
        localVideoviewcontainer.addView(mLocalView);
        // Set the local video view.
        VideoCanvas localVideoCanvas = new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(localVideoCanvas);
        joinChannel();
    }



    private void setupRemoteVideo(int uid) {

        // Create a SurfaceView object.
//        private RelativeLayout mRemoteContainer;
         SurfaceView mRemoteView;


        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        remoteViewContainer.addView(mRemoteView);
        // Set the remote video view.
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));

    }
    @Override
    protected void viewReady(View v) {



        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
//            initEngineAndJoinChannel();

        }
        teleToken= getIntent().getStringExtra("teleToken");
        emergencyid=getIntent().getStringExtra("emergencyId");
        startProgress(new String [] {"Joining Channel","Please wait a while"});
        initializeEngine();

    }
    private void joinChannel() {

        // Join a channel with a token.
        //TODO CHANGE THIS TOKEN AND CHANNEL
        mRtcEngine.joinChannel(teleToken, emergencyid, "Extra Optional Data", 0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCallEnd) {
            leaveChannel();
        }
        RtcEngine.destroy();
    }

    private void leaveChannel() {
        // Leave the current channel.
        mRtcEngine.leaveChannel();
    }
    //Permission checker method
    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }
    @Override
    protected int getRootView() {
        return R.layout.activity_telemedicine_video;
    }
}
