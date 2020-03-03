package com.timper.bindingadapter.collection;

import androidx.databinding.ViewDataBinding;
import androidx.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.View;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides the necessary information to bind an item in a collection to a view. This includes the
 * variable id and the layout as well as any extra bindings you may want to provide.
 *
 * @param <T> The item type.
 */
public final class ItemBinding<T> {

  /**
   * Use this constant as the variable id to not bind the item in the collection to the layout if
   * no data is need, like a static footer or loading indicator.
   */
  public static final  int VAR_NONE    = 0;
  private static final int VAR_INVALID = -1;
  private static final int LAYOUT_NONE = 0;

  /**
   * Constructs an instance with the given variable id and layout.
   */
  public static <T> ItemBinding<T> of(int variableId, @LayoutRes int layoutRes) {
    return new ItemBinding<T>(null).set(variableId, layoutRes);
  }

  /**
   * Constructs an instance with the given callback. It will be called for each item in the
   * collection to set the binding info.
   *
   * @see OnItemBind
   */
  public static <T> ItemBinding<T> of(OnItemBind<T> onItemBind) {
    if (onItemBind == null) {
      throw new NullPointerException("onItemBind == null");
    }
    return new ItemBinding<>(onItemBind);
  }

  private final OnItemBind<T>                          onItemBind;
  private       int                                    variableId;
  @LayoutRes
  private       int                                    layoutRes;
  private       OnItemClickListener                    onItemClick;
  private       Map<Integer, OnItemChildClickListener> onItemChildClickMap;
  private       OnItemBindListener<T>                  onItemBindListener;
  private       SparseArray<Object>                    extraBindings;

  private ItemBinding(OnItemBind<T> onItemBind) {
    this.onItemBind = onItemBind;
  }

  /**
   * Set the variable id and layout. This is normally called in {@link
   * OnItemBind#onItemBindView(ItemBinding, int, Object)}.
   */
  public final ItemBinding<T> set(int variableId, @LayoutRes int layoutRes) {
    this.variableId = variableId;
    this.layoutRes = layoutRes;
    return this;
  }

  /**
   * Set the variable id. This is normally called in
   */
  public final ItemBinding<T> variableId(int variableId) {
    this.variableId = variableId;
    return this;
  }

  /**
   * Set the layout. This is normally called in
   */
  public final ItemBinding<T> layoutRes(@LayoutRes int layoutRes) {
    this.layoutRes = layoutRes;
    return this;
  }

  /**
   * Bind an extra variable to the view with the given variable id. The same instance will be
   * provided to all views the binding is bound to.
   */
  public final ItemBinding<T> bindExtra(int variableId, Object value) {
    if (extraBindings == null) {
      extraBindings = new SparseArray<>(1);
    }
    extraBindings.put(variableId, value);
    return this;
  }

  /**
   * Clear all extra variables. This is normally called in {@link
   * OnItemBind#onItemBindView(ItemBinding, int, Object)}.
   */
  public final ItemBinding<T> clearExtras() {
    if (extraBindings != null) {
      extraBindings.clear();
    }
    return this;
  }

  /**
   * Remove an extra variable with the given variable id. This is normally called in {@link
   * OnItemBind#onItemBindView(ItemBinding, int, Object)}.
   */
  public ItemBinding<T> removeExtra(int variableId) {
    if (extraBindings != null) {
      extraBindings.remove(variableId);
    }
    return this;
  }

  public ItemBinding<T> set(OnItemClickListener onItemClick) {
    this.onItemClick = onItemClick;
    return this;
  }

  public ItemBinding<T> set(OnItemBindListener onItemBindListener) {
    this.onItemBindListener = onItemBindListener;
    return this;
  }

  public ItemBinding<T> set(int resId, OnItemChildClickListener onItemChildClick) {
    if (this.onItemChildClickMap == null) {
      this.onItemChildClickMap = new HashMap<>();
    }
    if (!this.onItemChildClickMap.containsKey(resId)) {
      this.onItemChildClickMap.put(resId, onItemChildClick);
    }
    return this;
  }

  public ItemBinding<T> set(Map<Integer, OnItemChildClickListener> onItemChildClickMap) {
    if (this.onItemChildClickMap == null) {
      this.onItemChildClickMap = new HashMap<>();
    }
    this.onItemChildClickMap.clear();
    if (onItemChildClickMap != null) {
      this.onItemChildClickMap.putAll(onItemChildClickMap);
    }
    return this;
  }

  public interface OnItemBindListener<T> {
    void OnItemBind(ViewDataBinding binding, int position, T item);
  }

  /**
   * Interface definition for a callback to be invoked when an itemchild in this
   * view has been clicked
   */
  public interface OnItemChildClickListener {
    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view The view whihin the ItemView that was clicked
     * @param position The position of the view int the adapter
     */
    void onItemChildClick(View view, ViewDataBinding binding, int position);
  }

  /**
   * Interface definition for a callback to be invoked when an item in this
   * RecyclerView itemView has been clicked.
   */
  public interface OnItemClickListener {

    /**
     * Callback method to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param view The itemView within the RecyclerView that was clicked (this
     * will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    void onItemClick(View view, ViewDataBinding binding, int position);
  }

  /**
   * Returns the current variable id of this binding.
   */
  public final int variableId() {
    return variableId;
  }

  /**
   * Returns the current layout fo this binding.
   */
  @LayoutRes
  public final int layoutRes() {
    return layoutRes;
  }

  /**
   * Returns the current extra binding for the given variable id or null if one isn't present.
   */
  public final Object extraBinding(int variableId) {
    if (extraBindings == null) {
      return null;
    }
    return extraBindings.get(variableId);
  }

  /**
   * Updates the state of the binding for the given item and position. This is called internally
   * by the binding collection adapters.
   */
  public void onItemBind(int position, T item) {
    if (onItemBind != null) {
      variableId = VAR_INVALID;
      layoutRes = LAYOUT_NONE;
      onItemBind.onItemBindView(this, position, item);
      if (variableId == VAR_INVALID) {
        throw new IllegalStateException("variableId not set in onItemBind()");
      }
      if (layoutRes == LAYOUT_NONE) {
        throw new IllegalStateException("layoutRes not set in onItemBind()");
      }
    }
  }

  /**
   * Binds the item and extra bindings to the given binding. Returns true if anything was bound
   * and false otherwise. This is called internally by the binding collection adapters.
   *
   * @throws IllegalStateException if the variable id isn't present in the layout.
   */
  public boolean bind(final ViewDataBinding binding, final int position, T item) {
    if (variableId == VAR_NONE) {
      return false;
    }
    if (onItemBind != null) {
      if (onItemBind.getOnItemClick(item) != null) {
        binding.getRoot()
               .setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                   onItemBind.getOnItemClick(item)
                             .onItemClick(v, binding, position);
                 }
               });
      }

      if (onItemBind.getOnItemBindListener(item) != null) {
        onItemBind.getOnItemBindListener(item)
                  .OnItemBind(binding, position, item);
      }
      if (onItemBind.getOnItemChildClick(item) != null && onItemBind.getOnItemChildClick(item)
                                                                    .size() > 0) {
        for (final Map.Entry<Integer, OnItemChildClickListener> entry : onItemBind.getOnItemChildClick(item)
                                                                                  .entrySet()) {
          View view = binding.getRoot()
                             .findViewById(entry.getKey());
          if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                entry.getValue()
                     .onItemChildClick(v, binding, position);
              }
            });
          }
        }
      }
    } else {
      if (onItemClick != null) {
        binding.getRoot()
               .setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                   onItemClick.onItemClick(v, binding, position);
                 }
               });
      }
      if (onItemBindListener != null) {
        onItemBindListener.OnItemBind(binding, position, item);
      }
      if (onItemChildClickMap != null && onItemChildClickMap.size() > 0) {
        for (final Map.Entry<Integer, OnItemChildClickListener> entry : onItemChildClickMap.entrySet()) {
          View view = binding.getRoot()
                             .findViewById(entry.getKey());
          if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                entry.getValue()
                     .onItemChildClick(v, binding, position);
              }
            });
          }
        }
      }
    }
    boolean result = binding.setVariable(variableId, item);
    if (!result) {
      Utils.throwMissingVariable(binding, variableId, layoutRes);
    }
    if (extraBindings != null) {
      for (int i = 0, size = extraBindings.size(); i < size; i++) {
        int variableId = extraBindings.keyAt(i);
        Object value = extraBindings.valueAt(i);
        if (variableId != VAR_NONE) {
          binding.setVariable(variableId, value);
        }
      }
    }
    return true;
  }
}
