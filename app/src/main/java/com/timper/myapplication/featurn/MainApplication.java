package com.timper.myapplication.featurn;

import android.app.Application;

/**
 * User: tangpeng.yang
 * Date: 2019-10-21
 * Description:
 * FIXME
 */
public class MainApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    LogInit.init(this);
  }
}
