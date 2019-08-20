package com.timper.bindingadapter.recyclerview;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.timper.bindingadapter.R;
import com.timper.bindingadapter.action.ParamCommand;
import com.timper.bindingadapter.collection.ItemBinding;
import com.timper.bindingadapter.recyclerview.collections.AsyncDiffObservableList;
import io.reactivex.subjects.PublishSubject;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 */
public class BindingRecyclerViewAdapters {

  // RecyclerView
  @SuppressWarnings("unchecked")
  @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder", "diffConfig"}, requireAll = false)
  public static <T> void setAdapter(RecyclerView recyclerView, ItemBinding<T> itemBinding, List<T> items, BindingRecyclerViewAdapter<T> adapter,
    BindingRecyclerViewAdapter.ItemIds<? super T> itemIds, BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory,
    AsyncDifferConfig<T> diffConfig) {
    if (itemBinding == null) {
      throw new IllegalArgumentException("itemBinding must not be null");
    }
    BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter) recyclerView.getAdapter();
    if (adapter == null) {
      if (oldAdapter == null) {
        adapter = new BindingRecyclerViewAdapter<>();
      } else {
        adapter = oldAdapter;
      }
    }
    adapter.setItemBinding(itemBinding);

    //when data change only update changes items
    if (diffConfig != null && items != null) {
      AsyncDiffObservableList<T> list = (AsyncDiffObservableList<T>) recyclerView.getTag(R.id.bindingadapter_list_id);
      if (list == null) {
        list = new AsyncDiffObservableList<>(diffConfig);
        recyclerView.setTag(R.id.bindingadapter_list_id, list);
        adapter.setItems(list);
      }
      list.update(items);
    } else {
      adapter.setItems(items);
    }

    adapter.setItemIds(itemIds);
    adapter.setViewHolderFactory(viewHolderFactory);
    if (oldAdapter != adapter) {
      recyclerView.setAdapter(adapter);
    }
  }

  @BindingConversion
  public static <T> AsyncDifferConfig<T> toAsyncDifferConfig(DiffUtil.ItemCallback<T> callback) {
    return new AsyncDifferConfig.Builder<>(callback).build();
  }

  @BindingAdapter("layoutManager")
  public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
    recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
  }

  @BindingAdapter(value = {"hasFixedSize", "scrollingEnabled"}, requireAll = false)
  public static void setIndex(final RecyclerView recyclerView, final Boolean hasFixedSize, final Boolean scrollingEnabled) {
    recyclerView.setHasFixedSize(hasFixedSize);
    recyclerView.setNestedScrollingEnabled(scrollingEnabled);
  }

  @BindingAdapter(value = {"onScrollChange", "onScrollStateChanged"}, requireAll = false)
  public static void onScrollChangeCommand(final RecyclerView recyclerView, final ParamCommand<ScrollDataWrapper> onScrollChangeCommand,
    final ParamCommand<Integer> onScrollStateChangedCommand) {
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      private int state;

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (onScrollChangeCommand != null) {
          onScrollChangeCommand.execute(new ScrollDataWrapper(dx, dy, state));
        }
      }

      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        state = newState;
        if (onScrollStateChangedCommand != null) {
          onScrollChangeCommand.equals(newState);
        }
      }
    });
  }

  @BindingAdapter(value = {"onLoadMore"}, requireAll = false)
  public static void onLoadMoreCommand(final RecyclerView recyclerView, final ParamCommand<Integer> onLoadMoreCommand) {
    recyclerView.addOnScrollListener(new OnLoadMoreScrollListener(onLoadMoreCommand));
  }

  @BindingAdapter(value = {"itemDecoration"}, requireAll = false)
  public static void addItemDecoration(final RecyclerView recyclerView, final RecyclerView.ItemDecoration decor) {
    recyclerView.addItemDecoration(decor);
  }

  public static class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {

    private PublishSubject<Integer> methodInvoke = PublishSubject.create();

    private ParamCommand<Integer> onLoadMoreCommand;

    public OnLoadMoreScrollListener(ParamCommand<Integer> onLoadMoreCommand) {
      this.onLoadMoreCommand = onLoadMoreCommand;
      methodInvoke.throttleLast(1, TimeUnit.SECONDS)
                  .subscribe(c -> {
                    onLoadMoreCommand.execute(c);
                  });
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
      if (layoutManager instanceof LinearLayoutManager) {
        LinearLayoutManager manager = ((LinearLayoutManager) layoutManager);
        int totalItemCount = manager.getItemCount();
        int pastVisiblesItems = manager.findLastVisibleItemPosition();
        if (newState == RecyclerView.SCROLL_STATE_IDLE && onLoadMoreCommand != null && (1 + pastVisiblesItems) == totalItemCount) {
          methodInvoke.onNext(recyclerView.getAdapter()
                                          .getItemCount());
        }
      }
    }
  }

  public static class ScrollDataWrapper {
    public float scrollX;
    public float scrollY;
    public int   state;

    public ScrollDataWrapper(float scrollX, float scrollY, int state) {
      this.scrollX = scrollX;
      this.scrollY = scrollY;
      this.state = state;
    }
  }

  public static class ItemDataWrapper {
    public RecyclerView.Adapter adapter;
    public View                 view;
    public int                  position;

    public ItemDataWrapper(RecyclerView.Adapter adapter, View view, int position) {
      this.adapter = adapter;
      this.view = view;
      this.position = position;
    }
  }
}
