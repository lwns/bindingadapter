package com.timper.myapplication

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.timper.bindingadapter.collection.itembindings.OnItemBindClass
import com.timper.bindingadapter.collection.itembindings.OnItemBindEvent
import com.timper.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  /**
   * drawerBinding
   */
  var itemBinding: OnItemBindClass<ItemMainViewModel> =
    OnItemBindClass<ItemMainViewModel>().map(ItemMainViewModel::class.java, OnItemBindEvent<ItemMainViewModel>(BR.viewModel, R.layout.item_main))

  var datas = ArrayList<ItemMainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main);
    binding.view = this

    datas.add(ItemMainViewModel())
    datas.add(ItemMainViewModel())
    datas.add(ItemMainViewModel())
    datas.add(ItemMainViewModel())
    datas.add(ItemMainViewModel())
    datas.add(ItemMainViewModel())
    datas.add(ItemMainViewModel())
  }
}
