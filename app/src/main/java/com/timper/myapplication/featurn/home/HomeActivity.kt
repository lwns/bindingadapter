package com.timper.myapplication.featurn.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.timper.myapplication.R
import com.timper.myapplication.featurn.bindingadapter.MainActivity
import com.timper.myapplication.featurn.emptyview.EmptyViewActivity
import com.timper.myapplication.featurn.loading.LoadingActivity

class HomeActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.act_home)
  }

  fun toBindingadapter(view: View) {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
  }

  fun toEmptyView(view: View) {
    val intent = Intent(this, EmptyViewActivity::class.java)
    startActivity(intent)
  }

  fun toLoadingView(view: View) {
    val intent = Intent(this, LoadingActivity::class.java)
    startActivity(intent)
  }
}
