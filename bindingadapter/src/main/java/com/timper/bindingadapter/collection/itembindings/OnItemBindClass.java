package com.timper.bindingadapter.collection.itembindings;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import com.timper.bindingadapter.collection.BindingListViewAdapter;
import com.timper.bindingadapter.collection.ItemBinding;
import com.timper.bindingadapter.collection.OnItemBind;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link OnItemBind} that selects an item view based on the class of the given item.
 * <pre>{@code
 * itemBind = new OnItemBindClass<>()
 *     .map(String.class, BR.name, R.layout.item_name)
 *     .map(Footer.class, ItemBinding.VAR_NONE, R.layout.item_footer);
 * }</pre>
 */
public class OnItemBindClass<T> implements OnItemBind<T> {

  private final Map<Class<? extends T>, OnItemBind<? extends T>> itemBindingMap;

  public OnItemBindClass() {
    itemBindingMap = new HashMap<>();
  }

  /**
   * Maps the given class to the given variableId and layout. This is assignment-compatible match with the object represented by
   * Class.
   */
  public OnItemBindClass<T> map(@NonNull Class<? extends T> itemClass, final int variableId, @LayoutRes final int layoutRes) {
    itemBindingMap.put(itemClass, itemBind(variableId, layoutRes));
    return this;
  }

  /**
   * Maps the given class to the given {@link OnItemBind}. This is assignment-compatible match with the object represented by
   * Class.
   */
  public <E extends T> OnItemBindClass<T> map(@NonNull Class<E> itemClass, OnItemBind<E> onItemBind) {
    itemBindingMap.put(itemClass, onItemBind);
    return this;
  }

  /**
   * Returns the number of item types in the map. This is useful for {@link
   * BindingListViewAdapter#BindingListViewAdapter(int)} or {@code app:itemTypeCount} in an {@code
   * AdapterView}.
   */
  public int itemTypeCount() {
    return itemBindingMap.size();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onItemBindView(ItemBinding itemBinding, int position, T item) {
    if (itemBindingMap.containsKey(item.getClass())) {
      OnItemBind itemBind = itemBindingMap.get(item.getClass());
      itemBind.onItemBindView(itemBinding, position, item);
      return;
    } else {
      throw new IllegalArgumentException("Missing class for item " + item);
    }
  }

  @Override
  public OnItemBind<T> onItemClickBind(ItemBinding.OnItemClickListener onItemClickListener) {
    return null;
  }

  @Override
  public OnItemBind<T> onItemBind(ItemBinding.OnItemBindListener<T> onItemBindListener) {
    return null;
  }

  @Override
  public OnItemBind<T> onItemChildClickBind(int resId, ItemBinding.OnItemChildClickListener onItemChildClick) {
    return null;
  }

  @Override
  public ItemBinding.OnItemClickListener getOnItemClick(T item) {
    if (itemBindingMap.containsKey(item.getClass())) {
      OnItemBind itemBind = itemBindingMap.get(item.getClass());
      return itemBind.getOnItemClick(item);
    } else {
      throw new IllegalArgumentException("Missing class for item " + item);
    }
  }

  @Override
  public ItemBinding.OnItemBindListener<T> getOnItemBindListener(T item) {
    if (itemBindingMap.containsKey(item.getClass())) {
      OnItemBind itemBind = itemBindingMap.get(item.getClass());
      return itemBind.getOnItemBindListener(item);
    } else {
      throw new IllegalArgumentException("Missing class for item " + item);
    }
  }

  @Override
  public Map<Integer, ItemBinding.OnItemChildClickListener> getOnItemChildClick(T item) {
    if (itemBindingMap.containsKey(item.getClass())) {
      OnItemBind itemBind = itemBindingMap.get(item.getClass());
      return itemBind.getOnItemChildClick(item);
    } else {
      throw new IllegalArgumentException("Missing class for item " + item);
    }
  }

  @NonNull
  private OnItemBind<T> itemBind(final int variableId, @LayoutRes final int layoutRes) {
    return new OnItemBind<T>() {
      @Override
      public void onItemBindView(ItemBinding itemBinding, int position, T item) {
        itemBinding.set(variableId, layoutRes);
      }

      @Override
      public OnItemBind<T> onItemClickBind(ItemBinding.OnItemClickListener onItemClickListener) {
        return null;
      }

      @Override
      public OnItemBind<T> onItemBind(ItemBinding.OnItemBindListener<T> onItemBindListener) {
        return null;
      }

      @Override
      public OnItemBind<T> onItemChildClickBind(int resId, ItemBinding.OnItemChildClickListener onItemChildClick) {
        return null;
      }

      @Override
      public ItemBinding.OnItemClickListener getOnItemClick(T item) {
        return null;
      }

      @Override
      public ItemBinding.OnItemBindListener<T> getOnItemBindListener(T item) {
        return null;
      }

      @Override
      public Map<Integer, ItemBinding.OnItemChildClickListener> getOnItemChildClick(T item) {
        return null;
      }
    };
  }
}
