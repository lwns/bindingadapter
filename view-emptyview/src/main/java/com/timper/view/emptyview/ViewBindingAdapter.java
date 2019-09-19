package com.timper.view.emptyview;

import android.databinding.BindingAdapter;
import com.timper.bindingadapter.action.Command;

public final class ViewBindingAdapter {

  @BindingAdapter({"onRetry"})
  public static void retry(EmptyLayout emptyLayout, Command command) {
    emptyLayout.setOnRefreshListener(() -> {
      if (command != null) {
        command.execute();
      }
    });
  }

  @BindingAdapter(value = {"state"}, requireAll = false)
  public static void setStatus(EmptyLayout emptyLayout, EmptyLayout.Status status) {
    if (status != null) {
      emptyLayout.setStatus(status);
    }
  }

  @BindingAdapter({"emptyInfo"})
  public static void setEmptyInfo(EmptyLayout emptyLayout, String emptyInfo) {
    emptyLayout.setInfo(emptyInfo);
  }
}

