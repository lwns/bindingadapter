package com.timper.bindingadapter.collection;

import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * @author op
 * @version 1.0
 */
public class BindingLoopViewPagerAdapter<T> extends BindingViewPagerAdapter<T> {

  @Override public int getCount() {
    return items == null ? 0 : Integer.MAX_VALUE;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    if (inflater == null) {
      inflater = LayoutInflater.from(container.getContext());
    }

    position %= items.size();
    T item = items.get(position);
    itemBinding.onItemBind(position, item);

    ViewDataBinding binding = onCreateBinding(inflater, itemBinding.layoutRes(), container);
    onBindBinding(binding, itemBinding.variableId(), itemBinding.layoutRes(), position, item);

    //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
    ViewParent vp = binding.getRoot().getParent();
    if (vp != null) {
      ViewGroup parent = (ViewGroup) vp;
      parent.removeView(binding.getRoot());
    }
    container.addView(binding.getRoot());
    binding.getRoot().setTag(item);
    return binding.getRoot();
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    if (object instanceof View) {
      container.removeView((View) object);
    }
  }
}
