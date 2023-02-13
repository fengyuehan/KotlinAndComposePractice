package com.example.kotlinandcompose.Flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinandcompose.MainAdapter
import com.example.kotlinandcompose.MainBean
import com.example.kotlinandcompose.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowActivity:AppCompatActivity() {
    private lateinit var mAdapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)
        initView()
        initData()
        initListener()
    }

    private fun initListener() {
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as MainBean
            when(bean.id){
                1 -> {
                    flowCreate1()
                    flowCreate2()
                    flowCreate3()
                }
                2 ->{
                    flowOnTest()
                }
                3 ->{
                    test1()
                }
                4 ->{
                    zipTest2()
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

    /**
     * Flow的创建有三种方式
     * 1.flow {...}
     * 2.asFlow
     * 3.flowOf
     */
    private fun flowCreate1(){
        lifecycleScope.launchWhenResumed {
            flow {
                for (i in 1..3){
                    delay(100)
                    emit(i)
                }
            }.collect{
                value -> Log.d("zz","value:${value}")
            }
        }
    }

    private fun flowCreate2(){
        lifecycleScope.launch {
            (1..3).asFlow().collect { value -> Log.d("zz", "value :${value}") }
        }
    }

    private fun flowCreate3(){
        lifecycleScope.launch {
            flowOf(1, 2, 2, 3).collect { value ->
                Log.d("zz", "collect :${value}")
            }
        }

    }

    /**
     * 线程的切换 flowOn
     * flowOn可以将执行此流的上下文更改为指定的上下文。
        flowOn可以进行组合使用。
        flowOn只影响前面没有自己上下文的操作符。已经有上下文的操作符不受后面flowOn影响。
        不管flowOn如何切换线程,collect始终是运行在调用它的协程调度器上。
     */
    fun flowOnTest(){
        lifecycleScope.launch {
            flow {
                for (i in 1..3) {
                    Log.d("zz", "flow :${ currentCoroutineContext()}")
                    delay(100)
                    emit(i)
                }
            }.flowOn(Dispatchers.IO)
                .map {
                    Log.d("zz", "map :${ currentCoroutineContext()}")
                    it
                }.flowOn(Dispatchers.Default)
                .flowOn(Dispatchers.IO) //只有第一个才有效
                .collect { value ->
                    Log.d("zz", "collect:${ currentCoroutineContext()} value :${value}")
                }
        }
    }

    /**
     * 执行流程依次是onStart->flow{ ...}->onEach->collect->onCompletion
     */
    fun test1(){
        lifecycleScope.launch {
            flow {
                Log.d("zz", "flow")
                emit(1)
            }.onStart {
                Log.d("zz", "onStart ")
            }.onEach {
                Log.d("zz", "onEach :${it}")
            }.onCompletion {
                Log.d("zz", "onCompletion")
            }.collect { value ->
                Log.d("zz", "collect :${value}")
            }
        }
    }

    /**
     * 异常操作符：用来捕获处理流的异常。比如：catch,onErrorCollect(已废弃，建议用catch)。
        转换操作符：主要做一些数据转换操作。比如：transform/map/filter/flatMapConcat/flatMapMerge/debounce/distinctUntilChanged 等
            debounce:防抖
            distinctUntilChanged ：过滤掉重复的请求
            后缀为 Concat 是串行，后缀为 Merge 的是并行
        限制操作符：流触及相应限制的时候会将它的执行取消。比如：drop/take等
                  take操作符返回包含第一个计数元素的流。当发射次数大于等于count的值时，通过抛出异常来取消执行。
                    takeWhile操作符与filter类似，不过它是当遇到条件判断为false的时候，将会中断后续的操作。
                  drop操作符与take恰恰相反，它是丢弃掉指定的count数量后执行后续的流。
    末端操作符：是在流上用于启动流收集挂起函数。collect是最基础的末端操作符，但是还有另外一些更方便使用的末端操作符。例如：toList、toSet、first、last、single、reduce、fold等等
    reduce、fold:将flow处理成一个值,fold()有一个初始值，而reduce()则没有
    single：flow只发射一个数据，发多一个就报错
    first、last：取第一个和取后一个
     */
    private fun zipTest2(){
        lifecycleScope.launch {
            val flow1 = flowOf(1,2).onEach {
                delay(100)
            }
            val flow2 = flowOf("a","b","c").onEach {
                delay(200)
            }
            /**
             * 两个 flow 会互相等待，等两个 flow 都收到了数据时，就会走到这里，有一个 flow 退出了组合体就会退出
             * 2023-02-13 16:46:52.908 31615-31615/com.example.kotlinandcompose D/zz: zip:1 -- a
            2023-02-13 16:46:53.108 31615-31615/com.example.kotlinandcompose D/zz: zip:2 -- b
             */
            flow1.zip(flow2){
                a,b ->
                "${a} -- ${b}"
            }.collect{
                Log.d("zz","zip:${it}")
            }
            /**
             * 两个 flow 分别接收数据，接收慢的 flow 收到数据时会走到这里（接收快的那个此处会保存最新接收的值），两个 flow 都退出了组合体就会退出
             * 2023-02-13 16:46:53.312 31615-31615/com.example.kotlinandcompose D/zz: combine:1 -- a
            2023-02-13 16:46:53.314 31615-31615/com.example.kotlinandcompose D/zz: combine:2 -- a
            2023-02-13 16:46:53.512 31615-31615/com.example.kotlinandcompose D/zz: combine:2 -- b
            2023-02-13 16:46:53.715 31615-31615/com.example.kotlinandcompose D/zz: combine:2 -- c
             */
            flow1.combine(flow2){
                    a,b ->
                "${a} -- ${b}"
            }.collect{
                Log.d("zz","combine:${it}")
            }
        }
    }

}