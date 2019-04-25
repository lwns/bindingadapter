package com.timper.bindingadapter.collection;

import android.support.annotation.IdRes;
import java.util.Map;

/**
 * Callback for setting up a {@link ItemBinding} for an item in the collection.
 *
 * @param <T> the item type
 */
public interface OnItemBind<T> {
  /**
   * Called on each item in the collection, allowing you to modify the given {@link ItemBinding}.
   * Note that you should not do complex processing in this method as it's called many times.
   */
  void onItemBindView(ItemBinding itemBinding, int position, T item);

  OnItemBind<T> onItemClickBind(ItemBinding.OnItemClickListener onItemClickListener);

  OnItemBind<T> onItemBind(ItemBinding.OnItemBindListener<T> onItemBindListener);

  OnItemBind<T> onItemChildClickBind(@IdRes int resId, ItemBinding.OnItemChildClickListener onItemChildClick);

  ItemBinding.OnItemClickListener getOnItemClick(T item);

  ItemBinding.OnItemBindListener<T> getOnItemBindListener(T item);

  Map<Integer, ItemBinding.OnItemChildClickListener> getOnItemChildClick(T item);
}
