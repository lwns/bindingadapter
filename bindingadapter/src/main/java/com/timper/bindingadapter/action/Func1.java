package com.timper.bindingadapter.action;

/**
 * User: tangpeng.yang
 * Date: 20/03/2018
 * Description:
 * FIXME
 */
public interface Func1<T, R> extends Function {
  R call(T t);
}
