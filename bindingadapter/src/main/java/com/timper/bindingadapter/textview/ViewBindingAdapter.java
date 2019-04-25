package com.timper.bindingadapter.textview;

import android.databinding.BindingAdapter;
import android.text.method.MovementMethod;
import android.widget.TextView;

public final class ViewBindingAdapter {

  @BindingAdapter({"movementMethod"})
  public static void setMovementMethod(TextView textView, final MovementMethod movementMethod) {
    textView.setMovementMethod(movementMethod);
  }
}

