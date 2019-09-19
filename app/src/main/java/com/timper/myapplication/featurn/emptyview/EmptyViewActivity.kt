package com.timper.myapplication.featurn.emptyview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.timper.myapplication.R
import com.timper.view.emptyview.EmptyLayout

class EmptyViewActivity : AppCompatActivity() {

  val emptyLayout by lazy {
    findViewById<EmptyLayout>(R.id.empty_layout)
  }

  val btnChange by lazy {
    findViewById<Button>(R.id.btn_change)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.act_empty_view)


    btnChange.setText(emptyLayout.status.name)
  }

  fun changeStaus(view: View) {
    when (emptyLayout.status) {
      EmptyLayout.Status.EMPTY -> {
        emptyLayout.status = EmptyLayout.Status.NO_NETWORK
      }
      EmptyLayout.Status.NO_NETWORK -> {
        emptyLayout.status = EmptyLayout.Status.TRANSPARENT_REFRESH
      }
      EmptyLayout.Status.TRANSPARENT_REFRESH -> {
        emptyLayout.status = EmptyLayout.Status.REFRESH
      }
      EmptyLayout.Status.REFRESH -> {
        emptyLayout.status = EmptyLayout.Status.NONE
      }
      EmptyLayout.Status.NONE -> {
        emptyLayout.status = EmptyLayout.Status.ERROR
      }
      EmptyLayout.Status.ERROR -> {
        emptyLayout.status = EmptyLayout.Status.EMPTY
      }
    }

    btnChange.setText(emptyLayout.status.name)
  }
}
