package com.timper.bindingadapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.OnRebindCallback;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.timper.bindingadapter.collection.Utils;
import com.timper.bindingadapter.collection.AdapterReferenceCollector;
import com.timper.bindingadapter.collection.BindingCollectionAdapter;
import com.timper.bindingadapter.collection.ItemBinding;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * A {@link RecyclerView.Adapter} that binds items to layouts using the given {@link ItemBinding}.
 * If you give it an {@link ObservableList} it will also updated itself based on changes to that
 * list.
 */
public class BindingRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BindingCollectionAdapter<T> {
  private static final Object DATA_INVALIDATION = new Object();

  private ItemBinding<T>                        itemBinding;
  private WeakReferenceOnListChangedCallback<T> callback;
  private List<T>                               items;
  private LayoutInflater                        inflater;
  @Nullable
  private ItemIds<? super T>                    itemIds;
  @Nullable
  private ViewHolderFactory                     viewHolderFactory;
  @Nullable
  private LifecycleOwner                        lifecycleOwner;

  // Currently attached recyclerview, we don't have to listen to notifications if null.
  @Nullable
  private RecyclerView recyclerView;

  @Override
  public void setItemBinding(ItemBinding<T> itemBinding) {
    this.itemBinding = itemBinding;
  }

  @Override
  public ItemBinding<T> getItemBinding() {
    return itemBinding;
  }

  /**
   * Sets the lifecycle owner of this adapter to work with {@link androidx.lifecycle.LiveData}.
   * This is normally not necessary, but due to an androidx limitation, you need to set this if
   * the containing view is <em>not</em> using databinding.
   */
  public void setLifecycleOwner(@Nullable LifecycleOwner lifecycleOwner) {
    this.lifecycleOwner = lifecycleOwner;
    if (recyclerView != null) {
      for (int i = 0; i < recyclerView.getChildCount(); i++) {
        View child = recyclerView.getChildAt(i);
        ViewDataBinding binding = DataBindingUtil.getBinding(child);
        if (binding != null) {
          binding.setLifecycleOwner(lifecycleOwner);
        }
      }
    }
  }

  @Override
  public void setItems(@Nullable List<T> items) {
    if (this.items == items) {
      return;
    }
    // If a recyclerview is listening, set up listeners. Otherwise wait until one is attached.
    // No need to make a sound if nobody is listening right?
    if (recyclerView != null) {
      if (this.items instanceof ObservableList) {
        ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
        callback = null;
      }
      if (items instanceof ObservableList) {
        callback = new WeakReferenceOnListChangedCallback<>(this, (ObservableList<T>) items);
        ((ObservableList<T>) items).addOnListChangedCallback(callback);
      }
    }
    this.items = items;
    notifyDataSetChanged();
  }

  @Override
  public T getAdapterItem(int position) {
    return items.get(position);
  }

  @Override
  public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
    return DataBindingUtil.inflate(inflater, layoutId, viewGroup, false);
  }

  @Override
  public void onBindBinding(ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item) {
    boolean bound = itemBinding.bind(binding, position, item);
    binding.setLifecycleOwner(lifecycleOwner);
    if (bound) {
      binding.executePendingBindings();
    }
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    if (this.recyclerView == null && items != null && items instanceof ObservableList) {
      callback = new WeakReferenceOnListChangedCallback<>(this, (ObservableList<T>) items);
      ((ObservableList<T>) items).addOnListChangedCallback(callback);
    }
    this.recyclerView = recyclerView;
    if (lifecycleOwner == null) {
      lifecycleOwner = Utils.findLifecycleOwner(recyclerView);
    }
  }

  @Override
  public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    if (this.recyclerView != null && items != null && items instanceof ObservableList) {
      ((ObservableList<T>) items).removeOnListChangedCallback(callback);
      callback = null;
    }
    this.recyclerView = null;
  }

  @Override
  public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
    if (inflater == null) {
      inflater = LayoutInflater.from(viewGroup.getContext());
    }
    ViewDataBinding binding = onCreateBinding(inflater, layoutId, viewGroup);
    final RecyclerView.ViewHolder holder = onCreateViewHolder(binding);
    //binding item itemlong click
    bindViewClickListener(holder, binding);
    binding.addOnRebindCallback(new OnRebindCallback() {
      @Override
      public boolean onPreBind(ViewDataBinding binding) {
        return recyclerView != null && recyclerView.isComputingLayout();
      }

      @Override
      public void onCanceled(ViewDataBinding binding) {
        if (recyclerView == null || recyclerView.isComputingLayout()) {
          return;
        }
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
          notifyItemChanged(position, DATA_INVALIDATION);
        }
      }
    });
    return holder;
  }

  private void bindViewClickListener(final RecyclerView.ViewHolder holder, final ViewDataBinding binding) {
    final View view = binding.getRoot();
    if (view == null) {
      return;
    }
  }

  /**
   * Constructs a view holder for the given databinding. The default implementation is to use
   * {@link ViewHolderFactory} if provided, otherwise use a default view holder.
   */
  public RecyclerView.ViewHolder onCreateViewHolder(ViewDataBinding binding) {
    if (viewHolderFactory != null) {
      return viewHolderFactory.createViewHolder(binding);
    } else {
      return new BindingViewHolder(binding);
    }
  }

  private static class BindingViewHolder extends RecyclerView.ViewHolder {
    public BindingViewHolder(ViewDataBinding binding) {
      super(binding.getRoot());
    }
  }

  @Override
  public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    T item = items.get(position);
    ViewDataBinding binding = DataBindingUtil.getBinding(viewHolder.itemView);
    onBindBinding(binding, itemBinding.variableId(), itemBinding.layoutRes(), position, item);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
    if (isForDataBinding(payloads)) {
      ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
      binding.executePendingBindings();
    } else {
      super.onBindViewHolder(holder, position, payloads);
    }
  }

  private boolean isForDataBinding(List<Object> payloads) {
    if (payloads == null || payloads.size() == 0) {
      return false;
    }
    for (int i = 0; i < payloads.size(); i++) {
      Object obj = payloads.get(i);
      if (obj != DATA_INVALIDATION) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int getItemViewType(int position) {
    itemBinding.onItemBind(position, items.get(position));
    return itemBinding.layoutRes();
  }

  /**
   * Set the item id's for the items. If not null, this will set {@link
   * RecyclerView.Adapter#setHasStableIds(boolean)} to true.
   */
  public void setItemIds(@Nullable ItemIds<? super T> itemIds) {
    if (this.itemIds != itemIds) {
      this.itemIds = itemIds;
      setHasStableIds(itemIds != null);
    }
  }

  /**
   * Set the factory for creating view holders. If null, a default view holder will be used. This
   * is useful for holding custom state in the view holder or other more complex customization.
   */
  public void setViewHolderFactory(@Nullable ViewHolderFactory factory) {
    viewHolderFactory = factory;
  }

  @Override
  public int getItemCount() {
    return items == null ? 0 : items.size();
  }

  @Override
  public long getItemId(int position) {
    return itemIds == null ? position : itemIds.getItemId(position, items.get(position));
  }

  private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    final WeakReference<BindingRecyclerViewAdapter<T>> adapterRef;

    WeakReferenceOnListChangedCallback(BindingRecyclerViewAdapter<T> adapter, ObservableList<T> items) {
      this.adapterRef = AdapterReferenceCollector.createRef(adapter, items, this);
    }

    @Override
    public void onChanged(ObservableList sender) {
      BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
      if (adapter == null) {
        return;
      }
      Utils.ensureChangeOnMainThread();
      adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList sender, final int positionStart, final int itemCount) {
      BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
      if (adapter == null) {
        return;
      }
      Utils.ensureChangeOnMainThread();
      adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableList sender, final int positionStart, final int itemCount) {
      BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
      if (adapter == null) {
        return;
      }
      Utils.ensureChangeOnMainThread();
      adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList sender, final int fromPosition, final int toPosition, final int itemCount) {
      BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
      if (adapter == null) {
        return;
      }
      Utils.ensureChangeOnMainThread();
      for (int i = 0; i < itemCount; i++) {
        adapter.notifyItemMoved(fromPosition + i, toPosition + i);
      }
    }

    @Override
    public void onItemRangeRemoved(ObservableList sender, final int positionStart, final int itemCount) {
      BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
      if (adapter == null) {
        return;
      }
      Utils.ensureChangeOnMainThread();
      if (itemCount > 1) {
        adapter.notifyDataSetChanged();
        return;
      }
      adapter.notifyItemRangeRemoved(positionStart, itemCount);
      adapter.notifyItemRangeChanged(positionStart, sender.size() - positionStart);
    }
  }

  public interface ItemIds<T> {
    long getItemId(int position, T item);
  }

  public interface ViewHolderFactory {
    RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding);
  }

  ///**
  // * Interface definition for a callback to be invoked when an item in this
  // * RecyclerView itemView has been clicked.
  // */
  //public interface OnItemClickListener {
  //
  //  /**
  //   * Callback method to be invoked when an item in this RecyclerView has
  //   * been clicked.
  //   *
  //   * @param adapter the adpater
  //   * @param view The itemView within the RecyclerView that was clicked (this
  //   * will be a view provided by the adapter)
  //   * @param position The position of the view in the adapter.
  //   */
  //  void onItemClick(RecyclerView.Adapter adapter, View view, int position);
  //}
  //
  ///**
  // * Interface definition for a callback to be invoked when an item in this
  // * view has been clicked and held.
  // */
  //public interface OnItemLongClickListener {
  //  /**
  //   * callback method to be invoked when an item in this view has been
  //   * click and held
  //   *
  //   * @param adapter the adpater
  //   * @param view The view whihin the RecyclerView that was clicked and held.
  //   * @param position The position of the view int the adapter
  //   * @return true if the callback consumed the long click ,false otherwise
  //   */
  //  boolean onItemLongClick(RecyclerView.Adapter adapter, View view, int position);
  //}
}
