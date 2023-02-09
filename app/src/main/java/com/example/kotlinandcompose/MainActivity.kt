package com.example.kotlinandcompose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinandcompose.Flow.FlowActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter:MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
        initListener()
    }

    private fun initListener() {
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as MainBean
            when(bean.id){
                1 -> {
                    startActivity(Intent(this,FlowActivity::class.java))
                }
                2 ->{

                }
            }
        }
    }

    private fun initData() {
        val mData = ArrayList<MainBean>()
        mData.add(MainBean("Flow的使用",1))
        mAdapter.setList(mData)
    }

    private fun initView() {
        rv.layoutManager = LinearLayoutManager(this)
        mAdapter = MainAdapter()
        rv.adapter = mAdapter
    }
}