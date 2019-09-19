package com.timper.bindingadapter.action;

/**
 * User: tangpeng.yang
 * Date: 20/03/2018
 * Description:
 * FIXME
 */
public class ParamCommand<T> {

  private Action1<T> execute1;

  private Func0<Boolean> canExecute0;

  public ParamCommand(Action1<T> execute) {
    this.execute1 = execute;
  }

  /**
   * @param execute callback for event,this callback need a params
   * @param canExecute0 if this function return true the action execute would be invoked! otherwise would't invoked!
   */
  public ParamCommand(Action1<T> execute, Func0<Boolean> canExecute0) {
    this.execute1 = execute;
    this.canExecute0 = canExecute0;
  }

  private boolean canExecute0() {
    if (canExecute0 == null) {
      return true;
    }
    return canExecute0.call();
  }

  public void execute(T parameter) {
    if (execute1 != null && canExecute0()) {
      execute1.call(parameter);
    }
  }
}
