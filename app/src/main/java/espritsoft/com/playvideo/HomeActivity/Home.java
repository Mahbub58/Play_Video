package espritsoft.com.playvideo.HomeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import espritsoft.com.playvideo.Audio.AudioFragment;
import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudioPlay;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.ActionPlaying;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.HeadsetPlugInRecever;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.MediaButtonEventReceiver;
import espritsoft.com.playvideo.Folder.FolderFragment;
import espritsoft.com.playvideo.Folder.Model.FolderModul;
import espritsoft.com.playvideo.HistoryLibrary.HistoryFragment;
import espritsoft.com.playvideo.HomeActivity.ViewModel.HomeViewModel;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Search.SearchAudio.SearchViewAudio;
import espritsoft.com.playvideo.Search.SearchVideo.SearchVideoFragment;
import espritsoft.com.playvideo.Video.VideoFragment;
import espritsoft.com.playvideo.Video.VideoModel;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsFragment;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsModel;
import espritsoft.com.playvideo.VideoPlay.VideoPlayFragment;



public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,ActionPlaying{

    public static boolean fl=true;




    //===================== backPrease
    public boolean folderOpen=false;
    public boolean playlistOpen=false;
    public String audiouOpen="ideal";//from play system
    public boolean forcelyActivetedLandspeceMood=false;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

//        if(videoPlayIsActive==1){
//            ScreenViewOnBack(true);
//        }else{
//            ScreenViewOnBack(false);
//        }

        if(videoPlayIsActive.equals("Expended")) {
            if(forcelyActivetedLandspeceMood){
                setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_USER);
                forcelyActivetedLandspeceMood=false;
            }else{
                ScreenViewOnBack(true);
            }

        }else if(audiouOpen.equals("Expended")) {
            if(forcelyActivetedLandspeceMood){
                setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_USER);
                forcelyActivetedLandspeceMood=false;
            }else{
                ScreenViewOnBack(false);
            }
        }else if(folderOpen){
            FolderFragment.getInstance().folderOpenClose(false);
        }else if(playlistOpen){
            HistoryFragment.getInstance().ListOpenClose(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Home.getInstance().bottomNavigationShow();
                }
            },1000);

        }
    }


    /**
     *   =============================== remove fragment
     */

    public void removeFragment() {
        getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.frmVideoContainer)).commit();
    }


    /***==============================================  Start Desgin ===================================================================================== ***/


    /**Desgin for Audio contaiber and details **/
    //========================= Fram Size ===========
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void AudioFremmSizeIsExpendet(boolean isExpendent){
        if(isExpendent){
            paramsBottom.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineBottom.setLayoutParams(paramsBottom);

            parmsAudioVartical.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidline_musicView.setLayoutParams(parmsAudioVartical);
            constraintSet.applyTo(frm_audio_container);

           AudioPlay.getInstance().isExpandAudiouView(true);
            audiouOpen="Expended";
        }else {
            // Desplay Oriantation
            int orientation =getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
               //land mod
                paramsBottom.guidePercent = 0.82F; // 45% // range: 0 <-> 1
                guidelineBottom.setLayoutParams(paramsBottom);

                parmsAudioVartical.guidePercent = 0.70F; // 45% // range: 0 <-> 1
                guidline_musicView.setLayoutParams(parmsAudioVartical);
            }else{
                paramsBottom.guidePercent = 0.90F; // 45% // range: 0 <-> 1
                guidelineBottom.setLayoutParams(paramsBottom);

                parmsAudioVartical.guidePercent = 0.83F; // 45% // range: 0 <-> 1
                guidline_musicView.setLayoutParams(parmsAudioVartical);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AudioPlay.getInstance().isExpandAudiouView(false);
                }
            },100);

           audiouOpen="down";

            /**
             * Force land mode is on
             */
            if(forcelyActivetedLandspeceMood){
                homeViewModel.ScreenPotraitMode(this);
                forcelyActivetedLandspeceMood=false;
            }
        }
        AudioAnimation();


    }


    //======================= play video ==========================

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void PlayAudio() {
        if(videoPlayIsActive.equals("Expended") || videoPlayIsActive.equals("down") ){

                frmVideoContainer.setVisibility(View.INVISIBLE);
                frmDetailsContainer.setVisibility(View.INVISIBLE);
                VideoPlayFragment.getInstance().StopVideo();


        }else {

        }
        frm_audio_container.setVisibility(View.VISIBLE);

        mFragment = new AudioPlay();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(frm_audio_container.getId(), mFragment).commit();

        videoPlayIsActive="ideal";


            ScreenView(false);



       AudioContainerMove();

    }

    private float startAudioX = 0F;
    private float startAudioY = 0F;
    private float audiodX = 0F;
    private float audiodY = 0F;
    float  startAudioState=0F;
    float stateAudioX=0f;
    float varticalPersentageAudio  =0f;
    float percentHorizontalAudio=0f;
    float audioDirectionY=0f;
    float audioDirectionX=0f;
  public void AudioContainerMove(){
        frm_audio_container.setOnTouchListener(new View.OnTouchListener() {

            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onDoubleTap(MotionEvent e) {


                   float f= (e.getRawY() + audiodY) / Float.valueOf(deviceheight);
                   if(f<0.65){
                       Log.d("TEST", "onDoubleTap============ left back==========");
                   }else if(f>0.65)
                       Log.d("TEST", "onDoubleTap============== right ========");


                    return super.onDoubleTap(e);
                }
            });


            @SuppressLint("ClickableViewAccessibility")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startAudioX = event.getRawX();
                        startAudioY = event.getRawY();
                        audiodX = v.getX() - startAudioX;
                        audiodY = v.getY() - startAudioY;


                        audioDirectionY = (event.getRawY() + audiodY) / Float.valueOf(deviceheight);

                        // Log.d(TAG, "xDwn"+event.x)
                        // Log.d(TAG, "yDwn"+event.y)
                       // startAudioState = (startAudioY + audiodY) / Float.valueOf(deviceheight);
                     //   stateAudioX = (startAudioX + audiodX) / Float.valueOf(deviceWegt);
                        Log.d(TAG, "yDwn" + startAudioState);
                        Log.d(TAG, "xDwn" + stateAudioX);
                      //  audioDirectionY = (event.getRawY() + audiodY) / Float.valueOf(deviceheight);
                      //  audioDirectionX = (event.getRawX() + audiodX) / Float.valueOf(deviceWegt);
                        break;

                    case MotionEvent.ACTION_UP:

                        if (varticalPersentageAudio < 0.45) {
                            AudioFremmSizeIsExpendet(true);
                        } else {
                            AudioFremmSizeIsExpendet(false);
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:

                        varticalPersentageAudio = (event.getRawY() + audiodY) / Float.valueOf(deviceheight);
                        percentHorizontalAudio = (event.getRawX() + audiodX) / Float.valueOf(deviceWegt);

                        // if(varticalPersentageAudio)
                        Log.d("audiooX==", "" + varticalPersentageAudio);
                        // Log.d("audiooY==",""+percentHorizontalAudio);
                        //audo View chenge

                        float i=audioDirectionY-varticalPersentageAudio;
                        if (i<0 || 0<i) {

                        AudioPlay.getInstance().HideShowAudioView(varticalPercentage);



                            if (varticalPersentageAudio < 0.92f ) {
                                parmsAudioVartical.guidePercent = varticalPersentageAudio; // 45% // range: 0 <-> 1
                                guidline_musicView.setLayoutParams(parmsAudioVartical);
                            }


                            if (varticalPersentageAudio <= 0.0) {
//

                                paramsBottom.guidePercent = 1F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);


                            } else if (varticalPersentageAudio < 0.1) {

                                paramsBottom.guidePercent = 0.99F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);


                            } else if (varticalPersentageAudio < 0.2) {
//
                                paramsBottom.guidePercent = 0.98F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);


                            } else if (varticalPersentageAudio < 0.25) {
//
                                paramsBottom.guidePercent = 0.97F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);


                            } else if (varticalPersentageAudio < 0.3) {
//
                                paramsBottom.guidePercent = 0.96F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);


                            } else if (varticalPersentageAudio < 0.35) {
//
                                paramsBottom.guidePercent = 0.95F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);

                            } else if (varticalPersentageAudio < 0.4) {
//
                                paramsBottom.guidePercent = 0.94F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);


                            } else if (varticalPersentageAudio < 0.45) {

                                paramsBottom.guidePercent = 0.93F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);


                            } else if (varticalPersentageAudio < 0.5) {
//
                                paramsBottom.guidePercent = 0.92F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);

                                frmDetailsContainer.setAlpha(0.2F);

                            } else if (varticalPersentageAudio < 0.55) {
//
                                paramsBottom.guidePercent = 0.91F; // 45% // range: 0 <-> 1
                                guidelineBottom.setLayoutParams(paramsBottom);

                            }


                            constraintSet.applyTo(frm_audio_container);
                            break;
                        }
                }

                return true;
            }
        });
    }

    void AudioAnimation(){
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1 );
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 83);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 90, 1);
        @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder pvhRoundness = PropertyValuesHolder.ofFloat("roundness", 0, 1);

        final Animator collapseExpandAnim = ObjectAnimator.ofPropertyValuesHolder(frm_audio_container, pvhLeft, pvhTop,
                pvhRight, pvhBottom, pvhRoundness);
        collapseExpandAnim.setupStartValues();

        frm_audio_container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                frmVideoContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                collapseExpandAnim.setupEndValues();
                collapseExpandAnim.start();
                return false;
            }
        });
    }




        /** desgin for video view frame container raw codeing **/


    float varticalPercentage=0.42F;
    float horizantalPercentage=0.68F;
    float bootomPercentage=0.90F;
    float endPercentage=0.97F;
  //========================= Fram Size ===========
  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public void VideoFremmSizeIsExpendet(boolean isExpendent){
        if(isExpendent){
            paramsHorizantal.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidelineHorizontal.setLayoutParams(paramsHorizantal);

            paramsVartical.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidelineVertical.setLayoutParams(paramsVartical);

            paramsBottom.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineBottom.setLayoutParams(paramsBottom);

            paramsMarzinEnd.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);


            frmDetailsContainer.setAlpha(1F);
            constraintSet.applyTo(frmVideoContainer);
            constraintSet.applyTo(frmDetailsContainer);

           statusBarTextWhite();
           //animationExpand();
            animationUp();
           // state
            directionYIsNotActive=true;
            directionYIsProces=false;
            videoViewDismis.setVisibility(View.INVISIBLE);
            videoPlayIsActive="Expended";
        }else {
            // Desplay Oriantation
            int orientation =getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscap
                paramsHorizantal.guidePercent = 0.5F; // 45% // range: 0 <-> 1
                guidelineHorizontal.setLayoutParams(paramsHorizantal);

                paramsVartical.guidePercent = 0.65F; // 45% // range: 0 <-> 1
                guidelineVertical.setLayoutParams(paramsVartical);

                paramsBottom.guidePercent = 0.82F; // 45% // range: 0 <-> 1
                guidelineBottom.setLayoutParams(paramsBottom);

                paramsMarzinEnd.guidePercent = 0.975F; // 45% // range: 0 <-> 1
                guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);


            }else{
                paramsHorizantal.guidePercent = 0.685F; // 45% // range: 0 <-> 1
                guidelineHorizontal.setLayoutParams(paramsHorizantal);

                paramsVartical.guidePercent = 0.425F; // 45% // range: 0 <-> 1
                guidelineVertical.setLayoutParams(paramsVartical);

                paramsBottom.guidePercent = 0.90F; // 45% // range: 0 <-> 1
                guidelineBottom.setLayoutParams(paramsBottom);

                paramsMarzinEnd.guidePercent = 0.975F; // 45% // range: 0 <-> 1
                guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);
            }



            constraintSet.applyTo(frmVideoContainer);
            constraintSet.applyTo(frmDetailsContainer);
           // directionYIsNotActive=true;

            statusbarTextBlack();
            animationDown();

            directionYIsNotActive=true;
            directionYIsProces=true;
            videoViewDismis.setVisibility(View.VISIBLE);
            VideoPlayFragment.getInstance().videoBackHide();

            /**
             * Force land mode is on
             */
            if(forcelyActivetedLandspeceMood){
                homeViewModel.ScreenPotraitMode(this);
                forcelyActivetedLandspeceMood=false;
            }

            videoPlayIsActive="down";
        }

    }
    //===============video move=============
    //Initialize touch variables
    private float startX = 0F;
    private float startY = 0F;
    private float dX = 0F;
    private float dY = 0F;
    float  startState=0F;
    float stateX=0f;
    float varticalPersentage  =0f;
    float percentHorizontal=0f;
    String TAG;
    float directionY=0f;
    float directionX=0f;
    float directionLX=0f;
    float directionYH=0f;
    boolean directionYIsNotActive=true;
    boolean frameVideoContainerRemove=false;
    boolean directionYIsProces=false ;
    public String videoPlayIsActive="ideal";
    boolean videoAudioOpen1stTime=false;
    boolean videoNotMove=true;
    public boolean videoBackOpen=false;
    void videoContainerMove(){
       frmVideoContainer.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();
                        dX = v.getX() - startX;
                        dY = v.getY() - startY;

                        directionYH = (event.getRawY() + dY) / Float.valueOf(deviceheight);

                        // Log.d(TAG, "xDwn"+event.x)
                        // Log.d(TAG, "yDwn"+event.y)
                        startState = (startY + dY) / Float.valueOf(deviceheight);
                        stateX=(startX + dX) / Float.valueOf(deviceWegt);
//                        Log.d(TAG, "yDwn"+startState);
//                        Log.d(TAG, "xDwn"+stateX);
                        break;

                    case MotionEvent.ACTION_UP:
                        //videoPlayFragment
                     //   if(videoOpen==1 && videoBackOpen){
                        if(videoBackOpen){
                           // VideoPlayFragment.getInstance().videoBackOpen(true);
                           VideoPlayFragment.getInstance().videoBackShow();
                            videoBackOpen=false;
                           // Toast.makeText(getApplicationContext(), "Show", Toast.LENGTH_SHORT).show();
                        }else {
                            VideoPlayFragment.getInstance().videoBackHide();
                            videoBackOpen=true;
                           // Toast.makeText(getApplicationContext(), "Hide", Toast.LENGTH_SHORT).show();
                           // VideoPlayFragment.getInstance().videoBackOpen(false);
//                            videoBackOpen=true;
//                            videoNotMove=true;
                        }


                        if(directionY < 0.5 && directionYIsNotActive ) {
                            VideoFremmSizeIsExpendet(true);
                        }else{
                            VideoFremmSizeIsExpendet(false);
                        }

                        /**remove framVideoContainer =============  **/
                        if(directionLX<0.02 && frameVideoContainerRemove ){
                           frmVideoContainer.setVisibility(View.GONE);
                        //   frmDetailsContainer.setVisibility(View.GONE);;
                            videoPlayIsActive="ideal";
                            VideoPlayFragment.getInstance().StopVideo();
                            frameVideoContainerRemove=false;
                            videoBackOpen=false;
                        }



                        break;
                    case MotionEvent.ACTION_MOVE:
                        //video play back hide
                         VideoPlayFragment.getInstance().videoBackHide();
                        //VideoPlayFragment.getInstance().videoBackOpenDismis(true);

                        int orientation =getResources().getConfiguration().orientation;
                        if (orientation != Configuration.ORIENTATION_LANDSCAPE) {

                            varticalPersentage = (event.getRawY() + dY) / Float.valueOf(deviceheight);

                            directionY = (event.getRawY() + dY) / Float.valueOf(deviceheight);
                            directionLX = (event.getRawX() + dX) / Float.valueOf(deviceWegt);
                            directionX = (event.getRawY() + dY) / Float.valueOf(deviceheight) - 0.35f;
//

                            if (directionY >= percentHorizontal && directionLX >= varticalPercentage) {
                                directionYIsNotActive = true;
                                directionYIsProces = true;
                            }

                            if (directionYIsProces) {
                                if (directionLX <= 0.03) {
                                    frameVideoContainerRemove = true;
                                } else {
                                    frameVideoContainerRemove = false;
                                }

                                //XLeft Marzin remove
                                if (directionLX <= 0.42) {
                                    paramsVartical.guidePercent = directionLX; // 45% // range: 0 <-> 1
                                    guidelineVertical.setLayoutParams(paramsVartical);

                                    if (directionLX + 0.60f <= 0.97) {
                                        paramsMarzinEnd.guidePercent = directionLX + 0.60f; // 45% // range: 0 <-> 1
                                        guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);
                                    }

                                    directionYIsNotActive = false;

                                }
                            }

                            float i = directionYH - directionY;
                            if (i < 0 || 0 < i) {


                                if (directionYIsNotActive) {

//                            if(directionY<0.67 && directionY>0.66)
//                            directionYIsProces=false;
                                    //hight
                                    if (directionY < 0.68) {
                                        paramsHorizantal.guidePercent = directionY; // 45% // range: 0 <-> 1
                                        guidelineHorizontal.setLayoutParams(paramsHorizantal);
                                        directionYIsProces = false;
                                    }
                                    //wightt
                                    if (directionX <= 0.42 && directionX >= 0.0) {
                                        paramsVartical.guidePercent = directionX; // 45% // range: 0 <-> 1
                                        guidelineVertical.setLayoutParams(paramsVartical);
                                    }
                                    //weght end
                                    if (directionX <= 0.0) {
                                        float endX = 0.97f - directionX;
                                        if (endX >= 0.97f && endX <= 1.1f) {
                                            paramsMarzinEnd.guidePercent = endX; // 45% // range: 0 <-> 1
                                            guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);

                                        }
                                    }
                                    //bottom Y
                                    float Ybotom = 1.3f - directionY;
                                    if (Ybotom >= 0.91 && Ybotom <= 1.1) {
                                        paramsBottom.guidePercent = Ybotom; // 45% // range: 0 <-> 1
                                        guidelineBottom.setLayoutParams(paramsBottom);
                                    }

                                    // visible FramDetailsContainer
                                    float YVisable = 0.5f - directionY;
                                    if (YVisable > 0.0 && YVisable < 1.0) {
                                        frmDetailsContainer.setAlpha(YVisable + 0.6f);
                                    }
                                }

                            }
                        }


//                        if(percentHorizontal<0.40  && directionYIsNotActive) {
//                            if(directionYIsNotActive) {
//                                Log.d(TAG, "moveHDDH" + percentHorizontal);
//                                paramsVartical.guidePercent = percentHorizontal; // 45% // range: 0 <-> 1
//                                guidelineVertical.setLayoutParams(paramsVartical);
//
//                                paramsMarzinEnd.guidePercent = percentHorizontal + 0.55f; // 45% // range: 0 <-> 1
//                                guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);
//
//                                if(varticalPersentage>0.67 && percentHorizontal<0.02){
//                                    frameVideoContainerRemove=true;
//                                }else {
//                                    frameVideoContainerRemove=false;
//                                }
//
//                                    directionYIsProces=false;
//
//                           }
//
//
//                        }else if(varticalPersentage<0.67) {
//
//                            directionYIsNotActive = false;
//
////                            if (varticalPersentage < 0.6) {
////                                directionYIsNotActive = false;
////                            }
//
//                          //  if (directionYIsProces) {
//
//
//                                if (varticalPersentage > 0.3) {
////                            constraintSet.setGuidelinePercent(
////                                    guidelineHorizontal.getId(),
////                                    varticalPersentage
////                            );
//
//                                    paramsHorizantal.guidePercent = varticalPersentage; // 45% // range: 0 <-> 1
//                                    guidelineHorizontal.setLayoutParams(paramsHorizantal);
//
//
//                                } else {
////                            constraintSet.setGuidelinePercent(
////                                    guidelineHorizontal.getId(),
////                                    varticalPersentage
////                            );
////                            constraintSet.setGuidelinePercent(
////                                    guidelineVertical.getId(),
////                                    varticalPersentage
////                            );
//                                    paramsHorizantal.guidePercent = varticalPersentage; // 45% // range: 0 <-> 1
//                                    guidelineHorizontal.setLayoutParams(paramsHorizantal);
//
//                                    paramsVartical.guidePercent = varticalPersentage; // 45% // range: 0 <-> 1
//                                    guidelineVertical.setLayoutParams(paramsVartical);
//
//                                }
//
//                                if (startState <= 0.0 || startState > 0.5) {
//
//                                    if (varticalPersentage < 0.0) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineMarginEnd.getId(),
////                                        1f
////                                );
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),1f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 1f);
//
//                                        paramsBottom.guidePercent = 1F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        paramsMarzinEnd.guidePercent = 1F; // 45% // range: 0 <-> 1
//                                        guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);
//
//                                    } else if (varticalPersentage < 0.1) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineMarginEnd.getId(),
////                                        0.99f
////                                );
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),0.99f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 0.9f);
//
//                                        paramsBottom.guidePercent = 0.99F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        paramsMarzinEnd.guidePercent = 0.99F; // 45% // range: 0 <-> 1
//                                        guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);
//                                        frmDetailsContainer.setAlpha(0.9F);
//
//                                    } else if (varticalPersentage < 0.2) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineMarginEnd.getId(),
////                                        0.98f
////                                );
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),0.98f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 0.8f);
//
//                                        paramsBottom.guidePercent = 0.98F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        paramsMarzinEnd.guidePercent = 0.98F; // 45% // range: 0 <-> 1
//                                        guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);
//                                        frmDetailsContainer.setAlpha(0.8F);
//
//                                    } else if (varticalPersentage < 0.25) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),0.97f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 0.7f);
//                                        paramsBottom.guidePercent = 0.97F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        frmDetailsContainer.setAlpha(0.7F);
//
//                                    } else if (varticalPersentage < 0.3) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),0.96f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 0.6f);
//
//                                        paramsBottom.guidePercent = 0.96F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        frmDetailsContainer.setAlpha(0.6F);
//
//                                    } else if (varticalPersentage < 0.35) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),0.95f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 0.5f);
//                                        paramsBottom.guidePercent = 0.95F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        frmDetailsContainer.setAlpha(0.5F);
//
//                                    } else if (varticalPersentage < 0.4) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),0.94f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 0.4f);
//                                        paramsBottom.guidePercent = 0.94F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        frmDetailsContainer.setAlpha(0.4F);
//
//                                    } else if (varticalPersentage < 0.45) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),0.93f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 0.3f);
//                                        paramsBottom.guidePercent = 0.93F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        frmDetailsContainer.setAlpha(0.3F);
//
//                                    } else if (varticalPersentage < 0.5) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),0.92f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 0.2f);
//
//                                        paramsBottom.guidePercent = 0.92F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        frmDetailsContainer.setAlpha(0.2F);
//
//                                    } else if (varticalPersentage < 0.55) {
////                                constraintSet.setGuidelinePercent(
////                                        guidelineBottom.getId(),0.91f);
////                                constraintSet.setAlpha(frmDetailsContainer.getId(), 0.1f);
//                                        paramsBottom.guidePercent = 0.91F; // 45% // range: 0 <-> 1
//                                        guidelineBottom.setLayoutParams(paramsBottom);
//
//                                        frmDetailsContainer.setAlpha(0.1F);
//                                    }
//
//                                    //}else if(){
//
//                                }
//                            }

                     //   }




                        constraintSet.applyTo(frmVideoContainer);
                        constraintSet.applyTo(frmDetailsContainer);
                        break;
                }


                return true;
            }
        });
    }
//=======================video screen ration
    boolean isChanged=false;
   public void ScreenRatioChange() {
        if(isChanged){
           // constraintSet.clone(rotContiner);
            constraintSet.setDimensionRatio(frmVideoContainer.getId(), "16:9");
            constraintSet.applyTo(rotContiner);
            isChanged=false;
        }else{
           // constraintSet.clone(rotContiner);
            constraintSet.setDimensionRatio(frmVideoContainer.getId(), "9:16");
            constraintSet.applyTo(rotContiner);
            isChanged=true;
            Toast.makeText(getApplicationContext(), "inside", Toast.LENGTH_SHORT).show();
        }

    }
    public void DefaultScreenRatio(){
        constraintSet.setDimensionRatio(frmVideoContainer.getId(), "16:9");
        constraintSet.applyTo(rotContiner);
    }



//=================================================== bottom navigation hide show
    public void bottomNavigationHide(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Transition  transition = new ChangeBounds();
            transition.setInterpolator(new AnticipateOvershootInterpolator());
            transition.setDuration(500);

            TransitionManager.beginDelayedTransition(rotContiner, transition);
        }
        // Desplay Oriantation
        int orientation =getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parmsAudioVartical.guidePercent = 0.86F; // 45% // range: 0 <-> 1
            guidline_musicView.setLayoutParams(parmsAudioVartical);

            paramsBottom.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineBottom.setLayoutParams(paramsBottom);
        }else{
            parmsAudioVartical.guidePercent = 0.93F; // 45% // range: 0 <-> 1
            guidline_musicView.setLayoutParams(parmsAudioVartical);

            paramsBottom.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineBottom.setLayoutParams(paramsBottom);
        }


        constraintSet.applyTo(frm_audio_container);
        constraintSet.applyTo(rotContiner);
    }
    public void bottomNavigationShow(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Transition transition = new ChangeBounds();
            transition.setInterpolator(new AnticipateOvershootInterpolator());
            transition.setDuration(500);

            TransitionManager.beginDelayedTransition(rotContiner, transition);
        }
        // Desplay Oriantation
        int orientation =getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscap
            paramsBottom.guidePercent = 0.84F; // 45% // range: 0 <-> 1
            guidelineBottom.setLayoutParams(paramsBottom);


            parmsAudioVartical.guidePercent = 0.70F; // 45% // range: 0 <-> 1
            guidline_musicView.setLayoutParams(parmsAudioVartical);
        }else {
            paramsBottom.guidePercent = 0.91F; // 45% // range: 0 <-> 1
            guidelineBottom.setLayoutParams(paramsBottom);

            parmsAudioVartical.guidePercent = 0.83F; // 45% // range: 0 <-> 1
            guidline_musicView.setLayoutParams(parmsAudioVartical);
        }
        constraintSet.applyTo(frm_audio_container);
        constraintSet.applyTo(rotContiner);
    }
    //============================= video view===============================================================
  //  public long videoProgress=0;
  // public String videoSongUri,videoSongName,videoSongDuration,videoSongFolder,videoSongCover;

    Fragment videoFragment=null,videoDetailsFragment=null;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void PlayVideo() {

        if(videoPlayIsActive.equals("Expended")||videoPlayIsActive.equals("down")){
//            VideoPlayFragment.getInstance().init_videoPlay();
//            VideoDetailsFragment.getInstance().RefrashList();
//            animationUp();
            videoFragment = new VideoPlayFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(frmVideoContainer.getId(), videoFragment).commit();

            mFragment = new VideoDetailsFragment();
            FragmentManager fragmentManagers = getSupportFragmentManager();
            fragmentManagers.beginTransaction()
                    .replace(R.id.frmDetailsContainer, mFragment).commit();
        }else if(videoPlayIsActive.equals("ideal")){
            videoFragment = new VideoPlayFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(frmVideoContainer.getId(), videoFragment).commit();

            mFragment = new VideoDetailsFragment();
            FragmentManager fragmentManagers = getSupportFragmentManager();
            fragmentManagers.beginTransaction()
                    .replace(R.id.frmDetailsContainer, mFragment).commit();
        }

        try{
//            AudioPlaySystem.getInstance().pauseMedia();
            Pause();
            frm_audio_container.setVisibility(View.INVISIBLE);
            audiouOpen="ideal";
        }catch (Exception e){}




        ScreenView(true);
        directionYIsNotActive=true;
        directionYIsProces=false;

        frmVideoContainer.setVisibility(View.VISIBLE);
        frmDetailsContainer.setVisibility(View.VISIBLE);
        frmDetailsContainer.setAlpha(1f);


    }
    private ConstraintSet constraintSet = new ConstraintSet();
    ConstraintLayout.LayoutParams paramsHorizantal;
    ConstraintLayout.LayoutParams paramsVartical;
    ConstraintLayout.LayoutParams paramsBottom;
    ConstraintLayout.LayoutParams paramsMarzinEnd;
    ConstraintLayout.LayoutParams parmsAudioVartical;
    ConstraintLayout.LayoutParams parmsSearchView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void ScreenView(boolean videoPlay) {


        if(videoPlay) {
            paramsHorizantal.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidelineHorizontal.setLayoutParams(paramsHorizantal);

            paramsVartical.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidelineVertical.setLayoutParams(paramsVartical);

            paramsBottom.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineBottom.setLayoutParams(paramsBottom);

            paramsMarzinEnd.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);

           //animation

            constraintSet.applyTo(frmVideoContainer);
            constraintSet.applyTo(frmDetailsContainer);



            videoContainerMove();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarTextWhite();
            }
            videoViewDismis.setVisibility(View.INVISIBLE);
            videoPlayIsActive="Expended";




        }else {
            paramsBottom.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineBottom.setLayoutParams(paramsBottom);

            parmsAudioVartical.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidline_musicView.setLayoutParams(parmsAudioVartical);
            constraintSet.applyTo(frm_audio_container);
            AudioAnimation();
            audiouOpen="Expended";
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void animationUp(){
        Transition transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator());
        transition.setDuration(500);
        TransitionManager.beginDelayedTransition(rotContiner, transition);

        frmDetailsContainer.animate()
                .alpha(1f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        frmDetailsContainer.setAlpha(1F);
                    }
                });
    }

    void animationExpand() {
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 1, 0);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 1, 0);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 1, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 1, 1);
        @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder pvhRoundness = PropertyValuesHolder.ofFloat("roundness", 0, 1);

        final Animator collapseExpandAnim = ObjectAnimator.ofPropertyValuesHolder(frmVideoContainer, pvhLeft, pvhTop,
                pvhRight, pvhBottom, pvhRoundness);
        collapseExpandAnim.setupStartValues();

        frmVideoContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                frmVideoContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                collapseExpandAnim.setupEndValues();
                collapseExpandAnim.start();
                return false;
            }
        });
        // details Container
        PropertyValuesHolder pvhLeft2 = PropertyValuesHolder.ofInt("left", 1,  0);
        PropertyValuesHolder pvhTop2 = PropertyValuesHolder.ofInt("top", 1, 0);
        PropertyValuesHolder pvhRight2 = PropertyValuesHolder.ofInt("right", 1, 1);
        PropertyValuesHolder pvhBottom2 = PropertyValuesHolder.ofInt("bottom", 1, 1);
        @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder pvhRoundness2 = PropertyValuesHolder.ofFloat("roundness", 0, 1);

        final Animator collapseExpandAnimDetails = ObjectAnimator.ofPropertyValuesHolder(frmDetailsContainer, pvhLeft2, pvhTop2,
                pvhRight2, pvhBottom2, pvhRoundness2);
        collapseExpandAnimDetails.setupStartValues();

        frmDetailsContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                frmDetailsContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                collapseExpandAnimDetails.setupEndValues();
                collapseExpandAnimDetails.start();
                return false;
            }
        });


        frmDetailsContainer.animate()
                .alpha(1f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        frmDetailsContainer.setAlpha(1F);
                    }
                });



    }




    void animationDown(){

        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 42, 1 );
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 68, 1);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 97, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 90, 1);
        @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder pvhRoundness = PropertyValuesHolder.ofFloat("roundness", 0, 1);

        final Animator collapseExpandAnim = ObjectAnimator.ofPropertyValuesHolder(frmVideoContainer, pvhLeft, pvhTop,
                pvhRight, pvhBottom, pvhRoundness);
        collapseExpandAnim.setupStartValues();

        frmVideoContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                frmVideoContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                collapseExpandAnim.setupEndValues();
                collapseExpandAnim.start();
                return false;
            }
        });



        PropertyValuesHolder pvhLeft2 = PropertyValuesHolder.ofInt("left", 1,  0);
        PropertyValuesHolder pvhTop2 = PropertyValuesHolder.ofInt("top", 1, 0);
        PropertyValuesHolder pvhRight2 = PropertyValuesHolder.ofInt("right", 1, 1);
        PropertyValuesHolder pvhBottom2 = PropertyValuesHolder.ofInt("bottom", 1, 1);
        @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder pvhRoundness2 = PropertyValuesHolder.ofFloat("roundness", 0, 1);

        final Animator collapseExpandAnimDetails = ObjectAnimator.ofPropertyValuesHolder(frmDetailsContainer, pvhLeft2, pvhTop2,
                pvhRight2, pvhBottom2, pvhRoundness2);
        collapseExpandAnimDetails.setupStartValues();

        frmDetailsContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                frmDetailsContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                collapseExpandAnimDetails.setupEndValues();
                collapseExpandAnimDetails.start();
                return false;
            }
        });


        frmDetailsContainer.animate()
                .alpha(0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        frmDetailsContainer.setAlpha(0F);
                    }
                });

    }

   public void ScreenViewOnBack(boolean videoPlay) {

        if(videoPlay) {
            // Desplay Oriantation
            int orientation =getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscap
                paramsHorizantal.guidePercent = 0.5F; // 45% // range: 0 <-> 1
                guidelineHorizontal.setLayoutParams(paramsHorizantal);

                paramsVartical.guidePercent = 0.65F; // 45% // range: 0 <-> 1
                guidelineVertical.setLayoutParams(paramsVartical);

                paramsBottom.guidePercent = 0.84F; // 45% // range: 0 <-> 1
                guidelineBottom.setLayoutParams(paramsBottom);

                paramsMarzinEnd.guidePercent = 0.975F; // 45% // range: 0 <-> 1
                guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);
            }else{
                paramsHorizantal.guidePercent = 0.685F; // 45% // range: 0 <-> 1
                guidelineHorizontal.setLayoutParams(paramsHorizantal);

                paramsVartical.guidePercent = 0.425F; // 45% // range: 0 <-> 1
                guidelineVertical.setLayoutParams(paramsVartical);

                paramsBottom.guidePercent = 0.90F; // 45% // range: 0 <-> 1
                guidelineBottom.setLayoutParams(paramsBottom);

                paramsMarzinEnd.guidePercent = 0.975F; // 45% // range: 0 <-> 1
                guidelineMarginEnd.setLayoutParams(paramsMarzinEnd);
            }

            constraintSet.applyTo(frmVideoContainer);
            constraintSet.applyTo(frmDetailsContainer);
            VideoPlayFragment.getInstance().videoBackHide();
            // animation();
            animationDown();
            directionYIsNotActive = true;
            statusbarTextBlack();
            videoViewDismis.setVisibility(View.VISIBLE);
           // VideoPlayFragment.getInstance().videoBackOpenDismis(true);
           videoPlayIsActive="down";
        }else{

            // Desplay Oriantation
            int orientation =getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //land
                paramsBottom.guidePercent = 0.81F; // 45% // range: 0 <-> 1
                guidelineBottom.setLayoutParams(paramsBottom);

                parmsAudioVartical.guidePercent = 0.70F; // 45% // range: 0 <-> 1
                guidline_musicView.setLayoutParams(parmsAudioVartical);
            }else{
                paramsBottom.guidePercent = 0.90F; // 45% // range: 0 <-> 1
                guidelineBottom.setLayoutParams(paramsBottom);

                parmsAudioVartical.guidePercent = 0.83F; // 45% // range: 0 <-> 1
                guidline_musicView.setLayoutParams(parmsAudioVartical);
            }

            AudioPlay.getInstance().isExpandAudiouView(false);

            constraintSet.applyTo(frm_audio_container);
            AudioAnimation();
            audiouOpen="down";
        }
        bottomNavigationShow();
    }


        //=========================== layout init
        int deviceheight,deviceWegt;
    int Dwidth,Dheight;
    ImageButton videoViewDismis;
    ConstraintLayout frmVideoContainer, frmDetailsContainer,rotContiner,frm_audio_container,frmSearchViewContainer;
    Guideline guidelineHorizontal, guidelineVertical, guidelineMarginEnd, guidelineBottom,guidline_musicView,guidelineMarzinBottomSearch;
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void initialLayout() {


        //searchView
        frmSearchViewContainer=findViewById(R.id.searchViewContainer);

        //videwo Dismis
        videoViewDismis=findViewById(R.id.videoViewDismis);
        videoViewDismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the animation
                frmVideoContainer.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.anim_slide_out_left));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frmVideoContainer.setVisibility(View.INVISIBLE);
                        frmDetailsContainer.setVisibility(View.INVISIBLE);
                        videoBackOpen=false;
                        ScreenViewOnBack(true);

//                        getSupportFragmentManager().beginTransaction().remove(videoFragment).commit();

//                        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frmVideoContainer)).commit();
//                        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frmDetailsContainer)).commit();
                    }
                },500);
                videoPlayIsActive="ideal";
                VideoPlayFragment.getInstance().StopVideo();
                frameVideoContainerRemove=false;



            }
        });
       
        //musicView
        guidline_musicView=findViewById(R.id.guideLine_musicView);
        frm_audio_container=findViewById(R.id.frm_audio_container);


        frmVideoContainer = findViewById(R.id.frmVideoContainer);
        frmDetailsContainer = findViewById(R.id.frmDetailsContainer);


        rotContiner = findViewById(R.id.rootContainer);
        guidelineHorizontal = findViewById(R.id.guidelineHorizontal);
        guidelineVertical = findViewById(R.id.guidelineVertical);
        guidelineMarginEnd = findViewById(R.id.guidelineMarginEnd);

//

        guidelineHorizontal = (Guideline) findViewById(R.id.guidelineHorizontal);
        guidelineVertical = (Guideline) findViewById(R.id.guidelineVertical);
        guidelineBottom = (Guideline) findViewById(R.id.guidelineBottom);
        guidelineMarginEnd = (Guideline) findViewById(R.id.guidelineMarginEnd);

        guidelineMarzinBottomSearch = (Guideline) findViewById(R.id.guidelineMazinSearchView);
// gdgdg
        paramsHorizantal = (ConstraintLayout.LayoutParams) guidelineHorizontal.getLayoutParams();
        paramsVartical = (ConstraintLayout.LayoutParams) guidelineVertical.getLayoutParams();
        paramsBottom = (ConstraintLayout.LayoutParams) guidelineBottom.getLayoutParams();
        paramsMarzinEnd = (ConstraintLayout.LayoutParams) guidelineMarginEnd.getLayoutParams();
        parmsAudioVartical = (ConstraintLayout.LayoutParams) guidline_musicView.getLayoutParams();
        parmsSearchView = (ConstraintLayout.LayoutParams) guidelineMarzinBottomSearch.getLayoutParams();



        //hight wedth
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceheight = displayMetrics.heightPixels;
        deviceWegt = displayMetrics.widthPixels;






    }




    static Fragment mFragment = null;
    void openFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frmHomeContainer, mFragment).commit();



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Fragment mFragment = null;

        int id = menuItem.getItemId();

        switch (id) {
            case R.id.video:
                mFragment = new VideoFragment();
                openFragment();
                nevItem=1;
                return true;
            case R.id.audio:

                mFragment = new AudioFragment();
                openFragment();
                nevItem=2;
                return true;
            case R.id.folder:

                mFragment = new FolderFragment();
                openFragment();
                nevItem=3;
                return true;
            case R.id.history:

                mFragment = new HistoryFragment();
                openFragment();
                nevItem=4;
                return true;

        }

        return false;
    }

    void statusbarTextBlack(){
     //   getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
       // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(Home.this,R.color.background_color_dd));// set status background white
        }

    }

    void statusBarTextWhite(){




    }



    /***==============================================  End Desgin ===================================================================================== ***/



    /**------------------------------------------- Video song colect from Device **/
    /*** ============ Video Culection ==================================== **/
    //================================================================ User Permission =============================================================

    public void CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED)){
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE,},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        get_All_Video_Song();
        get_all_audio_songs();

    }

    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    get_All_Video_Song();
                    get_all_audio_songs();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Denail", Toast.LENGTH_SHORT)
                            .show();
                   Alart();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public AlertDialog.Builder Alart() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder=new AlertDialog.Builder(this);
        mBuilder.setTitle("Alart")
                .setMessage("App Will not working properly.Please Allow all permission")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckUserPermsions();
                    }
                });

        mBuilder.create();
        mBuilder.show();
        return mBuilder;


    }
    public ArrayList<VideoModel> VideoList =new ArrayList<>();
    public ArrayList<FolderModul> VideoFolderList =new ArrayList<>();
    public void get_All_Video_Song() {
        VideoList.clear();
        Uri allsonguri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        // String selection = MediaStore.Video.Media. + "!=0";
        String orderBy=MediaStore.Images.Media.DATE_MODIFIED;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA};

        Cursor cursor =getApplicationContext().getContentResolver().query(allsonguri, null, null, null, orderBy+" DESC");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String albumArtUriImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                    String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                    String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
                    /** Duration**/
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    int duration1=0;
                    try {
                        duration1 = Integer.parseInt(duration);
                    }catch (Exception e){ }

                    /**
                     * video time collect
                     */
                    int seconds = (int) (duration1 / 1000) % 60 ;
                    int minutes = (int) ((duration1 / (1000*60)) % 60);
                    int hours=(int) ((duration1 / (1000*60*60)) % 24);
                    String second = String.valueOf(seconds);
                    String minute = String.valueOf(minutes);
                    String hour =String.valueOf(hours);
                    /**AlbumArt**/

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    //  Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                    //  String albumArtUriImage = albumArtUri.toString();

                    //find folder
                    String  recpintList=fullpath;
                    String[] recpints=(recpintList.split("/"));
                    String folderName=recpints[recpints.length-2];


                        VideoList.add(new VideoModel(fullpath, song_name, album_name,folderName, artist_name, albumArtUriImage, second, minute,hour));
                        VideoFolderList.add(new FolderModul(folderName,albumArtUriImage,"1"));


                    //    SuffleList.add(new customItem(fullpath, song_name, album_name, artist_name, albumArtUriImage, minute, second));
                } while (cursor.moveToNext());
            }
            cursor.close();


        }

    }



    // cullect Video By folder ==============
    public ArrayList<VideoDetailsModel>VideoListFromfolder=new ArrayList<>();
    public void get_All_Video_by_folder(String folderName) {
        //clear
        VideoListFromfolder.clear();

        Uri allsonguri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        // String selection = MediaStore.Video.Media. + "!=0";
        String orderBy=MediaStore.Images.Media.DATE_MODIFIED;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA};

        Cursor cursor =getApplicationContext().getContentResolver().query(allsonguri, null, null, null, orderBy+" DESC");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String albumArtUriImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                    String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                    String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
                    /** Duration**/
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    int duration1=0;
                    try {
                        duration1 = Integer.parseInt(duration);
                    }catch (Exception e){ }
                    int duration2 = duration1 / 1000;
                    int hour1=duration2/3600;
                    int minute1 = duration2 % 60;
                    int second1 = duration2 / 60;
                    String second = String.valueOf(second1);
                    String minute = String.valueOf(minute1);
                    String hour =String.valueOf(hour1);
                    /**AlbumArt**/

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    //  Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                    //  String albumArtUriImage = albumArtUri.toString();

                    //find folder
                    String  recpintList=fullpath;
                    String[] recpints=(recpintList.split("/"));
                    String folder_Name=recpints[recpints.length-2];


                    if(folderName.equals(folder_Name)) {
                        VideoListFromfolder.add(new VideoDetailsModel(fullpath, song_name, album_name,folderName, artist_name, albumArtUriImage, minute, second, hour));
                    }
                    //    SuffleList.add(new customItem(fullpath, song_name, album_name, artist_name, albumArtUriImage, minute, second));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    // cullect Video By folder ==============
    public ArrayList<AudiouModel>AudiouListFromfolder=new ArrayList<>();
    public void get_All_Audiou_by_folder(String seclectedFolder) {
        //clear
        AudiouListFromfolder.clear();
        String orderBy=MediaStore.Images.Media.DATE_MODIFIED;
        Uri allsonguri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getContentResolver().query(allsonguri, null, selection, null, orderBy+" DESC");




        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    /** Duration**/
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    int duration1=0;
                    try {
                        duration1 = Integer.parseInt(duration);
                    }catch (Exception e){}
                    int duration2 = duration1 / 1000;
                    int hour1 = duration1/3600;
                    int minute1 = duration2 % 60;
                    int second1 = duration2 / 60;
                    String second = String.valueOf(second1);
                    String minute = String.valueOf(minute1);
                    String hour = String.valueOf(hour1);
                    /**AlbumArt**/

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                    String albumArtUriImage = albumArtUri.toString();

                    //find folder
                    String  recpintList=fullpath;
                    String[] recpints=(recpintList.split("/"));
                    String folderName=recpints[recpints.length-2];
                    if(folderName.equals(seclectedFolder)) {
                        if (duration1 > 0.0)
                            AudiouListFromfolder.add(new AudiouModel(fullpath, song_name, album_name, artist_name, albumArtUriImage, minute, second, hour, folderName));
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

    }




    /**------------------------------------------- Audio song colect from Device **/
    /*** ============ Audio Culection ==================================== **/
  public ArrayList<AudiouModel> AudiouSongsList = new ArrayList();
    public ArrayList<FolderModul> audioFolderList=new ArrayList();
    public void get_all_audio_songs() {
        String orderBy=MediaStore.Images.Media.DATE_MODIFIED;
        Uri allsonguri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getContentResolver().query(allsonguri, null, selection, null, orderBy+" DESC");


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    /** Duration**/
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    int duration1=0;
                    try {
                         duration1 = Integer.parseInt(duration);
                    }catch (Exception e){ }
                    int duration2 = duration1 / 1000;
                    int hour1 = duration1/3600;
                    int minute1 = duration2 % 60;
                    int second1 = duration2 / 60;
                    String second = String.valueOf(second1);
                    String minute = String.valueOf(minute1);
                    String hour = String.valueOf(hour1);
                    /**AlbumArt**/

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                    String albumArtUriImage = albumArtUri.toString();

                    //find folder
                    String  recpintList=fullpath;
                    String[] recpints=(recpintList.split("/"));
                    String folderName=recpints[recpints.length-2];

                    if(duration1>0.0)
                    audioFolderList.add(new FolderModul(folderName,albumArtUriImage,"1"));
                    if(duration1>0.0)
                    AudiouSongsList.add(new AudiouModel(fullpath, song_name, album_name, artist_name, albumArtUriImage, minute, second,hour,folderName));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

    }
  // ========================= get audio Song for queu listt
  public void AudiouQueueList(){
        AudioPlaySystem.getInstance().queueList.clear();
      String orderBy=MediaStore.Images.Media.DATE_MODIFIED;
      Uri allsonguri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
      String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

      Cursor cursor = getContentResolver().query(allsonguri, null, selection, null, orderBy+" DESC");

      if (cursor != null) {
          if (cursor.moveToFirst()) {
              do {
                  String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                  String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                  String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                  String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                  String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                  /** Duration**/
                  String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                  int duration1=0;
                  try {
                      duration1 = Integer.parseInt(duration);
                  }catch (Exception e){ }
                  /**
                   * video time collect
                   */
                  int seconds = (int) (duration1 / 1000) % 60 ;
                  int minutes = (int) ((duration1 / (1000*60)) % 60);
                  int hours=(int) ((duration1 / (1000*60*60)) % 24);
                  String second = String.valueOf(seconds);
                  String minute = String.valueOf(minutes);
                  String hour =String.valueOf(hours);
                  /**AlbumArt**/

                  Uri sArtworkUri = Uri
                          .parse("content://media/external/audio/albumart");
                  Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                  String albumArtUriImage = albumArtUri.toString();

                  //find folder
                  String  recpintList=fullpath;
                  String[] recpints=(recpintList.split("/"));
                  String folderName=recpints[recpints.length-2];

                  if(duration1>0.0)
                  AudioPlaySystem.getInstance().queueList.add(new AudiouModel(fullpath, song_name, album_name, artist_name, albumArtUriImage, second,minute,hour,folderName));
              } while (cursor.moveToNext());
          }
          cursor.close();
      }
  }

    // ============ all video Folder coullect==========================================================
                         /**folder colect **/
    int totalV=1;
    boolean state;
    public ArrayList<FolderModul> VideoFolderListItem =new ArrayList<>();
    public void videoFolderList(){
        VideoFolderListItem.clear();

        try {
          Collections.sort(VideoFolderList, new Comparator<FolderModul>() {
            @Override
            public int compare(FolderModul o1, FolderModul o2) {
                return o1.getFolderName().compareTo(o2.getFolderName());
            }

        });
        }catch (Exception e){
            Log.e("error=",""+e);
        }


        if(VideoFolderList.size()==1){
            int p=0;
            VideoFolderListItem.add(new FolderModul(VideoFolderList.get(p).getFolderName(),VideoFolderList.get(p).getCover(),String.valueOf(1)));
        }



        for (int i = 1; i < VideoFolderList.size(); i++) {
            String a1 = VideoFolderList.get(i).getFolderName();
            String cover=VideoFolderList.get(i-1).getCover();
            String a2 = VideoFolderList.get(i-1).getFolderName();

            if (!a1.equals(a2)) {
                VideoFolderListItem.add(new FolderModul(a2,cover,String.valueOf(totalV)));

                if(i==VideoFolderList.size()-1){
                    VideoFolderListItem.add(new FolderModul(VideoFolderList.get(i).getFolderName(),VideoFolderList.get(i).getCover(),String.valueOf(1)));
                }
                totalV=1;
            }else totalV++;

            if (a1.equals(a2))
                if(i==VideoFolderList.size()-1){
                    VideoFolderListItem.add(new FolderModul(VideoFolderList.get(i).getFolderName(),VideoFolderList.get(i).getCover(),String.valueOf(totalV)));
                }
        }


    }
  //================== Find Audio Folder
  int totalA=1;
  public ArrayList<FolderModul> audiouFolderListItem=new ArrayList<>();
  public void AudioFolder(){
      totalA=1;
      //clear
      audiouFolderListItem.clear();

      Collections.sort(audioFolderList, new Comparator<FolderModul>() {
          @Override
          public int compare(FolderModul o1, FolderModul o2) {
              return o1.getFolderName().compareTo(o2.getFolderName());
          }

      });

      if(audioFolderList.size()==1){
          int p=0;
          audiouFolderListItem.add(new FolderModul(audioFolderList.get(p).getFolderName(),audioFolderList.get(p).getCover(),String.valueOf(1)));
      }

      for (int i = 1; i < audioFolderList.size(); i++) {
          String a1 = audioFolderList.get(i).getFolderName();
          String cover=audioFolderList.get(i-1).getCover();
          String a2 = audioFolderList.get(i-1).getFolderName();

          if (!a1.equals(a2)) {
              audiouFolderListItem.add(new FolderModul(a2,cover,String.valueOf(totalA)));

              if(i==audioFolderList.size()-1){
                  audiouFolderListItem.add(new FolderModul(audioFolderList.get(i).getFolderName(),audioFolderList.get(i).getCover(),String.valueOf(1)));
              }
              totalA=1;
          }else totalA++;

          if (a1.equals(a2))
              if(i==audioFolderList.size()-1){
                  audiouFolderListItem.add(new FolderModul(audioFolderList.get(i).getFolderName(),audioFolderList.get(i).getCover(),String.valueOf(totalA)));
              }
      }
    }


    //================================ SearchView
   public void videoSerchViewOpen(){
      Fragment fragment=new SearchVideoFragment();
      FragmentManager fragmentManager=getSupportFragmentManager();
      fragmentManager.beginTransaction().replace(R.id.searchViewContainer,fragment).commit();

       parmsSearchView.guidePercent=0f;
       guidelineMarzinBottomSearch.setLayoutParams(parmsSearchView);
       constraintSet.applyTo(rotContiner);
    }
    public void audioSerchViewOpen(){
        Fragment fragment=new SearchViewAudio();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.searchViewContainer,fragment).commit();

        parmsSearchView.guidePercent=0f;
        guidelineMarzinBottomSearch.setLayoutParams(parmsSearchView);
        constraintSet.applyTo(rotContiner);
    }
    public void SerchViewClose(){
        parmsSearchView.guidePercent=1f;
        guidelineMarzinBottomSearch.setLayoutParams(parmsSearchView);
        constraintSet.applyTo(rotContiner);
    }



    //===================================== song Play System
    void StartMusciService(){
        Intent serviceIntent = new Intent(this, AudioPlaySystem.class);
        startService(serviceIntent);
    }





    /**
     *
     * Head Set , Airbud , Micro phone configuration
     *
     *
     */
    //Media button listner head set
    private AudioManager mAudioManager;
    private ComponentName mRemoteControlResponder;
    HeadsetPlugInRecever headsetPlugInRecever;
    public void HeadSetConfiguration(){

        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        headsetPlugInRecever=new HeadsetPlugInRecever();
        registerReceiver(headsetPlugInRecever, receiverFilter);



        //Media button listner head set
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        mRemoteControlResponder = new ComponentName(getPackageName(),
                MediaButtonEventReceiver.class.getName());

    }




    //===================================================================================== start activity
    //History Fragment
    public String opendedPlayListName="favourite";
    //folderFragment
    public String opendedFolderName="";
    public boolean isAudio;
    private static Home instance;
    Toolbar toolbar;

    HomeViewModel homeViewModel;
    BottomNavigationView bottomNavigationView;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        instance=this;
        homeViewModel= ViewModelProviders.of(this).get(HomeViewModel.class);
//        toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //statusBarHideShow
        int orientation =getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
           getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }



        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        initialLayout();
        CheckUserPermsions();

        //activity recreated load session
                   //restor nevigation layout
        if (savedInstanceState != null) {
            int option = savedInstanceState.getInt("nevItem");
            switch (option) {
                case 1:
                    mFragment = new VideoFragment();
                    openFragment();
                    break;
                case 2:
                    mFragment = new AudioFragment();
                    openFragment();
                    break;
                case 3:
                    mFragment = new FolderFragment();
                    openFragment();
                    break;
                case 4:
                    mFragment = new HistoryFragment();
                    openFragment();
                    break;
            }
        } else {  //open vodeo view
            mFragment = new VideoFragment();
            openFragment();
        }
                   //restore videoDetails
        if (savedInstanceState != null) {



            audiouOpen = savedInstanceState.getString("audioView");



            if(audiouOpen.equals("Expended")){
                frm_audio_container.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AudioFremmSizeIsExpendet(true);
                    AudioContainerMove();

                }
            }else if(audiouOpen.equals("down")){
                frm_audio_container.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AudioFremmSizeIsExpendet(false);
                    AudioContainerMove();
                }
            }else{
                frm_audio_container.setVisibility(View.INVISIBLE);
            }

            videoPlayIsActive = savedInstanceState.getString("layoutState");
            if(videoPlayIsActive.equals("ideal")) {
                frmVideoContainer.setVisibility(View.INVISIBLE);
                frmDetailsContainer.setVisibility(View.INVISIBLE);
            }else if (videoPlayIsActive.equals("Expended")) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    VideoFremmSizeIsExpendet(true);
                    videoContainerMove();
                }

            } else if (videoPlayIsActive.equals("down")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    VideoFremmSizeIsExpendet(false);
                    videoContainerMove();
                }
            }else{

            }
        }else{
            if (AudioPlaySystem.audioService) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    PlayAudio();
                }
            }
        }

        if(savedInstanceState !=null){
            playlistOpen=savedInstanceState.getBoolean("plOpen");
            folderOpen=savedInstanceState.getBoolean("fopen");
            opendedFolderName=savedInstanceState.getString("folderName");
            isAudio=savedInstanceState.getBoolean("isAudio");
          //  videoProgress=savedInstanceState.getInt("videoProgress");
            forcelyActivetedLandspeceMood=savedInstanceState.getBoolean("forcelyActivetedLandspeceMood");
            opendedPlayListName=savedInstanceState.getString("plname");
        }



        HeadSetConfiguration();


    }

//    // were head phone
//    @Override
//    public boolean onKeyDown (int keyCode, KeyEvent event) {
//        // This is the center button for headphones
//        if (event.getKeyCode() == KeyEvent.KEYCODE_HEADSETHOOK) {
//            Toast.makeText(getApplicationContext(), "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }



    public static Home getInstance() {
        return instance;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent = new Intent(this, AudioPlaySystem.class);
//        startService(intent);
        if (AudioPlaySystem.audioService) {
            //  PlayAudio();

        }
        //Media button listner head set
        mAudioManager.registerMediaButtonEventReceiver(
                mRemoteControlResponder);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this,AudioPlaySystem.class);
        startService(intent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
        // Media button listner head set
        mAudioManager.unregisterMediaButtonEventReceiver(
                mRemoteControlResponder);
    }

    private int nevItem;
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("nevItem",nevItem);
//        outState.putString("videoSogName",videoSongName);
//        outState.putString("videoSogFolder",videoSongFolder);
//        outState.putString("videoSogUri",videoSongUri);
//        outState.putString("videoSogDuration",videoSongDuration);
//        outState.putString("videoSogCover",videoSongCover);

        outState.putBoolean("fopen",folderOpen);
        outState.putBoolean("plOpen",playlistOpen);
        outState.putString("folderName",opendedFolderName);
        outState.putBoolean("forcelyActivetedLandspeceMood",forcelyActivetedLandspeceMood);


        outState.putString("layoutState",videoPlayIsActive);
        outState.putString("audioView",audiouOpen);

        //folder fragment
        outState.putBoolean("isAudio",isAudio);
        //history Fragment
        outState.putString("plname",opendedPlayListName);
//        outState.putInt("videoProgress", (int) videoProgress);

//        outState.putFloat("bottom",);
//        outState.putFloat("vertical",);
//        outState.putFloat("horizantel",);
//        outState.putFloat("end",);
    }






    boolean serviceBound = false;
    AudioPlaySystem player;
    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            AudioPlaySystem.LocalBinder binder = (AudioPlaySystem.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

            Toast.makeText(Home.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    @Override
    public void nextClick() {

    }

    @Override
    public void previousClick() {

    }

    @Override
    public void PlayPause() {

    }

    @Override
    public void Dismis() {

        Toast.makeText(getApplicationContext(),"hhh",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Play() {

    }
}