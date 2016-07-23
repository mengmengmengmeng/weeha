package ph.codebuddy.weeha.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ph.codebuddy.weeha.fragment.GroupFragment;
import ph.codebuddy.weeha.fragment.MapFragment;
import ph.codebuddy.weeha.fragment.SettingFragment;
import ph.codebuddy.weeha.fragment.TrackFragment;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        switch (index) {
            case 0:
                return new MapFragment();
            case 1:
                return new TrackFragment();
            case 2:
                return new GroupFragment();
            case 3:
                return new SettingFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 4;
    }

}