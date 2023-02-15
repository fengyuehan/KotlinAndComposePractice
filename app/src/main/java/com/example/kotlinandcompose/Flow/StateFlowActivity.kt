package com.example.kotlinandcompose.Flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinandcompose.MainAdapter
import com.example.kotlinandcompose.MainBean
import com.example.kotlinandcompose.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StateFlowActivity:AppCompatActivity() {
    val liveData = MutableLiveData<Int>()
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
                    shareFlowTest()
                }
                2 ->{
                    stateFlowTest()
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
        mData.add(MainBean("shareFlow的使用创建",1))
        mData.add(MainBean("stateFlow的使用创建",2))
        mData.add(MainBean("过度操作符",3))
        mData.add(MainBean("zip与combine的区别",4))
        mAdapter.setList(mData)
    }

    private fun initView() {
        rv.layoutManager = LinearLayoutManager(this)
        mAdapter = MainAdapter()
        rv.adapter = mAdapter
    }

    /**
     * 没有日志打印 是因为MutableSharedFlow的
     * replay: Int = 0,
     * extraBufferCapacity: Int = 0,
     * replay：指的是观察者注册后，能够收到多少个之前的通知，默认是为 0。
     * extraBufferCapacity：指的是当被观察者发送速度过快，或者观察者消化消息的速度过慢的时候，数据就会临时存储起来，而这里指的就是额外存储的个数，注意，是额外存储的个数，实际存储的个数是 replay + extraBufferCapacity。
     * onBufferOverflow：由于 MutableSharedFlow 的存储空间是固定的，有可能会被超过限制，而 onBufferOverflow 则是标识当超过这个限制的时候，怎么进行处理，目前有三种选项：

        BufferOverflow.SUSPEND：挂起，默认实现。
        BufferOverflow.DROP_OLDEST：丢弃最老的值，保留最新的。
        BufferOverflow.DROP_LATEST：丢弃最新的值，保留最老的。

     */
    private fun shareFlowTest(){
        val sharedFlow = MutableSharedFlow<Int>()

        lifecycleScope.launch {
            sharedFlow.emit(1)
            sharedFlow.emit(2)
            sharedFlow.emit(3)
            sharedFlow.emit(4)
            sharedFlow.collect{
                delay(1000)
                Log.d("zz","sharedFlow收到的数据${it}")
            }
        }
    }

    /**
     * MutableSharedFlow(1,0, BufferOverflow.SUSPEND)
     * StateFlow使用collect方法注册观察者，每一个观察者内部都维护了一个死循环，当数据更新完成的时候会将协程挂起保证死循环不退出。
        每一个StateFlow都会维护一个StateFlowSlot的数组(可以称之为状态槽slots)用来管理每个观察者的状态。当我们调用collect方法注册了一个观察者的时候，首先会将StateFlow中当前的值回调给观察者，然后StateFlow会将观察者的协程挂起，等待下一次数据更新到来。
        当下一次数据到来的时候，StateFlow会将数据更新到最新值，然后会遍历slots，将所有观察者对应的协程结束挂起，这样所有的观察者，会继续在死循环中去读取StateFlow中的最新值，并回调。
     */
    private fun stateFlowTest(){
        val stateFlow = MutableStateFlow(0)
        lifecycleScope.launch {
            stateFlow.emit(1)
            stateFlow.emit(2)
            stateFlow.emit(3)
            stateFlow.emit(4)
            stateFlow.collect{
                delay(1000)
                Log.d("zz","stateFlow收到的数据${it}")
            }
        }
    }
}