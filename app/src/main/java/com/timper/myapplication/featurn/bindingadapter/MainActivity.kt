package com.timper.myapplication.featurn.bindingadapter

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.timper.bindingadapter.collection.itembindings.OnItemBindClass
import com.timper.bindingadapter.collection.itembindings.OnItemBindEvent
import com.timper.myapplication.BR
import com.timper.myapplication.R
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

  /**
   * drawerBinding
   */
  var itemBinding: OnItemBindClass<String> =
    OnItemBindClass<String>().map(String::class.java, OnItemBindEvent<String>(BR.viewModel,
      R.layout.item_main))

  var datas = ArrayList<String>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_main)
    var binding = DataBindingUtil.setContentView<com.timper.myapplication.databinding.ActMainBinding>(this,
      R.layout.act_main);
    binding.view = this

//    datas.add(ItemMainViewModel())
//    datas.add(ItemMainViewModel())
//    datas.add(ItemMainViewModel())
//    datas.add(ItemMainViewModel())
//    datas.add(ItemMainViewModel())
//    datas.add(ItemMainViewModel())
//    datas.add(ItemMainViewModel())

    for (i in 0..9){
      datas.add("i")
    }
  }
}
