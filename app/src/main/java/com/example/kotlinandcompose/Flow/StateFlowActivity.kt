package com.example.kotlinandcompose.Flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinandcompose.MainAdapter
import com.example.kotlinandcompose.MainBean
import com.example.kotlinandcompose.R
import kotlinx.android.synthetic.main.activity_main.*

class StateFlowActivity:AppCompatActivity() {
    private lateinit var mAdapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_flow)
        initView()
        initData()
        initListener()
    }

    private fun initListener() {
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as MainBean
            when(bean.id){
                1 -> {

                }
                2 ->{

                }
                3 ->{

                }
                4 ->{

                }
            }
        }
    }

    private fun initData() {
        val mData = ArrayList<MainBean>()
        mData.add(MainBean("Flow的使用创建",1))
        mData.add(MainBean("FlowOn的使用创建",2))
        mData.add(MainBean("过度操作符",3))
        mData.add(MainBean("zip与combine的区别",4))
        mAdapter.setList(mData)
    }

    private fun initView() {
        rv.layoutManager = LinearLayoutManager(this)
        mAdapter = MainAdapter()
        rv.adapter = mAdapter
    }
}