package com.timper.bindingadapter.action;

/**
 * User: tangpeng.yang
 * Date: 20/03/2018
 * Description:
 * FIXME
 */
public interface Action1<T> extends Action {
  void call(T t);
}
