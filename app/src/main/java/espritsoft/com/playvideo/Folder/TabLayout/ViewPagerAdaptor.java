package espritsoft.com.playvideo.Folder.TabLayout;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import espritsoft.com.playvideo.Folder.FolderFragment;

public class ViewPagerAdaptor extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList=new ArrayList<>();
    private final List<String>fragmentTitleList=new ArrayList<>();

    public ViewPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return  fragmentList.get(position);

    }

    @Override
    public int getCount() {
        return fragmentTitleList.size() ;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment,String title){
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
}
