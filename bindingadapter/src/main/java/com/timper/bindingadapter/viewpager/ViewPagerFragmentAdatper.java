package com.timper.bindingadapter.viewpager;

import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;

/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/9/12
 */
public class ViewPagerFragmentAdatper extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String>   titles;

    public ViewPagerFragmentAdatper(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerFragmentAdatper(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }


    public ViewPagerFragmentAdatper(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
