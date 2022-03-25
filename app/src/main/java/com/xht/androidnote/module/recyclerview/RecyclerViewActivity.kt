package com.xht.androidnote.module.recyclerview

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewCacheExtension
import butterknife.BindView
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.util.*

/**
 * Created by xht on 2019/8/15.
 */
class RecyclerViewActivity : BaseActivity() {
    var list: MutableList<String> = ArrayList()

    lateinit var adapter: RvTestAdapter
    override fun getLayoutId(): Int {
        return R.layout.activity_recyclerview
    }

    override fun initEventAndData() {
        initView()

        for (i in 0..19) {
            list.add(i.toString() + "")
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        adapter = RvTestAdapter(list, this)

        //关闭预取功能
        layoutManager.isItemPrefetchEnabled = false

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        //设置mCachedView 缓存大小，默认是2
        //recyclerView.setItemViewCacheSize(5);
//        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (recyclerView!!.canScrollVertically(1)) {
//                } else {
//                    //滑动到底部
//                    Log.i("xht", "滑动到底部")
//                }
//                if (recyclerView!!.canScrollVertically(-1)) {
//                } else {
//                    Log.i("xht", "滑动到顶部")
//                }
//            }
//        })
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val firstCompletelyVisibleItemPosition =
                    linearLayoutManager!!.findFirstCompletelyVisibleItemPosition()
                if (firstCompletelyVisibleItemPosition == 0) {
                    Log.i("xht", "滑动到顶部。。。。")
                }
                val lastCompletelyVisibleItemPosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                if (lastCompletelyVisibleItemPosition == linearLayoutManager.itemCount - 1) {
                    Log.i("xht", "滑动到底部。。。。")
                }
            }
        })
    }

    private fun initView() {
        btnInsert.setOnClickListener {
        }

        btnUpdate.setOnClickListener {
            list[1] = "111"
            adapter.notifyItemChanged(1)
        }
        btnPartialRefresh.setOnClickListener {
            list[1] = "1+a"
            adapter.notifyItemChanged(1, "aaa")
        }
    }

    class Recycler {
        //一级缓存
        //存储的是当前还在屏幕中的 ViewHolder；按照 id 和 position 来查找 ViewHolder
        val mAttachedScrap = ArrayList<RecyclerView.ViewHolder>()

        //表示数据已经改变的 ViewHolder 列表, 存储 notifyXXX 方法时需要改变的 ViewHolder
        var mChangedScrap: ArrayList<RecyclerView.ViewHolder>? = null

        //二级缓存
        //用来缓存移除屏幕之外的 ViewHolder，默认情况下缓存容量是 2，可以通过 setViewCacheSize 方法来改变缓存的容量大小。
        //如果 mCachedViews 的容量已满，则会根据 FIFO 的规则移除旧 ViewHolder
        val mCachedViews = ArrayList<RecyclerView.ViewHolder>()

        //三级缓存
        //开发给用户的自定义扩展缓存，需要用户自己管理 View 的创建和缓存。
        private val mViewCacheExtension: ViewCacheExtension? = null

        //四级缓存
        /*
            ViewHolder 缓存池，在有限的 mCachedViews 中如果存不下新的 ViewHolder 时，根据FIFO的原则，把最新存入mCachedView中的
            viewHolder 存入 RecyclerViewPool 中，然后从mCachedView中移除。
            按照 Type 来查找 ViewHolder
            每个 Type 默认最多缓存 5 个
            可以多个 RecyclerView 共享 RecycledViewPool
         */
        var mRecyclerPool: RecycledViewPool? = null

        companion object {
            const val DEFAULT_CACHE_SIZE = 2
        }
    }
}