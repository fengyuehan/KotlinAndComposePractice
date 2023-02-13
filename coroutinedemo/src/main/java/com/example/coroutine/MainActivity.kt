package com.example.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter:MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv.layoutManager = LinearLayoutManager(this)
        mAdapter = MainAdapter()
        rv.adapter = mAdapter
        initData()

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as MainBean
            when(bean.id){
                1 -> {
                    start()
                }
                2 ->{
                    context()
                }
                3 -> {
                    testCoroutineScope()
                }
                4 -> {
                    testCoroutineScope1()
                }
                5 -> {
                    testCoroutineScope2()
                }
                6 -> {
                    testCoroutineExceptionHandler()
                }
            }
        }
    }

    private fun initData() {
        val mData = ArrayList<MainBean>()
        mData.add(MainBean("runBlocking,lunch和async的比较",1))
        mData.add(MainBean("协程上下文",2))
        mData.add(MainBean("协程作用域 CoroutineScope",3))
        mData.add(MainBean("主从作用域 supervisorScope ",4))
        mData.add(MainBean("协从作用域",5))
        mData.add(MainBean("测试CoroutineExceptionHandler",6))
        mAdapter.setList(mData)
    }

    private fun start(){
        /**
         * runBlocking启动的是一个新的协程并阻塞调用它的线程
         * launch返回一个job,job的状态有New，Active，Completing，Cancelling，Cancelled，Completed。可以通过isActive、isCompleted、isCancelled来获取到Job的当前状态
         * async返回的DeferredCoroutine对象，通过await()方法来获取返回值
         */
        val runBlockingJob = runBlocking {
            Log.d("runBlocking", "启动一个协程")
            41
        }
        Log.d("runBlockingJob", "$runBlockingJob")
        val launchJob = GlobalScope.launch{
            Log.d("launch", "启动一个协程")
        }
        Log.d("launchJob", "$launchJob")
        val asyncJob = GlobalScope.async{
            Log.d("async", "启动一个协程")
            "我是返回值"
        }
        Log.d("asyncJob", "$asyncJob")
    }

    /**
     * 协程调度器CoroutineDispatcher
     * Default：默认调度器，CPU密集型任务调度器，适合处理后台计算。通常处理一些单纯的计算任务，或者执行时间较短任务。比如：Json的解析，数据计算等
     * IO：IO调度器，，IO密集型任务调度器，适合执行IO相关操作。比如：网络处理，数据库操作，文件操作等
     * Main：UI调度器， 即在主线程上执行，通常用于UI交互，刷新等
     * Unconfined：非受限调度器，又或者称为“无所谓”调度器，不要求协程执行在特定线程上。
     */

    /**
     * plus有个关键字operator表示这是一个运算符重载的方法，类似List.plus的运算符，可以通过+号来返回一个包含原始集合和第二个操作数中的元素的结果。同理CoroutineContext中是通过plus来返回一个由原始的Element集合和通过+号引入的Element产生新的Element集合。
      get方法，顾名思义。可以通过 key 来获取一个Element
      fold方法它和集合中的fold是一样的，用来遍历当前协程上下文中的Element集合。
      minusKey方法plus作用相反，它相当于是做减法,是用来取出除key以外的当前协程上下文其他Element，返回的就是不包含key的协程上下文。
     */
    fun context(){
        val coroutineConText1 = Job() +CoroutineName("这是一个上下文")
        Log.d("coroutineConText1","$coroutineConText1")
        val coroutineConText2 = coroutineConText1 + Dispatchers.Default + CoroutineName("这是第二个上下文")
        Log.d("coroutineConText1","$coroutineConText2")
        val  coroutineConText3 = coroutineConText2 +Dispatchers.Main + CoroutineName("这是第三个上下文")
        Log.d("coroutineConText1","$coroutineConText3")
    }

    /**
     * CoroutineStart协程启动模式，是启动协程时需要传入的第二个参数。协程启动有4种：
        DEFAULT    默认启动模式，我们可以称之为饿汉启动模式，因为协程创建后立即开始调度，虽然是立即调度，单不是立即执行，有可能在执行前被取消。
        LAZY    懒汉启动模式，启动后并不会有任何调度行为，直到我们需要它执行的时候才会产生调度。也就是说只有我们主动的调用Job的start、join或者await等函数时才会开始调度。
        ATOMIC  一样也是在协程创建后立即开始调度，但是它和DEFAULT模式有一点不一样，通过ATOMIC模式启动的协程执行到第一个挂起点之前是不响应cancel 取消操作的，ATOMIC一定要涉及到协程挂起后cancel 取消操作的时候才有意义。
        UNDISPATCHED 协程在这种模式下会直接开始在当前线程下执行，直到运行到第一个挂起点。这听起来有点像 ATOMIC，不同之处在于UNDISPATCHED是不经过任何调度器就开始执行的。当然遇到挂起点之后的执行，将取决于挂起点本身的逻辑和协程上下文中的调度器。
     */

    /**
     * 协程作用域 CoroutineScope
     * 顶级作用域 --> 没有父协程的协程所在的作用域称之为顶级作用域。 GlobalScope
     * 协同作用域 --> 在协程中启动一个协程，新协程为所在协程的子协程。子协程所在的作用域默认为协同作用域。此时子协程抛出未捕获的异常时，会将异常传递给父协程处理，如果父协程被取消，则所有子协程同时也会被取消。
     * 主从作用域 官方称之为监督作用域。与协同作用域一致，区别在于该作用域下的协程取消操作的单向传播性，子协程的异常不会导致其它子协程取消。但是如果父协程被取消，则所有子协程同时也会被取消。supervisorScope
     */
    private fun testCoroutineScope(){
        GlobalScope.launch(Dispatchers.Main) {
            Log.d("zz","$coroutineContext")
            launch (CoroutineName("第一个子协程")){
                Log.d("zz","$coroutineContext")
            }
            launch (Dispatchers.IO){
                Log.d("zz","$coroutineContext")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun testCoroutineScope1(){
        val exceptionHandler = CoroutineExceptionHandler{
            coroutineContext, throwable ->
            Log.d("zz","${coroutineContext[CoroutineName]}${throwable.message}")
        }
        GlobalScope.launch(Dispatchers.Main + CoroutineName("scope1") + exceptionHandler) {
            supervisorScope {
                Log.d("scope", "--------- 1")
                launch(CoroutineName("scope2")) {
                    Log.d("scope", "--------- 2")
                    throw  NullPointerException("空指针")
                    Log.d("scope", "--------- 3")
                    val scope3 = launch(CoroutineName("scope3")) {
                        Log.d("scope", "--------- 4")
                        delay(2000)
                        Log.d("scope", "--------- 5")
                    }
                    scope3.join()
                }
                val scope4 = launch(CoroutineName("scope4")) {
                    Log.d("scope", "--------- 6")
                    delay(2000)
                    Log.d("scope", "--------- 7")
                }
                scope4.join()
                Log.d("scope", "--------- 8")
            }
        }

    }

    private fun testCoroutineScope2(){
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("exceptionHandler", "${coroutineContext[CoroutineName]} $throwable")
        }
        GlobalScope.launch(Dispatchers.Main + CoroutineName("scope1") + exceptionHandler) {
            Log.d("scope", "--------- 1")
            launch(CoroutineName("scope2") + exceptionHandler) {
                Log.d("scope", "--------- 2")
                throw  NullPointerException("空指针")
                Log.d("scope", "--------- 3")
            }
            val scope3 = launch(CoroutineName("scope3") + exceptionHandler) {
                Log.d("scope", "--------- 4")
                delay(2000)
                Log.d("scope", "--------- 5")
            }
            scope3.join()
            Log.d("scope", "--------- 6")
        }
    }

    private fun testCoroutineExceptionHandler(){
        GlobalScope.launch {
            val job = launch {
                Log.d("${Thread.currentThread().name}", " 抛出未捕获异常")
                throw NullPointerException("异常测试")
            }
            job.join()
            Log.d("${Thread.currentThread().name}", "end")
        }
    }

}