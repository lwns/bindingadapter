package com.timper.myapplication.featurn.loading

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.timper.myapplication.R
import com.timper.util.logger.Log4a

class LoadingActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.act_loading)
    Log4a.i("ytp","我是日志成功了")
  }
}
