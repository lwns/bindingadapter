package com.timper.bindingadapter.viewpager;

import android.databinding.BindingAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import com.timper.bindingadapter.action.ParamCommand;
import java.util.ArrayList;
import java.util.List;

public class ViewBindingAdapter {

  @BindingAdapter(value = { "frgManager", "fragments", "titles" }, requireAll = false)
  public static void init(final ViewPager viewPager, final FragmentManager fragmentManager, List<Fragment> items,
      List<String> titles) {
    if (items == null) {
      items = new ArrayList<>();
    }

    PagerAdapter adatper = viewPager.getAdapter();
    if (adatper == null) {
      if (titles == null) {
        adatper = new ViewPagerFragmentAdatper(fragmentManager, items);
      } else {
        adatper = new ViewPagerFragmentAdatper(fragmentManager, items, titles);
      }
      viewPager.setAdapter(adatper);
    }
  }

  @BindingAdapter(value = { "selectPosition" }, requireAll = false)
  public static void setIndex(final ViewPager viewPager, final Integer position) {
    if (viewPager != null && viewPager.getAdapter() != null && position >= 0) {
      viewPager.setCurrentItem(position);
    }
  }

  @BindingAdapter(value = { "pageLimit" }, requireAll = false)
  public static void setPageLimit(final ViewPager viewPager, final Integer pageLimit) {
    if (viewPager != null) {
      viewPager.setOffscreenPageLimit(pageLimit);
    }
  }

  @BindingAdapter(value = {
      "onPageScrolledCommand", "onPageSelectedCommand", "onPageScrollStateChangedCommand"
  }, requireAll = false)
  public static void onScrollChangeCommand(final ViewPager viewPager, final ParamCommand<ViewPagerDataWrapper> onPageScrolledCommand,
      final ParamCommand<Integer> onPageSelectedCommand, final ParamCommand<Integer> onPageScrollStateChangedCommand) {
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      private int state;

      @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (onPageScrolledCommand != null) {
          onPageScrolledCommand.execute(new ViewPagerDataWrapper(position, positionOffset, positionOffsetPixels, state));
        }
      }

      @Override public void onPageSelected(int position) {
        if (onPageSelectedCommand != null) {
          onPageSelectedCommand.execute(position);
        }
      }

      @Override public void onPageScrollStateChanged(int state) {
        this.state = state;
        if (onPageScrollStateChangedCommand != null) {
          onPageScrollStateChangedCommand.execute(state);
        }
      }
    });
  }

  public static class ViewPagerDataWrapper {
    public float positionOffset;
    public float position;
    public int positionOffsetPixels;
    public int state;

    public ViewPagerDataWrapper(float position, float positionOffset, int positionOffsetPixels, int state) {
      this.positionOffset = positionOffset;
      this.position = position;
      this.positionOffsetPixels = positionOffsetPixels;
      this.state = state;
    }
  }
}
