package espritsoft.com.playvideo.Folder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import espritsoft.com.playvideo.Folder.AudioFolder.AudioFolderFragment;
import espritsoft.com.playvideo.Folder.FolderOpen.FolderOpenFragment;
import espritsoft.com.playvideo.Folder.TabLayout.ViewPagerAdaptor;
import espritsoft.com.playvideo.Folder.VideoFolder.VideoFolderFragment;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FolderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FolderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private static FolderFragment instence;
    public static FolderFragment getInstance(){
        return instence;
    }




    View view;
    TabLayout tabLayout;
    ViewPager viewPager;
    FrameLayout ctr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_folder, container, false);
        instence=this;

        Home.getInstance().AudioFolder();
        Home.getInstance().videoFolderList();
        initLayout();

        if(savedInstanceState !=null){
            isAudio=savedInstanceState.getBoolean("vcl");
            Home.getInstance().folderOpen=savedInstanceState.getBoolean("fOpeClose");
                folderOpenClose(Home.getInstance().folderOpen);
                openedFolderName=savedInstanceState.getString("fop");
        }else{
            folderOpenClose(Home.getInstance().folderOpen);
        }
        if(savedInstanceState != null){
            int tabSeclected=savedInstanceState.getInt("tab");
            if(tabSeclected==0){
                tabLayout.selectTab(tabLayout.getTabAt(0));
            }else{
                tabLayout.selectTab(tabLayout.getTabAt(1));
            }

        }

        initLayout();



        return view;

    }

    /** opened folder name**
      */
    private ConstraintSet constraintSet = new ConstraintSet();
    ConstraintLayout.LayoutParams paramsminiView,paramsmSRootView;
    ConstraintLayout folderContainer,fopenRootContainer;
    Guideline guidelineMiniView,guidelineSRootView;
    
    ViewPagerAdaptor adaptor;
    public int tabSeclected=1;
    private void initLayout() {

        //toolbar
        TextView tollbarTitle=view.findViewById(R.id.search);
        tollbarTitle.setText("Folder");
        tollbarTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


        viewPager=view.findViewById(R.id.viewPager);
        tabLayout=view.findViewById(R.id.tablayout);

        //Log.d("tag","exutt");
//
        adaptor=new ViewPagerAdaptor(getActivity().getSupportFragmentManager());
        adaptor.addFragment(new AudioFolderFragment(), null);
        adaptor.addFragment(new VideoFolderFragment(), null);
        viewPager.setAdapter(adaptor);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_queue_music_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_play_arrow_24);
        tabSeclected=tabLayout.getSelectedTabPosition();
        tabLayout.selectTab(tabLayout.getTabAt(1));


  
        //========== folderOpen View Initialize ==
        fopenRootContainer=view.findViewById(R.id.rootCointanerFolder);
        folderContainer=view.findViewById(R.id.frmFolderContainer);
        guidelineMiniView=view.findViewById(R.id.guidline_below_fOpen);
        paramsminiView = (ConstraintLayout.LayoutParams) guidelineMiniView.getLayoutParams();
  
    }

    
    public boolean isAudio=true;
    public String openedFolderName;
    public void folderOpenClose(boolean open){
        if(open){
           Fragment mFragment = new FolderOpenFragment();
            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(folderContainer.getId(), mFragment).commit();
//
//
            paramsminiView.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidelineMiniView.setLayoutParams(paramsminiView);

            constraintSet.applyTo(fopenRootContainer);
            Home.getInstance().folderOpen=true;


            animationExpand();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Home.getInstance().bottomNavigationHide();
                }
            },500);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Home.getInstance().bottomNavigationShow();
                }
            },1000);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Transition  transition = new ChangeBounds();
                transition.setInterpolator(new AnticipateOvershootInterpolator());
                transition.setDuration(1000);

                TransitionManager.beginDelayedTransition(fopenRootContainer, transition);
            }

            paramsminiView.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineMiniView.setLayoutParams(paramsminiView);

            constraintSet.applyTo(fopenRootContainer);

            //  slideDown(folderContainer);
            Home.getInstance().folderOpen=false;

//            statusbarGrey();

        }
    }



    void animationExpand() {
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 1, 0);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 1, 0);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 1, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 1, 1);
        @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder pvhRoundness = PropertyValuesHolder.ofFloat("roundness", 0, 1);

        final Animator collapseExpandAnim = ObjectAnimator.ofPropertyValuesHolder(folderContainer, pvhLeft, pvhTop,
                pvhRight, pvhBottom, pvhRoundness);
        collapseExpandAnim.setupStartValues();

        folderContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                folderContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                collapseExpandAnim.setupEndValues();
                collapseExpandAnim.start();
                return false;
            }
        });
    }




    @Override
    public void onStart() {
        super.onStart();
        Log.d("tag","sst");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("tag","rss");
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab",tabSeclected);
        outState.putBoolean("fOpeClose", Home.getInstance().folderOpen);
        outState.putBoolean("vcl",isAudio);
        outState.putString("fop",openedFolderName);
    }
}