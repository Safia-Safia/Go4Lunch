package com.safia.go4lunch.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {


    //Default Constructor
    public PageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return(3);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
              //  return MapFragment.newInstance();
            case 1:
                //return ListViewFragment.newInstance();
            case 2:
                //return WorkmatesFragment.newInstance();
            default:
                return null;
        }
    }

}
