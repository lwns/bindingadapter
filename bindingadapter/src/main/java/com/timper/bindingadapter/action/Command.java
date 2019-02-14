package com.timper.bindingadapter.action;

/**
 * User: tangpeng.yang
 * Date: 20/03/2018
 * Description:
 * FIXME
 */
public class Command {

  private Action0        execute0;
  private Func0<Boolean> canExecute0;

  public Command(Action0 execute) {
    this.execute0 = execute;
  }

  /**
   * @param execute callback for event
   * @param canExecute0 if this function return true the action execute would be invoked! otherwise would't invoked!
   */
  public Command(Action0 execute, Func0<Boolean> canExecute0) {
    this.execute0 = execute;
    this.canExecute0 = canExecute0;
  }

  public void execute() {
    if (execute0 != null && canExecute0()) {
      execute0.call();
    }
  }

  private boolean canExecute0() {
    if (canExecute0 == null) {
      return true;
    }
    return canExecute0.call();
  }
}
