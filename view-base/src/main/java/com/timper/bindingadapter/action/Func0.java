package com.timper.bindingadapter.action;

import java.util.concurrent.Callable;

/**
 * User: tangpeng.yang
 * Date: 20/03/2018
 * Description:
 * FIXME
 */
public interface Func0<R> extends Function, Callable<R> {
  @Override R call();
}
