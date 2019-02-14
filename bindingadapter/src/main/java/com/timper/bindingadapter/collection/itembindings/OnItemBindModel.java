package com.timper.bindingadapter.collection.itembindings;

import com.timper.bindingadapter.collection.ItemBinding;

/**
 * An {@link OnItemBindClass} that selects item views by delegating to each item. Items must implement
 * {@link ItemBindingModel}.
 */
public class OnItemBindModel<T extends ItemBindingModel> extends OnItemBindEvent<T> {

  public OnItemBindModel(int variableId, int layoutRes) {
    super(variableId, layoutRes);
  }

  @Override
  public void onItemBindView(ItemBinding itemBinding, int position, T item) {
    super.onItemBindView(itemBinding, position, item);
    item.onItemBind(itemBinding);
  }
}
