package com.timper.bindingadapter.textview;

import android.text.method.MovementMethod;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;

public final class ViewBindingAdapter {

  @BindingAdapter({"movementMethod"})
  public static void setMovementMethod(TextView textView, final MovementMethod movementMethod) {
    textView.setMovementMethod(movementMethod);
  }
}

