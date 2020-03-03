package com.timper.bindingadapter.collection.itembindings;

import androidx.annotation.LayoutRes;
import com.timper.bindingadapter.collection.ItemBinding;
import com.timper.bindingadapter.collection.OnItemBind;
import java.util.HashMap;
import java.util.Map;

/**
 * User: tangpeng.yang
 * Date: 26/03/2018
 * Description:
 * FIXME
 */
public class OnItemBindEvent<T> implements OnItemBind<T> {

  protected ItemBinding.OnItemClickListener                    onItemClickListener;
  protected ItemBinding.OnItemBindListener<T>                  onItemBindListener;
  protected Map<Integer, ItemBinding.OnItemChildClickListener> map;
  protected int                                                variableId;
  protected @LayoutRes
  int layoutRes;

  public OnItemBindEvent(int variableId, @LayoutRes int layoutRes) {
    this.variableId = variableId;
    this.layoutRes = layoutRes;
  }

  @Override
  public void onItemBindView(ItemBinding itemBinding, int position, T item) {
    itemBinding.set(variableId, layoutRes);
  }

  @Override
  public OnItemBindEvent<T> onItemClickBind(ItemBinding.OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
    return this;
  }

  @Override
  public OnItemBindEvent<T> onItemBind(ItemBinding.OnItemBindListener<T> onItemBindListener) {
    this.onItemBindListener = onItemBindListener;
    return this;
  }

  @Override
  public OnItemBindEvent<T> onItemChildClickBind(int resId, ItemBinding.OnItemChildClickListener onItemChildClick) {
    if (map == null) {
      map = new HashMap<>();
    }
    map.put(resId, onItemChildClick);
    return this;
  }

  @Override
  public ItemBinding.OnItemClickListener getOnItemClick(T item) {
    return onItemClickListener;
  }

  @Override
  public ItemBinding.OnItemBindListener<T> getOnItemBindListener(T item) {
    return onItemBindListener;
  }

  @Override
  public Map<Integer, ItemBinding.OnItemChildClickListener> getOnItemChildClick(T item) {
    return map;
  }
}
