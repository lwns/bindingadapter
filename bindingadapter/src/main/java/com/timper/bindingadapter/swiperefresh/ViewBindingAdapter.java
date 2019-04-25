package com.timper.bindingadapter.swiperefresh;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import com.timper.bindingadapter.action.Command;

public class ViewBindingAdapter {
  @BindingAdapter({ "onRefresh" })
  public static void onRefreshCommand(SwipeRefreshLayout swipeRefreshLayout, final Command command) {
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        if (command != null) {
          command.execute();
        }
      }
    });
  }

  @BindingAdapter(value = { "refresh" }, requireAll = false)
  public static void setRefresh(final SwipeRefreshLayout swipeRefreshLayout, final Boolean refresh) {
    swipeRefreshLayout.setRefreshing(refresh);
  }

  @BindingAdapter(value = { "colorScheme" }, requireAll = false)
  public static void setColorSchemeResources(final SwipeRefreshLayout swipeRefreshLayout, final @ColorRes int... colorResIds) {
    swipeRefreshLayout.setColorSchemeResources(colorResIds);
  }
}
